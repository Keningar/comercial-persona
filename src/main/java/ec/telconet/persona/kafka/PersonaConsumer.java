package ec.telconet.persona.kafka;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import ec.telconet.microservicio.core.comercial.kafka.cons.CoreComercialConstants;
import ec.telconet.microservicio.core.comercial.kafka.request.HistorialServicioKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.InfoUsuarioKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.PersonaKafkaReq;
import ec.telconet.microservicio.dependencia.util.exception.GenericException;
import ec.telconet.microservicio.dependencia.util.general.Formato;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaRequest;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.HistorialServicioPorFechaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorDepartamentoReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorEmpresaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRegionReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRolReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersona;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoServicioHistorial;
import ec.telconet.persona.service.ConsultasService;
import ec.telconet.persona.service.PersonaService;
import ec.telconet.persona.service.ServicioHistorialService;

/**
 * Clase utilizada para consumir OP sincrónico o asincrónico en kafka
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@Component
public class PersonaConsumer {
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	PersonaService personaService;
	
	@Autowired
	ServicioHistorialService servicioHistorialService;
	
	@Autowired
	ConsultasService consultasService;
	
	/**
	 * Listener asincrónico kafka
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param kafkaRequest
	 * @throws GenericException
	 */
	@KafkaListener(topics = CoreComercialConstants.TOPIC_PERSONA_ASYN, groupId = CoreComercialConstants.GROUP_PERSONA, containerFactory = "kafkaListenerContainerFactory")
	public void personaAsynchrotListener(KafkaRequest<?> kafkaRequest) throws GenericException {
		log.info("Petición kafka asincrónico recibida: " + kafkaRequest.getOp());
		// EJECUCIONES ASINCRONICAS
	}
	
	/**
	 * Listener sincrónico kafka
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param <T>          Objeto de respuesta
	 * @param kafkaRequest
	 * @return KafkaResponse
	 * @throws GenericException
	 */
	@SuppressWarnings("unchecked")
	@KafkaListener(topics = CoreComercialConstants.TOPIC_PERSONA_SYNC, groupId = CoreComercialConstants.GROUP_PERSONA, containerFactory = "requestReplyListenerContainerFactory")
	@SendTo()
	public <T> KafkaResponse<T> personaSynChroListener(KafkaRequest<?> kafkaRequest, Acknowledgment commitKafka) throws GenericException {
		String idTransKafka = UUID.randomUUID().toString();
		log.info("Petición kafka sincrónico recibida: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
		KafkaResponse<String> kafkaResponse = new KafkaResponse<String>();
		try {
			if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA)) {
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersona());
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_RERSONA_POR)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				InfoPersona requestService = Formato.mapearObjDeserializado(data, InfoPersona.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersonaPor(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_POR_REGION)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaPorRegionReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaPorRegionReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersonaPorRegion(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_POR_ROL)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaPorRolReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaPorRolReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersonaPorRol(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_POR_DEPARTAMENTO)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaPorDepartamentoReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaPorDepartamentoReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersonaPorDepartamento(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_POR_EMPRESA)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaPorEmpresaReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaPorEmpresaReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersonaPorEmpresa(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_HISTORIAL_SERVICIO_POR_FECHA)) {
				HistorialServicioKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), HistorialServicioKafkaReq.class);
				// Inicio Proceso logico
				HistorialServicioPorFechaReqDTO requestService = Formato.mapearObjDeserializado(data, HistorialServicioPorFechaReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoServicioHistorial> response = new KafkaResponse<InfoServicioHistorial>();
				response.setData(servicioHistorialService.listaHistorialServicioPorFecha(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_INFO_USUARIO)) {
				InfoUsuarioKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), InfoUsuarioKafkaReq.class);
				// Inicio Proceso logico
				InfoUsuarioReqDTO requestService = Formato.mapearObjDeserializado(data, InfoUsuarioReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoUsuarioResDTO> response = new KafkaResponse<InfoUsuarioResDTO>();
				response.setData(consultasService.infoUsuario(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else {
				kafkaResponse.setCode(500);
				kafkaResponse.setStatus("ERROR");
				kafkaResponse.setMessage(
						"No se encuentra configurado el OP " + kafkaRequest.getOp() + " en el grupo " + CoreComercialConstants.GROUP_PERSONA);
			}
		} catch (GenericException e) {
			kafkaResponse.setCode(e.getCodeError());
			kafkaResponse.setStatus(e.getStatusError());
			kafkaResponse.setMessage(e.getMessageError());
		} catch (Exception e) {
			kafkaResponse.setCode(100);
			kafkaResponse.setStatus("ERROR");
			kafkaResponse.setMessage(e.getMessage());
		}
		commitKafka.acknowledge();
		log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka + ", Estado: Fallida");
		return (KafkaResponse<T>) kafkaResponse;
	}
}
