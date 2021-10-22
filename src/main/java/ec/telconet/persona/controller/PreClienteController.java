package ec.telconet.persona.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.telconet.microservicio.dependencia.util.response.GenericBasicResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteResDTO;
import ec.telconet.persona.service.PreClienteService;
import ec.telconet.persona.utils.PersonaUtils;

/**
 * Clase utilizada para publicar recursos para crear prospecto o pre-cliente JAC3
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
	 * Método que crea pre-cliente y retorta informacion del mismo JAC3
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
	public GenericBasicResponse<CrearPreClienteResDTO> craerPreCliente(@RequestBody CrearPreClienteReqDTO request) throws Exception {
		log.info("Petición recibida: crearPreCliente");
		GenericBasicResponse<CrearPreClienteResDTO> response = new GenericBasicResponse<CrearPreClienteResDTO>();		 
		if (request.getStrClientIp() == null) {
			request.setStrClientIp(personaUtils.getClientIp());
		}
		response.setData(preClienteService.crearPreCliente(request));
		return response;
 
	}
} 