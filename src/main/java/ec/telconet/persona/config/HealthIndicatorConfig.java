package ec.telconet.persona.config;

import com.google.gson.Gson;
import ec.telconet.microservicio.dependencia.util.general.ConsumoWebService;
import ec.telconet.microservicio.dependencia.util.general.Conversion;
import ec.telconet.microservicio.dependencia.util.general.Formato;
import ec.telconet.persona.dto.Metricas;
import ec.telconet.persona.service.ConsumoKafkaService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Component
public class HealthIndicatorConfig extends AbstractHealthIndicator {
    Logger log = LogManager.getLogger(this.getClass());

    @Value(value = "${spring.kafka.bootstrap-servers:kafka-1:19092,kafka-2:29092,kafka-3:39092}")
    private String bootstrapServers;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConsumoWebService consumoWebService;

    @Autowired
    private Conversion conversion;

    @Autowired
    Environment environment;

    @Autowired
    private ConsumoKafkaService consumoKafkaService;

    private HashMap<String, String> mapMail = new HashMap<>();

    private String asuntoMail = "Health check microservicio Persona";

    @Value(value = "${management.endpoint.health.notification.memoryPercentMax:95}")
    private long porcentMemoria;

    @Value(value = "${management.endpoint.health.notification.mailFrom}")
    private String fromMail;

    @Value("#{'${management.endpoint.health.notification.mailTo}'.split(';')}")
    private String toMail;

    @Autowired
    private Formato formato;

    @Override
    protected void doHealthCheck(Builder builder) throws Exception {
        List<String> listToMail = new ArrayList<>();
        listToMail.add(toMail);

        String port = environment.getProperty("server.port");

        String mensaje = "";
        String error = "";

        boolean booHealth = false;

        int errorCode = check();
        if (errorCode != 1) {
            booHealth = true;
            error = " Failed to obtain JDBC Connection";
            log.error("doHealthCheck exception jdbc: {}", error);
            mensaje = "Error en el microservicio exception jdbc " + error;
            mapMail.put("mensaje", mensaje);
            consumoKafkaService.enviarCorreo(fromMail, asuntoMail, listToMail, formato.crearBodyHealthCheck(mapMail));
            builder.down().withDetail("database", error);
        }

        try {
            if (!booHealth) {
                Properties properties = new Properties();
                properties.put("bootstrap.servers", bootstrapServers);
                AdminClient kafkaAdminClient = AdminClient.create(properties);
                DescribeClusterOptions options = new DescribeClusterOptions().timeoutMs(30000);
                DescribeClusterResult clusterDesc = kafkaAdminClient.describeCluster(options);
                builder.up().withDetail("kafka", "Validando cluster kafka")
                        .withDetail("clusterId", clusterDesc.clusterId().get())
                        .withDetail("nodeCount", clusterDesc.nodes().get().size()).build();
                kafkaAdminClient.close();
            }
        } catch (Exception ex) {
            error = ex.getMessage();
            log.error("doHealthCheck exception kafka: {}", error);
            mensaje = "Error en el microservicio exception kafka " + error;
            mapMail.put("mensaje", mensaje);
            consumoKafkaService.enviarCorreo(fromMail, asuntoMail, listToMail, formato.crearBodyHealthCheck(mapMail));
            builder.down().withDetail("kafka", ex);
        }

        log.trace("doHealthCheck estado: {}", builder.build().getStatus().toString());
        if (builder.build().getStatus().toString().equalsIgnoreCase("DOWN")) {
            log.trace("doHealthCheck DOWN valida error: {}", error.isEmpty());
            if (error.isEmpty()) {
                mensaje = "El estado de microservicio es down no se a determinado la causa por favor revisar logs";
                mapMail.put("mensaje", mensaje);
                log.trace("doHealthCheck if de estado: ");
                consumoKafkaService.enviarCorreo(fromMail, asuntoMail, listToMail, formato.crearBodyHealthCheck(mapMail));
            }
        } else {
            String usedMemory = consumoWebService
                    .getJsonRest("http://localhost:" + port + "/actuator/metrics/jvm.memory.used");
            Gson gson = new Gson();
            Metricas usedMetricas = gson.fromJson(usedMemory, Metricas.class);
            long usedTamano = conversion.bytesaformatolegible(usedMetricas.getMeasurements().get(0).getValue(), "MB");
            mapMail.put("usada", usedTamano + " MB");
            log.trace("doHealthCheck tamano de memoria usada: {} MB", usedTamano);

            String maxMemory = consumoWebService
                    .getJsonRest("http://localhost:" + port + "/actuator/metrics/jvm.memory.max");
            Metricas maxMetricas = gson.fromJson(maxMemory, Metricas.class);
            long maxTamano = conversion.bytesaformatolegible(maxMetricas.getMeasurements().get(0).getValue(), "MB");
            mapMail.put("total", maxTamano + " MB");
            log.trace("doHealthCheck tamano de memoria maxima: {} MB", maxTamano);

            long porcentajememoria = ((maxMetricas.getMeasurements().get(0).getValue()) * (porcentMemoria)) / 100;
            long porcentaje95 = conversion.bytesaformatolegible(porcentajememoria, "MB");
            log.trace("doHealthCheck pocentaje de memoria {}%: {} MB", porcentMemoria, porcentaje95);

            builder.up().withDetail("Memoria jvm", "Verificando memoria jvm").withDetail("Memoria maxima", usedTamano)
                    .withDetail("Memoria usada", maxTamano).build();

            if (usedTamano > porcentaje95) {
                mensaje = "El porcentaje de memoria esta llegando al limite revisar logs.";
                mapMail.put("mensaje", mensaje);
                consumoKafkaService.enviarCorreo(fromMail, asuntoMail, listToMail, formato.crearBodyHealthCheck(mapMail));
            }
        }
    }

    public int check() {
        List<Object> results = jdbcTemplate.query("select 1 from dual", new SingleColumnRowMapper<>());
        return results.size();
    }
}
