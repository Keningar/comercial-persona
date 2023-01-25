package ec.telconet.persona.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.telconet.microservicio.core.comercial.kafka.request.ValidarFormaContactoKafkaReq;
import ec.telconet.microservicio.dependencia.util.general.Formato;
import ec.telconet.microservicio.dependencia.util.response.GenericBasicResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.FormaContactoReqDTO;
import ec.telconet.persona.service.PreClienteService;
import ec.telconet.persona.utils.PersonaConstants;
import ec.telconet.persona.utils.PersonaUtils;

/**
 * Clase utilizada para publicar recursos para crear prospecto o pre-cliente
 * 
 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
 * @version 1.0
 * @since 20/08/2021
 */
@RestController
@RequestMapping
public class PreClienteController {
	Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	PreClienteService preClienteService;

	@Autowired
	PersonaUtils personaUtils;

	/**
	 * Método que crea pre-cliente y retorta informacion del mismo
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 20/08/2021
	 * 
	 * @param request {@linkplain CrearPreClienteReqDTO}
	 * @return {@linkplain GenericBasicResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "precliente/crear", consumes = "application/json")
	public GenericBasicResponse<CrearPreClienteResDTO> craerPreCliente(@RequestBody CrearPreClienteReqDTO request)
			throws Exception {
		log.info("Petición recibida: crearPreCliente");
		GenericBasicResponse<CrearPreClienteResDTO> response = new GenericBasicResponse<CrearPreClienteResDTO>();
		if (request.getClientIp() == null) {
			request.setClientIp(personaUtils.getClientIp());
		}

		CrearPreClienteResDTO crearPreClienteResDTO = new CrearPreClienteResDTO();
		List<ValidarFormaContactoKafkaReq.FormaContactoKafkaReq> contactos = Formato.mapearListObjDeserializado(
				request.getFormaContacto(), ValidarFormaContactoKafkaReq.FormaContactoKafkaReq.class);
		ValidarFormaContactoKafkaReq requestContacto = new ValidarFormaContactoKafkaReq();
		requestContacto.setFormasContacto(contactos);
		requestContacto.setIdPais((long) request.getIdPais());

		List<String> validacionesContacto = preClienteService.validarFormaContacto(requestContacto);

		if (validacionesContacto.size() != 0) {
			String message = "";
			for (String text : validacionesContacto) {
				message = message + text + " \n ";
			}
			message = message.substring(0, message.length() - 1);
			response.setCode(1);
			response.setStatus(PersonaConstants.STATUS_ERROR);
			response.setMessage(PersonaConstants.MSG_VALIDACION_CONTACTO + message);
		} else {
			crearPreClienteResDTO = preClienteService.crearPreCliente(request);
		}
		crearPreClienteResDTO.setValidacionesContacto(validacionesContacto);
		response.setData(crearPreClienteResDTO);
		return response;
	}

	@PostMapping(path = "validarFormaContacto", consumes = "application/json")
	public GenericBasicResponse<Object> validarFormaContacto(@RequestBody CrearPreClienteReqDTO request)
			throws Exception {
		GenericBasicResponse<Object> response = new GenericBasicResponse<Object>();
		List<ValidarFormaContactoKafkaReq.FormaContactoKafkaReq> contactos = Formato.mapearListObjDeserializado(
				request.getFormaContacto(), ValidarFormaContactoKafkaReq.FormaContactoKafkaReq.class);
		ValidarFormaContactoKafkaReq requestContacto = new ValidarFormaContactoKafkaReq();
		requestContacto.setFormasContacto(contactos);
		requestContacto.setIdPais((long) request.getIdPais());
		List<String> listaValidaciones = preClienteService.validarFormaContacto(requestContacto);
		if (listaValidaciones.size() != 0) {
			String message = "";
			for (String text : listaValidaciones) {
				message = message + text + " \n ";
			}
			message = message.substring(0, message.length() - 1);
			response.setCode(1);
			response.setStatus(PersonaConstants.STATUS_ERROR);
			response.setMessage(message);
		}

		return response;
	}

}