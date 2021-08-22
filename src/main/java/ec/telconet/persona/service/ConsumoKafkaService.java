package ec.telconet.persona.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.telconet.microservicio.core.general.kafka.cons.CoreGeneralConstants;
import ec.telconet.microservicio.core.general.kafka.request.CorreoKafkaReq;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaRequest;
import ec.telconet.microservicio.dependencia.util.kafka.producer.asynch.ProducerAsynchroImpl;

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
}