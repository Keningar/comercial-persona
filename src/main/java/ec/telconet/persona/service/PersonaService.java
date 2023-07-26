package ec.telconet.persona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.telconet.microservicio.dependencia.util.dto.PageDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorCaractReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorDepartamentoReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorEmpresaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRegionReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorRolReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaResponsableReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaResponsableResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersona;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaService;

/**
 * Clase utilizada donde se encuentran los servicios de las personas
 *
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@Service
public class PersonaService {
	@Autowired
	InfoPersonaService infoPersonaService;
	
	/**
	 * Método que retorna la lista de personas
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @return List<InfoPersona>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersona> listaPersona() throws Exception {
		return infoPersonaService.lista();
	}
	
	/**
	 * Método que retorna la lista de personas con filtros
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request InfoPersona
	 * @return List<InfoPersona>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersona> listaPersonaPor(InfoPersona request) throws Exception {
		return infoPersonaService.listaPor(request);
	}
	
	/**
	 * Método que retorna la paginación de una lista de personas con filtros
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request PageDTO<InfoPersona>
	 * @return Page<InfoPersona>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public Page<InfoPersona> paginaListaPersonaPor(PageDTO<InfoPersona> request) throws Exception {
		return infoPersonaService.paginaListaPor(request);
	}
	
	/**
	 * Método que retorna la lista de personas por region
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request PersonaPorRegionReqDTO
	 * @return List<InfoPersona>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersona> listaPersonaPorRegion(PersonaPorRegionReqDTO request) throws Exception {
		return infoPersonaService.personaPorRegion(request);
	}
	
	/**
	 * Método que retorna la lista de personas por rol
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request PersonaPorRolReqDTO
	 * @return List<InfoPersona>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersona> listaPersonaPorRol(PersonaPorRolReqDTO request) throws Exception {
		return infoPersonaService.personaPorRol(request);
	}

	/**
	 * Método que retorna la lista de personas responsables de tablets
	 * 
	 * @author Kenth Encalada <mailto:kencalada@telconet.ec>
	 * @version 1.0
	 * @since 25/05/2023
	 * 
	 * @param request {@linkplain PersonaResponsableReqDTO}
	 * @return List<PersonaResponsableResDTO>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<PersonaResponsableResDTO> listaPersonaResponsable(PersonaResponsableReqDTO request) throws Exception {
		return infoPersonaService.personaResponsable(request);
	}
	
	/**
	 * Método que retorna la lista de personas por departamento
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request PersonaPorDepartamentoReqDTO
	 * @return List<InfoPersona>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersona> listaPersonaPorDepartamento(PersonaPorDepartamentoReqDTO request) throws Exception {
		return infoPersonaService.personaPorDepartamento(request);
	}
	
	/**
	 * Método que retorna la lista de personas por empresa (Cod o Prefijo)
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request PersonaPorEmpresaReqDTO
	 * @return List<InfoPersona>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersona> listaPersonaPorEmpresa(PersonaPorEmpresaReqDTO request) throws Exception {
		return infoPersonaService.personaPorEmpresa(request);
	}
	
	/**
	 * Método que retorna la lista de personas por caracteristica
	 * 
	 * @author Wilson Quinto <mailto:wquinto@telconet.ec>
	 * @version 1.0
	 * @since 19/11/2021
	 * 
	 * @param request {@link PersonaPorCaractReqDTO}
	 * @return List<InfoPersona>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersona> listaPersonaPorCaract(PersonaPorCaractReqDTO request) throws Exception {
		return infoPersonaService.personaPorCaract(request);
	}
}
