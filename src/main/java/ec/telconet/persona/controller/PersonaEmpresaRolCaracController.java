package ec.telconet.persona.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.telconet.microservicio.dependencia.util.general.Formato;
import ec.telconet.microservicio.dependencia.util.response.GenericBasicResponse;
import ec.telconet.microservicio.dependencia.util.response.GenericListResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRolCarac;
import ec.telconet.persona.service.PersonaEmpresaRolCaracService;

/**
 * Clase utilizada para publicar microservicios para Persona Empresa Rol Caracteristica
 * rol
 * 
 * @author David De La Cruz <mailto:ddelacruz@telconet.ec>
 * @version 1.0
 * @since 03/01/2022
 */
@RestController
@RequestMapping
public class PersonaEmpresaRolCaracController {
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    PersonaEmpresaRolCaracService personaEmpresaRolCaracService;

    /**
	 * Método que registra una caracteristica con su respectivo valor para una persona empresa rol
	 * 
	 * @author David De La Cruz <mailto:ddelacruz@telconet.ec>
	 * @version 1.0
	 * @since 03/01/2022
	 * 
	 * @return InfoPersonaEmpresaRolCarac
	 * @throws Exception
	 */
	@PostMapping(path = "guardaPersonaEmpresaRolCarac", consumes = "application/json")
	public GenericBasicResponse<Object> guardaPersonaEmpresaRolCarac(@RequestBody Object request) throws Exception {
		log.info("Petición recibida: guardaPersonaEmpresaRolCarac");
		GenericBasicResponse<Object> response = new GenericBasicResponse<>();
		response.setData(personaEmpresaRolCaracService.guardar(Formato.mapearObjDeserializado(request, InfoPersonaEmpresaRolCarac.class)));
		return response;
	}

    /**
	 * Método que actualiza una caracteristica con su respectivo valor para una persona empresa rol
	 * 
	 * @author David De La Cruz <mailto:ddelacruz@telconet.ec>
	 * @version 1.0
	 * @since 03/01/2022
	 * 
	 * @return InfoPersonaEmpresaRolCarac
	 * @throws Exception
	 */
	@PostMapping(path = "actualizaPersonaEmpresaRolCarac", consumes = "application/json")
	public GenericBasicResponse<Object> actualizaPersonaEmpresaRolCarac(@RequestBody Object request) throws Exception {
		log.info("Petición recibida: actualizaPersonaEmpresaRolCarac");
		GenericBasicResponse<Object> response = new GenericBasicResponse<>();
		response.setData(personaEmpresaRolCaracService.actualizar(Formato.mapearObjDeserializado(request, InfoPersonaEmpresaRolCarac.class)));
		return response;
	}

    /**
	 * Método que retorna lista de caracteristicas con su respectivos valores sobre personas empresas rol según los filtros
	 * 
	 * @author David De La Cruz <mailto:ddelacruz@telconet.ec>
	 * @version 1.0
	 * @since 03/01/2022
	 * 
	 * @return GenericListResponse<InfoPersonaEmpresaRolCarac>
	 * @throws Exception
	 */
	@PostMapping(path = "listaPersonaEmpresaRolCaracPor", consumes = "application/json")
	public GenericListResponse<Object> listaPersonaEmpresaRolCaracPor(@RequestBody Object request) throws Exception {
		log.info("Petición recibida: listaPersonaEmpresaRolCaracPor");
		GenericListResponse<Object> response = new GenericListResponse<>();
		List<InfoPersonaEmpresaRolCarac> listaInfoPersonaEmpresaRolCarac = personaEmpresaRolCaracService.listaPor(Formato.mapearObjDeserializado(request,InfoPersonaEmpresaRolCarac.class));
		response.setData(Formato.mapearListObjDeserializado(listaInfoPersonaEmpresaRolCarac, Object.class));
		return response;
	}

}
