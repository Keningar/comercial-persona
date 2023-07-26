package ec.telconet.persona.kafka;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import ec.telconet.microservicio.core.comercial.kafka.cons.CoreComercialConstants;
import ec.telconet.microservicio.core.comercial.kafka.request.AdmiCaracteristicaKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.HistorialServicioKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.InfoClienteNotMasivaDetKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.InfoUsuarioKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.PersonaEmpresaRolCaracKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.PersonaEmpresaRolKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.PersonaKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.PersonaProspectoKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.PersonaPuntoServiciosKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.request.ValidarFormaContactoKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.response.InfoClienteNotMasivaDetKafkaRes;
import ec.telconet.microservicio.core.comercial.kafka.response.PersonaEmpresaRolKafkaRes;
import ec.telconet.microservicio.core.comercial.kafka.response.PersonaKafkaRes;
import ec.telconet.microservicio.core.comercial.kafka.response.PersonaProspectoKafkaRes;
import ec.telconet.microservicio.core.comercial.kafka.response.PuntoServicioKafkaRes;
import ec.telconet.microservicio.dependencia.util.exception.GenericException;
import ec.telconet.microservicio.dependencia.util.general.Formato;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaProperties;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaRequest;
import ec.telconet.microservicio.dependencia.util.kafka.KafkaResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.AgregarPersonaListaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.AgregarPersonaListaResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.BusquedaPersonaListaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.BusquedaPersonaListaResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.HistorialServicioPorFechaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoPersonaEmpFormaPagoDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoClienteNotMasivaDetReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoClienteNotMasivaDetResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PerfilPersonaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaEmpresaRolPorEmpresaActivoReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorCaractReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorDepartamentoReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorEmpresaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRegionReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRolReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaProspectoReqDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaResponsableReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaResponsableResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.SeguPerfilPersonaDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.AdmiCaracteristica;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersona;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRol;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRolCarac;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoServicioHistorial;
import ec.telconet.persona.dto.PersonaProspectoRespDto;
import ec.telconet.persona.service.ConsultasService;
import ec.telconet.persona.service.PersonaEmpresaRolCaracService;
import ec.telconet.persona.service.PersonaEmpresaRolService;
import ec.telconet.persona.service.PersonaProspectoService;
import ec.telconet.persona.service.PersonaService;
import ec.telconet.persona.service.PreClienteService;
import ec.telconet.persona.service.PersonaServiciosService;
import ec.telconet.persona.service.ServicioHistorialService;

/**
 * Clase utilizada para consumir OP sincrónico o asincrónico en kafka
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 *
 * @author Wilson Quinto <mailto:wquinto@telconet.ec>
 * @version 1.2
 * @since 12/12/2021 - Implementacion en servicio de kafka de consulta de persona por carateristicas
 *
 */
@Component
public class ServiceConsumer {
	Logger log = LogManager.getLogger(this.getClass());

	@Value("${kafka.request-reply.timeout-ms:300s}")
	private String replyTimeout;
	
	@Autowired
	PersonaService personaService;
	
	@Autowired
	ServicioHistorialService servicioHistorialService;
	
	@Autowired
	ConsultasService consultasService;
	
	@Autowired
	PersonaEmpresaRolService personaEmpresaRolService;

	@Autowired
	PersonaProspectoService personaProspectoService;

	@Autowired
	PreClienteService preClienteService;

	@Autowired
	PersonaEmpresaRolCaracService personaEmpresaRolCaracService;

	@Autowired
    PersonaServiciosService personaServicioService;
	
	@Autowired
	KafkaProperties kafkaProperties;

	@Autowired
	public ServiceConsumer(KafkaProperties kafkaProperties) {
		kafkaProperties.setTopicGroup(CoreComercialConstants.GROUP_PERSONA);
		log.info("Grupo kafka configurado: {}", kafkaProperties.getTopicGroup());
		Collection<String> colKafkaTopicSync = Collections.singletonList(CoreComercialConstants.TOPIC_PERSONA_SYNC);
		Collection<String> topicsSync = new ArrayList<>();
		if (kafkaProperties.getTopicSyncSufijo() != null) {
			colKafkaTopicSync.stream().distinct().forEach(v -> topicsSync.add(v.concat(kafkaProperties.getTopicSyncSufijo())));
			kafkaProperties.setTopicSync(topicsSync);
		} else {
			kafkaProperties.setTopicSync(colKafkaTopicSync);
		}
		log.info("Topic kafka sync configurado: {}", kafkaProperties.getTopicSync()::toString);

		Collection<String> colKafkaTopicAync = Collections.singletonList(CoreComercialConstants.TOPIC_PERSONA_ASYN);
		Collection<String> topicsAsyn = new ArrayList<>();
		if (kafkaProperties.getTopicAsynSufijo() != null) {
			colKafkaTopicAync.stream().distinct().forEach(v -> topicsAsyn.add(v.concat(kafkaProperties.getTopicAsynSufijo())));
			kafkaProperties.setTopicAsyn(topicsAsyn);
		} else {
			kafkaProperties.setTopicAsyn(colKafkaTopicAync);
		}
		log.info("Topic kafka asyn configurado: {}", kafkaProperties.getTopicAsyn()::toString);
		this.kafkaProperties = kafkaProperties;
	}

	/**
	 * Listener asincrónico kafka
	 *
	 * @param kafkaRequest Request Kafka
	 *
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @since 02/03/2020
	 */
	@KafkaListener(topics = "#{kafkaProperties.getTopicAsyn()}", groupId = "#{kafkaProperties.getTopicGroup()}", containerFactory = "kafkaListenerContainerFactory")
	public void serviceAsynchroListener(KafkaRequest<?> kafkaRequest) {
		String idTransKafka = UUID.randomUUID().toString();
		log.info("Petición kafka asincrónico recibida: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
		// EJECUCIONES ASINCRONICAS
		try {
			log.info("Petición kafka asincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
			throw new GenericException("No existe OP configurados, borre esta linea cuando se genere uno");
		} catch (Exception e) {
			log.error(e);
			log.info("Petición kafka asincrónico enviada: {}, Transacción: {}, Estado: Fallida", kafkaRequest.getOp(), idTransKafka);
		}
	}

	/**
	 * Listener sincrónico kafka
	 *
	 * @param <T>          Objeto de respuesta
	 * @param kafkaRequest Request Kafka
	 *
	 * @return KafkaResponse
	 *
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @since 02/03/2020
	 */
	@SuppressWarnings("unchecked")
	@KafkaListener(topics = "#{kafkaProperties.getTopicSync()}", groupId = "#{kafkaProperties.getTopicGroup()}", containerFactory = "requestReplyListenerContainerFactory")
	@SendTo()
	public <T> KafkaResponse<T> serviceSynchroListener(KafkaRequest<?> kafkaRequest, @Header(value = "dateSendMessage", defaultValue = "") String dateSendMessage,
													   Acknowledgment commitKafka) {
		String idTransKafka = UUID.randomUUID().toString();
		log.info("Petición kafka sincrónico recibida: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
		KafkaResponse<String> kafkaResponse = new KafkaResponse<>();
		try {
			validateConsumerMessage(dateSendMessage);
			if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA)) {
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersona());
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_RERSONA_POR)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				InfoPersona requestService = Formato.mapearObjDeserializado(data, InfoPersona.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersonaPor(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_POR_REGION)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaPorRegionReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaPorRegionReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersonaPorRegion(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_POR_ROL)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaPorRolReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaPorRolReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<>();
				response.setData(personaService.listaPersonaPorRol(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {} Transacción: {}",kafkaRequest::getOp, idTransKafka::toString);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_RESPONSABLE)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaResponsableReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaResponsableReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<PersonaResponsableResDTO> response = new KafkaResponse<>();
				response.setData(personaService.listaPersonaResponsable(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {} Transacción: {}",kafkaRequest::getOp, idTransKafka::toString);
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_POR_CARACT)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaPorCaractReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaPorCaractReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<>();
				response.setData(personaService.listaPersonaPorCaract(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_POR_DEPARTAMENTO)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaPorDepartamentoReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaPorDepartamentoReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<PersonaKafkaRes> response = new KafkaResponse<>();
				List<InfoPersona> listaPersonas = personaService.listaPersonaPorDepartamento(requestService);
				response.setData(Formato.mapearListObjDeserializado(listaPersonas, PersonaKafkaRes.class));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_POR_EMPRESA)) {
				PersonaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaKafkaReq.class);
				// Inicio Proceso logico
				PersonaPorEmpresaReqDTO requestService = Formato.mapearObjDeserializado(data, PersonaPorEmpresaReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoPersona> response = new KafkaResponse<InfoPersona>();
				response.setData(personaService.listaPersonaPorEmpresa(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_HISTORIAL_SERVICIO_POR_FECHA)) {
				HistorialServicioKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), HistorialServicioKafkaReq.class);
				// Inicio Proceso logico
				HistorialServicioPorFechaReqDTO requestService = Formato.mapearObjDeserializado(data, HistorialServicioPorFechaReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoServicioHistorial> response = new KafkaResponse<InfoServicioHistorial>();
				response.setData(servicioHistorialService.listaHistorialServicioPorFecha(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_INFO_USUARIO)) {
				InfoUsuarioKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), InfoUsuarioKafkaReq.class);
				// Inicio Proceso logico
				InfoUsuarioReqDTO requestService = Formato.mapearObjDeserializado(data, InfoUsuarioReqDTO.class);
				// Fin Proceso logico
				KafkaResponse<InfoUsuarioResDTO> response = new KafkaResponse<InfoUsuarioResDTO>();
				response.setData(consultasService.infoUsuario(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_EMPRESA_ROL)) {
				KafkaResponse<InfoPersonaEmpresaRol> response = new KafkaResponse<>();
				response.setData(personaEmpresaRolService.listaPersonaEmpresaRol());
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_EMPRESA_ROL_POR)) {
				PersonaEmpresaRolKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaEmpresaRolKafkaReq.class);
				// Inicio Proceso logico
				InfoPersonaEmpresaRol requestService = Formato.mapearObjDeserializado(data, InfoPersonaEmpresaRol.class);
				// Fin Proceso logico				
				KafkaResponse<PersonaEmpresaRolKafkaRes> response = new KafkaResponse<>();
				List<InfoPersonaEmpresaRol> listaPersonasEmpresaRol = personaEmpresaRolService.listaPersonaEmpresaRolPor(requestService);
				response.setData(Formato.mapearListObjDeserializado(listaPersonasEmpresaRol, PersonaEmpresaRolKafkaRes.class));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			
			}else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_CARACTERISTICA_POR)){
				AdmiCaracteristicaKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), AdmiCaracteristicaKafkaReq.class);
				AdmiCaracteristica requestService = Formato.mapearObjDeserializado(data, AdmiCaracteristica.class);
				KafkaResponse<AdmiCaracteristicaKafkaReq> response = new KafkaResponse<>();
				List<AdmiCaracteristica> admiCaracteristica = consultasService.listarCaracteristicaPor(requestService);
				response.setData(Formato.mapearListObjDeserializado(admiCaracteristica,AdmiCaracteristicaKafkaReq.class));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			}else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_CLIENTE_NOT_MASIVA_DET)){
				InfoClienteNotMasivaDetKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), InfoClienteNotMasivaDetKafkaReq.class);
				InfoClienteNotMasivaDetReqDTO requestService = Formato.mapearObjDeserializado(data, InfoClienteNotMasivaDetReqDTO.class);
				KafkaResponse<InfoClienteNotMasivaDetKafkaRes> response = new KafkaResponse<>();
				List<InfoClienteNotMasivaDetResDTO> infoClienteNotMasivaDetRes = consultasService.listarClienteNotMasivaDet(requestService);
				response.setData(Formato.mapearListObjDeserializado(infoClienteNotMasivaDetRes,InfoClienteNotMasivaDetKafkaRes.class));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_FORMA_PAGO_PROSPECTO)) {
				PersonaEmpresaRolPorEmpresaActivoReqDTO  requestService = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaEmpresaRolPorEmpresaActivoReqDTO .class);
				KafkaResponse<InfoPersonaEmpFormaPagoDTO> response = new KafkaResponse<>();
				response.setData(personaProspectoService.formaPagoProspecto(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_VALIDAR_PERFIL_PESONA)) {
				PerfilPersonaReqDTO  requestService = Formato.mapearObjDeserializado(kafkaRequest.getData(), PerfilPersonaReqDTO .class);
				KafkaResponse<SeguPerfilPersonaDTO> response = new KafkaResponse<>();
				response.setData(personaProspectoService.validarPerfilPersona(requestService));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_GUARDA_PERSONA_EMPRESA_ROL_CARAC)) {
				PersonaEmpresaRolCaracKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaEmpresaRolCaracKafkaReq.class);
				InfoPersonaEmpresaRolCarac requestService = Formato.mapearObjDeserializado(data, InfoPersonaEmpresaRolCarac.class);
				KafkaResponse<PersonaEmpresaRolCaracKafkaReq> response = new KafkaResponse<>();
				InfoPersonaEmpresaRolCarac personaEmpresaRolCarac = personaEmpresaRolCaracService.guardar(requestService);
				response.setData(Collections.singletonList(Formato.mapearObjDeserializado(personaEmpresaRolCarac, PersonaEmpresaRolCaracKafkaReq.class)));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_ACTUALIZA_PERSONA_EMPRESA_ROL_CARAC)) {
				PersonaEmpresaRolCaracKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaEmpresaRolCaracKafkaReq.class);
				InfoPersonaEmpresaRolCarac requestService = Formato.mapearObjDeserializado(data, InfoPersonaEmpresaRolCarac.class);
				KafkaResponse<PersonaEmpresaRolCaracKafkaReq> response = new KafkaResponse<>();
				InfoPersonaEmpresaRolCarac personaEmpresaRolCarac = personaEmpresaRolCaracService.actualizar(requestService);
				response.setData(Collections.singletonList(Formato.mapearObjDeserializado(personaEmpresaRolCarac, PersonaEmpresaRolCaracKafkaReq.class)));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_EMPRESA_ROL_CARAC_POR)) {
				PersonaEmpresaRolCaracKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaEmpresaRolCaracKafkaReq.class);
				InfoPersonaEmpresaRolCarac requestService = Formato.mapearObjDeserializado(data, InfoPersonaEmpresaRolCarac.class);
				KafkaResponse<PersonaEmpresaRolCaracKafkaReq> response = new KafkaResponse<>();
				List<InfoPersonaEmpresaRolCarac> personaEmpresaRolCarac = personaEmpresaRolCaracService.listaPor(requestService);
				response.setData(Formato.mapearListObjDeserializado(personaEmpresaRolCarac, PersonaEmpresaRolCaracKafkaReq.class));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;				
			} else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_BLANCA_NEGRA)) {
                AgregarPersonaListaReqDTO  requestService = Formato.mapearObjDeserializado(kafkaRequest.getData(), AgregarPersonaListaReqDTO .class);
                KafkaResponse<AgregarPersonaListaResDTO> response = new KafkaResponse<>();
                AgregarPersonaListaResDTO agregarPersonaListaResDTO = consultasService.agregarPersonaLista(requestService);
                response.setData(Collections.singletonList(agregarPersonaListaResDTO));
                commitKafka.acknowledge();
                log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
                return (KafkaResponse<T>) response;
            } else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_BUSCAR)) {
                BusquedaPersonaListaReqDTO  requestService = Formato.mapearObjDeserializado(kafkaRequest.getData(), BusquedaPersonaListaReqDTO .class);
                KafkaResponse<BusquedaPersonaListaResDTO> response = new KafkaResponse<BusquedaPersonaListaResDTO>();
                response.setData(consultasService.buscarPersonaLista(requestService));
                commitKafka.acknowledge();
                log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
                return (KafkaResponse<T>) response;
            } else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_VALIDAR_FORMA_CONTACTO)) {
                   	
            	ValidarFormaContactoKafkaReq requestService = Formato.mapearObjDeserializado(kafkaRequest.getData(), ValidarFormaContactoKafkaReq.class);  
                     	
                KafkaResponse<String> response = new KafkaResponse<String>();
                response.setData(preClienteService.validarFormaContacto(requestService));
                commitKafka.acknowledge();
                log.info("Petición kafka sincrónico enviada: " + kafkaRequest.getOp() + ", Transacción: " + idTransKafka);
                return (KafkaResponse<T>) response;
            } 
			else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_PROSPECTO)) 
			{					
				PersonaProspectoKafkaReq data = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaProspectoKafkaReq.class);
				PersonaProspectoReqDto requestService = Formato.mapearObjDeserializado(data, PersonaProspectoReqDto.class);
				PersonaProspectoRespDto responseData=personaProspectoService.personaProspecto(requestService);			
				KafkaResponse<PersonaProspectoKafkaRes> response = new KafkaResponse<>();
				response.setData(Collections.singletonList(Formato.mapearObjDeserializado(responseData, PersonaProspectoKafkaRes.class)));
				commitKafka.acknowledge();
				log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
				return (KafkaResponse<T>) response;							
			}
			
			 else if (kafkaRequest.getOp().equalsIgnoreCase(CoreComercialConstants.OP_LISTA_PERSONA_SERVICIOS)) 
			{					
				    PersonaPuntoServiciosKafkaReq  requestService = Formato.mapearObjDeserializado(kafkaRequest.getData(), PersonaPuntoServiciosKafkaReq .class);															
					KafkaResponse<PuntoServicioKafkaRes> response = new KafkaResponse<>();
					response.setData(personaServicioService.puntoServicios(requestService));
					commitKafka.acknowledge();
					log.info("Petición kafka sincrónico enviada: {}, Transacción: {}", kafkaRequest.getOp(), idTransKafka);
					return (KafkaResponse<T>) response;							
			}	
			else {
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
		log.info("Petición kafka sincrónico enviada: {}, Transacción: {}, Estado: Fallida", kafkaRequest.getOp(), idTransKafka);
		return (KafkaResponse<T>) kafkaResponse;
	}

	private void validateConsumerMessage(String dateSendMessage) throws GenericException {
		if (!dateSendMessage.equals("")) {
			Date dateMessageReceived = Formato.getDateByString(dateSendMessage, "yyyy-MM-dd HH:mm:ss");
			Date dateMessageProcess = new Date();
			long segReplyTimeout = Long.parseLong(replyTimeout.replace("s", "").trim());
			long segDiffDateMessage = TimeUnit.MILLISECONDS.toSeconds(dateMessageProcess.getTime() - dateMessageReceived.getTime());
			if (segDiffDateMessage > segReplyTimeout) {
				String msg = "El mensaje sé ha ignorado porque ha sobrepasado el tiempo de lectura (" + segDiffDateMessage + " segundos) del consumidor sincrónico " + "(" + segReplyTimeout + " segundos)";
				log.error(msg);
				throw new GenericException(msg);
			}
		}
	}
}
