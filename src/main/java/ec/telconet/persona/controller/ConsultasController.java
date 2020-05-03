package ec.telconet.persona.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.telconet.microservicio.dependencia.util.response.GenericListResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioResDTO;
import ec.telconet.persona.service.ConsultasService;

/**
 * Clase utilizada para publicar microservicios técnicos con información referente a las consultas DTO
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@RestController
@RequestMapping
public class ConsultasController {
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	ConsultasService consultasService;
	
	/**
	 * Método que retorna la información adicional de un usuario
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request {@linkplain InfoUsuarioReqDTO}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "infoUsuario", consumes = "application/json")
	public GenericListResponse<InfoUsuarioResDTO> infoUsuario(@RequestBody InfoUsuarioReqDTO request) throws Exception {
		log.info("Petición recibida: infoUsuario");
		GenericListResponse<InfoUsuarioResDTO> response = new GenericListResponse<InfoUsuarioResDTO>();
		response.setData(consultasService.infoUsuario(request));
		return response;
	}
}
