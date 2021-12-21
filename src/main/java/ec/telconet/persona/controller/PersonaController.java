package ec.telconet.persona.controller;

import java.util.Collections;
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
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.HistorialServicioPorFechaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorDepartamentoReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorEmpresaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRegionReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRolReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersona;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRol;
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
	public GenericListResponse<Object> listaPersona() throws Exception {
		log.info("Petición recibida: listaPersona");
		GenericListResponse<Object> response = new GenericListResponse<>();
		Object listaPersona = personaService.listaPersona();
		response.setData((List<Object>) listaPersona);
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
	public GenericListResponse<Object> listaPersonaPor(@RequestBody Object request) throws Exception {
		log.info("Petición recibida: listaPersonaPor");
		GenericListResponse<Object> response = new GenericListResponse<>();
		Object listaPersona = personaService.listaPersonaPor(Formato.mapearObjDeserializado(request, InfoPersona.class));
		response.setData((List<Object>) listaPersona);
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
	public GenericBasicResponse<Page<Object>> paginaListaPersonaPor(@RequestBody PageDTO<Object> request) throws Exception {
		log.info("Petición recibida: paginaListaPersonaPor");
		GenericBasicResponse<Page<Object>> response = new GenericBasicResponse<>();
		Object objListaPersona = personaService.paginaListaPersonaPor(Formato.mapearPageObjDeserializado(request, InfoPersona.class));
		response.setData((Page<Object>) objListaPersona);
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
	public GenericListResponse<Object> listaPersonaPorRegion(@RequestBody PersonaPorRegionReqDTO request) throws Exception {
		log.info("Petición recibida: listaPersonaPorRegion");
		GenericListResponse<Object> response = new GenericListResponse<>();
		Object listaPersona = personaService.listaPersonaPorRegion(Formato.mapearObjDeserializado(request, PersonaPorRegionReqDTO.class));
		response.setData((List<Object>) listaPersona);
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
	public GenericListResponse<Object> listaPersonaPorRol(@RequestBody PersonaPorRolReqDTO request) throws Exception {
		log.info("Petición recibida: listaPersonaPorRol");
		GenericListResponse<Object> response = new GenericListResponse<>();
		Object listaPersona = personaService.listaPersonaPorRol(Formato.mapearObjDeserializado(request, PersonaPorRolReqDTO.class));
		response.setData((List<Object>) listaPersona);
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
	public GenericListResponse<Object> listaPersonaPorDepartamento(@RequestBody Object request) throws Exception {
		log.info("Petición recibida: listaPersonaPorDepartamento");
		GenericListResponse<Object> response = new GenericListResponse<>();
		Object listaPersona = personaService.listaPersonaPorDepartamento(
				Formato.mapearObjDeserializado(request, PersonaPorDepartamentoReqDTO.class));
		response.setData((List<Object>) listaPersona);
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
	public GenericListResponse<Object> listaPersonaPorEmpresa(@RequestBody PersonaPorEmpresaReqDTO request) throws Exception {
		log.info("Petición recibida: listaPersonaPorEmpresa");
		GenericListResponse<Object> response = new GenericListResponse<>();
		Object listaPersona = personaService.listaPersonaPorEmpresa(Formato.mapearObjDeserializado(request, PersonaPorEmpresaReqDTO.class));
		response.setData((List<Object>) listaPersona);
		return response;
	}
}
