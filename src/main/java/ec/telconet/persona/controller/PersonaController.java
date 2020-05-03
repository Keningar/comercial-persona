package ec.telconet.persona.controller;

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
import ec.telconet.microservicio.dependencia.util.response.GenericBasicResponse;
import ec.telconet.microservicio.dependencia.util.response.GenericListResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorDepartamentoReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorEmpresaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRegionReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRolReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersona;
import ec.telconet.persona.service.PersonaService;

/**
 * Clase utilizada para publicar microservicios técnicos con información referente a las personas
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@RestController
@RequestMapping
public class PersonaController {
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	PersonaService personaService;
	
	/**
	 * Método que retorna la lista de personas
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@GetMapping("listaPersona")
	public GenericListResponse<InfoPersona> listaPersona() throws Exception {
		log.info("Petición recibida: listaPersona");
		GenericListResponse<InfoPersona> response = new GenericListResponse<InfoPersona>();
		response.setData(personaService.listaPersona());
		return response;
	}
	
	/**
	 * Método que retorna la lista de personas con filtros
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request {@linkplain InfoPersona}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "listaPersonaPor", consumes = "application/json")
	public GenericListResponse<InfoPersona> listaPersonaPor(@RequestBody InfoPersona request) throws Exception {
		log.info("Petición recibida: listaPersonaPor");
		GenericListResponse<InfoPersona> response = new GenericListResponse<InfoPersona>();
		response.setData(personaService.listaPersonaPor(request));
		return response;
	}
	
	/**
	 * Método que retorna la paginación de una lista de personas con filtros
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request {@linkplain PageDTO}
	 * @return {@linkplain GenericBasicResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "paginaListaPersonaPor", consumes = "application/json")
	public GenericBasicResponse<Page<InfoPersona>> paginaListaPersonaPor(@RequestBody PageDTO<InfoPersona> request) throws Exception {
		log.info("Petición recibida: paginaListaPersonaPor");
		GenericBasicResponse<Page<InfoPersona>> response = new GenericBasicResponse<Page<InfoPersona>>();
		response.setData(personaService.paginaListaPersonaPor(request));
		return response;
	}
	
	/**
	 * Método que retorna la lista de personas por region
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request {@linkplain PersonaPorRegionReqDTO}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "listaPersonaPorRegion", consumes = "application/json")
	public GenericListResponse<InfoPersona> listaPersonaPorRegion(@RequestBody PersonaPorRegionReqDTO request) throws Exception {
		log.info("Petición recibida: listaPersonaPorRegion");
		GenericListResponse<InfoPersona> response = new GenericListResponse<InfoPersona>();
		response.setData(personaService.listaPersonaPorRegion(request));
		return response;
	}
	
	/**
	 * Método que retorna la lista de personas por rol
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request {@linkplain PersonaPorRolReqDTO}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "listaPersonaPorRol", consumes = "application/json")
	public GenericListResponse<InfoPersona> listaPersonaPorRol(@RequestBody PersonaPorRolReqDTO request) throws Exception {
		log.info("Petición recibida: listaPersonaPorRol");
		GenericListResponse<InfoPersona> response = new GenericListResponse<InfoPersona>();
		response.setData(personaService.listaPersonaPorRol(request));
		return response;
	}
	
	/**
	 * Método que retorna la lista de personas por departamento
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request {@linkplain PersonaPorDepartamentoReqDTO}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "listaPersonaPorDepartamento", consumes = "application/json")
	public GenericListResponse<InfoPersona> listaPersonaPorDepartamento(@RequestBody PersonaPorDepartamentoReqDTO request) throws Exception {
		log.info("Petición recibida: listaPersonaPorDepartamento");
		GenericListResponse<InfoPersona> response = new GenericListResponse<InfoPersona>();
		response.setData(personaService.listaPersonaPorDepartamento(request));
		return response;
	}
	
	/**
	 * Método que retorna la lista de personas por empresa (Cod o Prefijo)
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request {@linkplain PersonaPorEmpresaReqDTO}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "listaPersonaPorEmpresa", consumes = "application/json")
	public GenericListResponse<InfoPersona> listaPersonaPorEmpresa(@RequestBody PersonaPorEmpresaReqDTO request) throws Exception {
		log.info("Petición recibida: listaPersonaPorEmpresa");
		GenericListResponse<InfoPersona> response = new GenericListResponse<InfoPersona>();
		response.setData(personaService.listaPersonaPorEmpresa(request));
		return response;
	}
}
