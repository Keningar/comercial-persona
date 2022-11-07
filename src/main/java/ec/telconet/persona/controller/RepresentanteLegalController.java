package ec.telconet.persona.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.telconet.microservicio.dependencia.util.response.GenericBasicResponse; 
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.RepresentanteLegalReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.RepresentanteLegalResDTO;
import ec.telconet.persona.service.RepresentanteLegalService; 
import ec.telconet.persona.utils.PersonaUtils;

/**
 * Clase utilizada para publicar recursos para representante legal
 * 
 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
 * @version 1.0
 * @since 80/08/2022
 */
@RestController
@RequestMapping
public class RepresentanteLegalController {
	Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	RepresentanteLegalService representanteLegalService;

	@Autowired
	PersonaUtils personaUtils;

	/**
	 * Método que verifica disponibilidad de representante legal
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 08/08/2022
	 * 
	 * @param request {@linkplain RepresentanteLegalReqDTO}
	 * @return {@linkplain GenericBasicResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "representante/verificar", consumes = "application/json")
	public GenericBasicResponse<RepresentanteLegalResDTO> verificarRepresentanteLegal(
			@RequestBody RepresentanteLegalReqDTO request) throws Exception {
		log.info("Petición recibida: verificar RepresentanteLegal");
		GenericBasicResponse<RepresentanteLegalResDTO> response = new GenericBasicResponse<RepresentanteLegalResDTO>();
		if (request.getClientIp() == null) {
			request.setClientIp(personaUtils.getClientIp());
		}

		RepresentanteLegalResDTO RepresentanteLegalResDTO = representanteLegalService
				.verificarRepresentanteLegal(request);
		response.setData(RepresentanteLegalResDTO);
		return response;

	}

	/**
	 * Método que consulta los representante legal relacionados a un cliente
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 08/08/2022
	 * 
	 * @param request {@linkplain RepresentanteLegalReqDTO}
	 * @return {@linkplain GenericBasicResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "representante/consultar", consumes = "application/json")
	public GenericBasicResponse<List<RepresentanteLegalResDTO>> consultarRepresentanteLegal(
			@RequestBody RepresentanteLegalReqDTO request) throws Exception {
		log.info("Petición recibida: consultar Representante Legal");
		GenericBasicResponse<List<RepresentanteLegalResDTO>> response = new GenericBasicResponse<List<RepresentanteLegalResDTO>>();
		if (request.getClientIp() == null) {
			request.setClientIp(personaUtils.getClientIp());
		}

		List<RepresentanteLegalResDTO> RepresentanteLegalResDTO = representanteLegalService
				.consultarRepresentanteLegal(request);
		response.setData(RepresentanteLegalResDTO);
		return response;

	}

	/**
	 * Método que actualiza los representante legal relacionados a un cliente
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 08/08/2022
	 * 
	 * @param request {@linkplain RepresentanteLegalReqDTO}
	 * @return {@linkplain GenericBasicResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "representante/actualizar", consumes = "application/json")
	public GenericBasicResponse<String> actualizarRepresentanteLegal(
			@RequestBody RepresentanteLegalReqDTO request) throws Exception {
		log.info("Petición recibida: actualizar Representante Legal");
		GenericBasicResponse<String> response = new GenericBasicResponse<String>();
		if (request.getClientIp() == null) {
			request.setClientIp(personaUtils.getClientIp());
		}

		String status = representanteLegalService.actualizarRepresentanteLegal(request);
		response.setData(status);
		return response;

	}

}