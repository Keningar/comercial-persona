package ec.telconet.persona.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.telconet.microservicio.dependencia.util.dto.PageDTO;
import ec.telconet.microservicio.dependencia.util.general.Formato;
import ec.telconet.microservicio.dependencia.util.response.GenericBasicResponse;
import ec.telconet.microservicio.dependencia.util.response.GenericListResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRol;
import ec.telconet.persona.service.PersonaEmpresaRolService;

/**
 * Clase utilizada para publicar microservicios técnicos con información referente a la información de persona empresa
 * rol
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 21/08/2020
 */
@RestController
@RequestMapping
public class PersonaEmpresaRolController {
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	PersonaEmpresaRolService personaEmpresaRolService;
	
	/**
	 * Método que retorna la lista de datos de la persona empresa rol
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 21/08/2020
	 * 
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@GetMapping("listaPersonaEmpresaRol")
	public GenericListResponse<Object> listaPersonaEmpresaRol() throws Exception {
		log.info("Petición recibida: listaPersonaEmpresaRol");
		GenericListResponse<Object> response = new GenericListResponse<>();
		Object listaPersona = personaEmpresaRolService.listaPersonaEmpresaRol();
		response.setData((List<Object>) listaPersona);
		return response;
	}
	
	/**
	 * Método que retorna la lista de datos de la persona empresa rol con filtros
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 21/08/2020
	 * 
	 * @param request {@linkplain InfoPersonaEmpresaRol}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "listaPersonaEmpresaRolPor", consumes = "application/json")
	public GenericListResponse<Object> listaPersonaEmpresaRolPor(@RequestBody Object request) throws Exception {
		log.info("Petición recibida: listaPersonaEmpresaRolPor");
		GenericListResponse<Object> response = new GenericListResponse<>();
		Object listaPersona = personaEmpresaRolService.listaPersonaEmpresaRolPor(Formato.mapearObjDeserializado(request, InfoPersonaEmpresaRol.class));
		response.setData((List<Object>) listaPersona);
		return response;
	}
	
	/**
	 * Método que retorna la paginación de una lista de datos de la persona empresa rol con filtros
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 21/08/2020
	 * 
	 * @param request {@linkplain PageDTO}
	 * @return {@linkplain GenericBasicResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "paginaListaPersonaEmpresaRolPor", consumes = "application/json")
	public GenericBasicResponse<Page<Object>> paginaListaPersonaEmpresaRolPor(@RequestBody PageDTO<Object> request)
			throws Exception {
		log.info("Petición recibida: paginaListaPersonaEmpresaRolPor");
		GenericBasicResponse<Page<Object>> response = new GenericBasicResponse<>();
	    Object objListaPersona = personaEmpresaRolService.paginaListaPersonaEmpresaRolPor(Formato.mapearPageObjDeserializado(request, InfoPersonaEmpresaRol.class));
	    response.setData((Page<Object>) objListaPersona);
	    return response;
	}
}
