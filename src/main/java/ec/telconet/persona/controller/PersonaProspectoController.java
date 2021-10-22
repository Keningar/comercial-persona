package ec.telconet.persona.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import ec.telconet.microservicio.dependencia.util.response.GenericBasicResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.EquifaxReqDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaEquifaxRecomendacionResDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaProspectoReqDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaProspectoRespDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.TarjetasEquifaxRecomendacionResDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRol;
import ec.telconet.persona.service.PersonaProspectoService;
import ec.telconet.persona.utils.PersonaUtils;


/**
 * Clase utilizada para publicar microservicios  información referente a la información de persona 
 * con el prospecto
 * 
 * @author Carlos Caguana <mailto:ccaguana@telconet.ec>
 * @version 1.0
 * @since 11/08/2021
 */
@RestController
@RequestMapping
public class PersonaProspectoController {
	
    Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	PersonaProspectoService personaProspectoService;
	
	@Autowired
	PersonaUtils personaUtils;

	
	
	/**
	 * Método que retorna la información de la persona relaciona al prospecto
	 * 
	 * @author Carlos Caguana <mailto:ccaguana@telconet.ec>
	 * @version 1.0
	 * @since 21/08/2020
	 * 
	 * @param request {@linkplain InfoPersonaEmpresaRol}
	 * @return {@linkplain GenericBasicResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "personaProspecto", consumes = "application/json")
	public GenericBasicResponse<PersonaProspectoRespDto> personaProspecto(@RequestBody PersonaProspectoReqDto request) throws Exception {
		log.info("Petición recibida: personaProspecto");
		GenericBasicResponse<PersonaProspectoRespDto> response = new GenericBasicResponse<PersonaProspectoRespDto>();
	    response.setData(personaProspectoService.personaProspecto(request));	
		return response;
	}
	
	
	/**
	 * Método que retorna la recomendación de la persona
	 * 
	 * @author Carlos Caguana <mailto:ccaguana@telconet.ec>
	 * @version 1.0
	 * @since 21/08/2020
	 * 
	 * @param request {@linkplain InfoPersonaEmpresaRol}
	 * @return {@linkplain GenericBasicResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "personaRecomendacion", consumes = "application/json")
	public GenericBasicResponse<PersonaEquifaxRecomendacionResDto> personaRecomendacion(@RequestBody EquifaxReqDto request) throws Exception {
		log.info("Petición recibida: personaRecomendacion");
		GenericBasicResponse<PersonaEquifaxRecomendacionResDto> response = new GenericBasicResponse<PersonaEquifaxRecomendacionResDto>();
		if (request.getIpCreacion() == null) {
			request.setIpCreacion(personaUtils.getClientIp());
		}
		response.setData(personaProspectoService.personaRecomendacion(request));
		return response;
	}
	
	/**
	 * Método que retorna la información de la persona relaciona al prospecto
	 * 
	 * @author Carlos Caguana <mailto:ccaguana@telconet.ec>
	 * @version 1.0
	 * @since 21/08/2020
	 * 
	 * @param request {@linkplain InfoPersonaEmpresaRol}
	 * @return {@linkplain GenericBasicResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "tarjetaRecomendacion", consumes = "application/json")
	public GenericBasicResponse<TarjetasEquifaxRecomendacionResDto> tarjetaRecomendacion(@RequestBody EquifaxReqDto request) throws Exception {
		log.info("Petición recibida: tarjetaRecomendacion");
		GenericBasicResponse<TarjetasEquifaxRecomendacionResDto> response = new GenericBasicResponse<TarjetasEquifaxRecomendacionResDto>();
		if (request.getIpCreacion() == null) {
			request.setIpCreacion(personaUtils.getClientIp());
		}
		response.setData(personaProspectoService.tarjetaRecomendacion(request));
		return response;
	}
	
	
	
	
	
		

}
