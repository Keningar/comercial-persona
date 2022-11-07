package ec.telconet.persona.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.telconet.microservicio.core.general.kafka.cons.CoreGeneralConstants;
import ec.telconet.microservicio.core.general.kafka.request.CorreoKafkaReq;
import ec.telconet.microservicio.core.general.kafka.request.ParametroDetKafkaReq;
import ec.telconet.microservicio.core.general.kafka.response.ParametroDetKafkaRes;
import ec.telconet.microservicio.dependencia.util.enumerado.StatusHandler;
import ec.telconet.microservicio.dependencia.util.exception.GenericException;
import ec.telconet.microservicio.dependencia.util.general.Formato;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaRequest;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaResponse;
import ec.telconet.microservicio.dependencia.util.kafka.producer.asynch.ProducerAsynchroImpl;
import ec.telconet.microservicio.dependencia.util.kafka.producer.synch.ProducerSynchroImpl;
import ec.telconet.microservicios.dependencias.esquema.general.entity.AdmiParametroDet;

/**
 * Clase utilizada donde se encuentran los servicios de los consumos kafka generales
 *
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 15/10/2020
 */
@Service
public class ConsumoKafkaService {
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    ProducerAsynchroImpl producerAsynchro;
    

    @Autowired
    ProducerSynchroImpl producerSynchro;

    public void enviarCorreo(String remitente, String asunto, List<String> to, String body,
                             String rutaArchivos, List<String> archivos,
                             String compression) {
        KafkaRequest<CorreoKafkaReq> kafkaReqCorreo = new KafkaRequest<>();
        CorreoKafkaReq dataCorreo = new CorreoKafkaReq();
        dataCorreo.setFrom(remitente);
        dataCorreo.setTo(to);
        dataCorreo.setSubject(asunto);
        dataCorreo.setBody(body);
        dataCorreo.setCompression(compression);
        if (rutaArchivos != null && !archivos.isEmpty()) {
            dataCorreo.setRutaArchivos(rutaArchivos);
            dataCorreo.setAttachment(archivos);
        }
        kafkaReqCorreo.setTopicName(CoreGeneralConstants.TOPIC_CORREO_ASYN);
        kafkaReqCorreo.setOp(CoreGeneralConstants.OP_ENVIAR_CORREO);
        kafkaReqCorreo.setData(dataCorreo);
        kafkaReqCorreo.setEsUtilitario(true);
        log.info("Consumiendo kafka {}", CoreGeneralConstants.TOPIC_CORREO_ASYN);
        producerAsynchro.sendKafkaAsynchro(kafkaReqCorreo);
    }

    public void enviarCorreo(String remitente, String asunto, List<String> to, String body) {
        KafkaRequest<CorreoKafkaReq> kafkaReqCorreo = new KafkaRequest<>();
        CorreoKafkaReq dataCorreo = new CorreoKafkaReq();
        dataCorreo.setFrom(remitente);
        dataCorreo.setTo(to);
        dataCorreo.setSubject(asunto);
        dataCorreo.setBody(body);
        kafkaReqCorreo.setTopicName(CoreGeneralConstants.TOPIC_CORREO_ASYN);
        kafkaReqCorreo.setOp(CoreGeneralConstants.OP_ENVIAR_CORREO);
        kafkaReqCorreo.setData(dataCorreo);
        kafkaReqCorreo.setEsUtilitario(true);
        log.info("Consumiendo kafka {}", CoreGeneralConstants.TOPIC_CORREO_ASYN);
        producerAsynchro.sendKafkaAsynchro(kafkaReqCorreo);
    }
    
    
    public List<ParametroDetKafkaRes> consumirParam(String descripcion, String codEmpresa) throws GenericException {
        List<ParametroDetKafkaRes> response;
        KafkaRequest<AdmiParametroDet> kafkaReqParam = new KafkaRequest<>();
        AdmiParametroDet dataParam = new AdmiParametroDet();
        
        dataParam.setDescripcion(descripcion);
        dataParam.setEmpresaCod(codEmpresa);
        dataParam.setEstado(StatusHandler.Activo.toString());
        kafkaReqParam.setTopicName(CoreGeneralConstants.TOPIC_PARAMETRO_SYNC);
        kafkaReqParam.setOp(CoreGeneralConstants.OP_LISTA_PARAMETRO_DET_POR);
        kafkaReqParam.setData(dataParam);
        log.info("Consumiendo kafka {}", CoreGeneralConstants.TOPIC_PARAMETRO_SYNC);
        KafkaResponse<Object> reply = producerSynchro.getKafkaResponse(kafkaReqParam);
        if (reply.getStatus().equalsIgnoreCase("OK")) {
            response = Formato.mapearListObjDeserializado(reply.getData(), ParametroDetKafkaRes.class);
        } else {
            throw new GenericException(reply.getMessage());
        }
        return response;
    }
}