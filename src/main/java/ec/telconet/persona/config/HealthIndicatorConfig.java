package ec.telconet.persona.config;

import ec.telconet.persona.service.ConsumoKafkaService;
import ec.telconet.microservicio.dependencia.util.cons.CoreUtilConstants;
import ec.telconet.microservicio.dependencia.util.dto.ParamsDTO;
import ec.telconet.microservicio.dependencia.util.exception.GenericException;
import ec.telconet.microservicio.dependencia.util.general.ConsumoWebService;
import ec.telconet.microservicio.dependencia.util.general.Formato;
import lombok.Data;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.*;

/**
 * Configuración HealthCheck
 *
 * @author Marlon Plúas
 * @since 16/06/2022
 */
@Component("healthCustom")
public class HealthIndicatorConfig extends AbstractHealthIndicator {
    Logger log = LogManager.getLogger(this.getClass());

    private final String urlHealth;
    private final String mailSubject;
    private Boolean sendMailForWs = false;

    @Value("${spring.kafka.bootstrap-servers:localhost}")
    private String bootstrapAddress;

    @Value("#{'${management.endpoint.health.notification.mailTo}'.split(';')}")
    private List<String> mailTo;

    @Value("${management.endpoint.health.notification.mailFrom}")
    private String mailFrom;

    @Value("${management.endpoint.health.notification.mailUrl}")
    private String mailUrl;

    @Value("${management.endpoint.health.notification.memoryPercentMax}")
    private long percentagePermitted;

    @Autowired
    private ConsumoWebService webService;

    @Autowired
    private ConsumoKafkaService consumoKafkaService;

    private DataMemoryJVM dataMemoryJVM;

    public HealthIndicatorConfig(@Value("${server.port}") String port, @Value("${aplicacion.nombre}") String nameApp) {
        this.urlHealth = "http://localhost:" + port + "/actuator";
        this.mailSubject = "HealthCheck Microservicio " + nameApp;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        ThreadContext.remove("idTransaccion");
        ThreadContext.remove("ipCliente");
        ThreadContext.remove("timeRequest");
        this.sendMailForWs = false;
        log.info("Evaluando estado de salud del microservicio, fecha {}", new Date());
        this.dataMemoryJVM = checkMemoryJVM();
        healthCheckKafka(builder);
        if (builder.build().getStatus().toString().equalsIgnoreCase("UP")) {
            healthCheckMemory(builder);
        }
        healthCheckDefault();
    }

    /**
     * Método para consultar el estado de salud de kafka
     *
     * @param builder Builder
     *
     * @author Marlon Plúas
     * @since 16/06/2022
     */
    private void healthCheckKafka(Health.Builder builder) {
        try {
            if (!bootstrapAddress.equalsIgnoreCase("localhost")) {
                Properties properties = new Properties();
                properties.put("bootstrap.servers", bootstrapAddress);
                AdminClient kafkaAdminClient = AdminClient.create(properties);
                DescribeClusterOptions options = new DescribeClusterOptions().timeoutMs(30000);
                DescribeClusterResult clusterDesc = kafkaAdminClient.describeCluster(options);
                builder.up().withDetail("kafka", "kafka cluster operativo, número de nodos: " + clusterDesc.nodes().get().size()).build();
                kafkaAdminClient.close();
            } else {
                this.sendMailForWs = true;
                builder.up().withDetail("Kafka", "Cluster no disponible").build();
            }
        } catch (Exception e) {
            this.sendMailForWs = true;
            List<ParamsDTO> listPropertiesBody = new ArrayList<>();
            String problem = "Microservicio presenta errores en la conexión con kafka: " + e.getMessage();
            String recommendations = "1. Revisar los logs para identificar el error.</br>2. Revisar los nodos del cluster kafka.</br>3. Los errores de " +
                                     "kafka se resuelven de manera automática.";
            listPropertiesBody.add(new ParamsDTO("{{MensajeHealthCheck}}", problem));
            listPropertiesBody.add(new ParamsDTO("{{RecomendacionesHealthCheck}}", recommendations));
            sendMailHealthCheck(listPropertiesBody);
            builder.down().withDetail("Kafka", e.getMessage()).build();
        }
    }

    /**
     * Método para consultar el estado de salud de componentes que vienen por defecto
     *
     * @author Marlon Plúas
     * @since 16/06/2022
     */
    private void healthCheckDefault() {
        List<ParamsDTO> listPropertiesBody = new ArrayList<>();
        String problem;
        String recommendations;
        try {
            String errorMongo = healthType("mongo");
            if (errorMongo != null) {
                problem = "Microservicio presenta problemas en la conexión con MongoDB: " + errorMongo;
                recommendations = "1. Revisar los logs para identificar el error.</br>2. Verificar en conjunto con el DBA el estado de la base mongo y sus " +
                                  "conexiones.";
                listPropertiesBody.add(new ParamsDTO("{{MensajeHealthCheck}}", problem));
                listPropertiesBody.add(new ParamsDTO("{{RecomendacionesHealthCheck}}", recommendations));
                sendMailHealthCheck(listPropertiesBody);
            }
            String errorJDBC = healthType("db");
            if (errorJDBC != null) {
                problem = "Microservicio presenta problemas en la conexión con un DataSource de JDBC: " + errorJDBC;
                recommendations = "1. Revisar los logs para identificar el error.</br>2. Verificar en conjunto con el DBA el estado de la base del datasource y sus " +
                                  "conexiones.";
                listPropertiesBody.add(new ParamsDTO("{{MensajeHealthCheck}}", problem));
                listPropertiesBody.add(new ParamsDTO("{{RecomendacionesHealthCheck}}", recommendations));
                sendMailHealthCheck(listPropertiesBody);
            }
        } catch (Exception e) {
            log.error("Error al verificar el estado de salud de los componentes por default: {}", e.getMessage());
        }
    }

    /**
     * Método para consultar el estado de salud dinámico
     *
     * @param type Tipo
     *
     * @return String
     *
     * @author Marlon Plúas
     * @since 16/06/2022
     */
    private String healthType(String type) {
        String response = null;
        MetricsHealth health = new MetricsHealth();
        try {
            ResponseEntity<Object> resWSHealth = webService.genericObjectRest(urlHealth.concat("/health/" + type), MediaType.APPLICATION_JSON,
                                                                              HttpMethod.GET, null, null, false);
            health = Formato.mapearObjDeserializado(resWSHealth.getBody(), MetricsHealth.class);
        } catch (Exception e) {
            boolean isGenericException = e.getCause() instanceof GenericException || e instanceof GenericException;
            if (e.getMessage() != null && e.getMessage().length() > 0 && !isGenericException) {
                String replaceErro503 = e.getMessage().replace("503 : \"", "");
                String deleteCaracterEnd = replaceErro503.substring(0, replaceErro503.length() - 1);
                health = Formato.mapearObjectToString(deleteCaracterEnd, MetricsHealth.class);
            }
        }
        if (health.getStatus() != null && health.getStatus().equalsIgnoreCase("DOWN")) {
            if (type.equalsIgnoreCase("db")) {
                for (Map.Entry<String, ComponentHealth> entry : health.getComponents().entrySet()) {
                    if (entry.getValue() != null && entry.getValue().getStatus() != null && entry.getValue().getStatus().equalsIgnoreCase("DOWN")) {
                        response = entry.getValue().getDetails().getError();
                        break;
                    }
                }
            } else {
                response = health.getDetails().getError();
            }
        }
        return response;
    }

    /**
     * Método para consultar el estado de salud de la memoria JVM
     *
     * @param builder Builder
     *
     * @author Marlon Plúas
     * @since 16/06/2022
     */
    private void healthCheckMemory(Health.Builder builder) {
        List<ParamsDTO> listPropertiesBody = new ArrayList<>();
        String problem = "Microservicio ha superado la memoria java permitida: ";
        String recommendations = "1. Revisar los logs para identificar el error.</br>2. Aumentar una o más replicas para equilibrar el alto consumo de " +
                                 "memoria.";
        long valuePercentagePermitted = (this.percentagePermitted * this.dataMemoryJVM.memoryMax) / 100;
        String summaryMessage = "Memoria actual: " + byteConvertFormat(this.dataMemoryJVM.memoryUsed, "MB") + " MB, Memoria permitida: " + byteConvertFormat(
                valuePercentagePermitted, "MB") + " MB, Memoria activación GC: " +
                                byteConvertFormat(this.dataMemoryJVM.memoryCommitted, "MB") + " MB, Memoria total: " + byteConvertFormat(
                this.dataMemoryJVM.memoryMax, "MB") + " MB";
        if (this.dataMemoryJVM.memoryUsed > valuePercentagePermitted) {
            problem = problem + summaryMessage;
            listPropertiesBody.add(new ParamsDTO("{{MensajeHealthCheck}}", problem));
            listPropertiesBody.add(new ParamsDTO("{{RecomendacionesHealthCheck}}", recommendations));
            sendMailHealthCheck(listPropertiesBody);
            builder.down().withDetail("Memory", summaryMessage).build();
        } else {
            builder.up().withDetail("Memory", summaryMessage).build();
        }
    }

    /**
     * Método para enviar notificación via kafka o ws
     *
     * @param listPropertiesBody Lista de parámetros a reemplazar
     *
     * @author Marlon Plúas
     * @since 16/06/2022
     */
    private void sendMailHealthCheck(List<ParamsDTO> listPropertiesBody) {
        listPropertiesBody.add(new ParamsDTO("{{TotalMemoriaJVM}}", byteConvertFormat(this.dataMemoryJVM.memoryMax, "MB") + " MB"));
        listPropertiesBody.add(new ParamsDTO("{{MemoriaJVMUsada}}", byteConvertFormat(this.dataMemoryJVM.memoryUsed, "MB") + " MB"));
        String problemBody = Formato.replacePlantillaByParams(CoreUtilConstants.BODY_HEALTH_CHECK, listPropertiesBody);
        if (this.sendMailForWs) {
            try {
                webService.postJsonRest(this.mailUrl, new MailReqDTO(this.mailSubject, this.mailFrom, this.mailTo, problemBody));
            } catch (Exception e) {
                log.error("No se pudo enviar el correo de la notificación HealthCheck : {}", e.getMessage());
            }
        } else {
            consumoKafkaService.enviarCorreo(this.mailFrom, this.mailSubject, this.mailTo, problemBody);
        }
    }

    private DataMemoryJVM checkMemoryJVM() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        return new DataMemoryJVM(heapMemoryUsage.getUsed(), heapMemoryUsage.getCommitted(), heapMemoryUsage.getMax());
    }

    @Override
    public Health getHealth(boolean includeDetails) {
        return super.getHealth(includeDetails);
    }

    @Data
    public static class DataMemoryJVM {
        private long memoryUsed;
        private long memoryCommitted;
        private long memoryMax;

        public DataMemoryJVM(long memoryUsed, long memoryCommitted, long memoryMax) {
            this.memoryUsed = memoryUsed;
            this.memoryCommitted = memoryCommitted;
            this.memoryMax = memoryMax;
        }
    }

    @Data
    public static class MetricsHealth {
        private String status;
        private DetailHealth details;
        private Map<String, ComponentHealth> components;
    }

    @Data
    public static class DetailHealth {
        private String error;
    }

    @Data
    public static class ComponentHealth {
        private String status;
        private DetailHealth details;
    }

    @Data
    public static class MailReqDTO {
        private String subject;
        private String from;
        private List<String> to;
        private String body;

        public MailReqDTO(String subject, String from, List<String> to, String body) {
            this.subject = subject;
            this.from = from;
            this.to = to;
            this.body = body;
        }
    }

    public long byteConvertFormat(long bytes, String tipo) {
        long kilobyte = 1024L;
        long megabyte = kilobyte * 1024L;
        long gigabyte = megabyte * 1024L;
        long terabyte = gigabyte * 1024L;
        if (bytes >= 0L && bytes < kilobyte) {
            return bytes;
        } else if (tipo.equalsIgnoreCase("KB")) {
            return bytes / kilobyte;
        } else if (tipo.equalsIgnoreCase("MB")) {
            return bytes / megabyte;
        } else if (tipo.equalsIgnoreCase("GB")) {
            return bytes / gigabyte;
        } else {
            return bytes >= terabyte ? bytes / terabyte : bytes;
        }
    }
}
