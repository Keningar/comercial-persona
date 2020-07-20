package ec.telconet.persona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.telconet.microservicio.dependencia.util.dto.PageDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRol;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaEmpresaRolService;

/**
 * Clase utilizada donde se encuentran los servicios de persona empresa rol
 *
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 21/08/2020
 */
@Service
public class PersonaEmpresaRolService {
	@Autowired
	InfoPersonaEmpresaRolService infoPersonaEmpresaRolService;
	
	/**
	 * Método que retorna la lista de datos de la persona empresa rol
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 21/08/2020
	 * 
	 * @return List<InfoPersonaEmpresaRol>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersonaEmpresaRol> listaPersonaEmpresaRol() throws Exception {
		return infoPersonaEmpresaRolService.lista();
	}
	
	/**
	 * Método que retorna la lista de datos de la persona empresa rol con filtros
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 21/08/2020
	 * 
	 * @param request InfoPersonaEmpresaRol
	 * @return List<InfoPersonaEmpresaRol>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersonaEmpresaRol> listaPersonaEmpresaRolPor(InfoPersonaEmpresaRol request) throws Exception {
		return infoPersonaEmpresaRolService.listaPor(request);
	}
	
	/**
	 * Método que retorna la paginación de una lista de datos de la persona empresa rol con filtros
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 21/08/2020
	 * 
	 * @param request PageDTO<InfoPersonaEmpresaRol>
	 * @return Page<InfoPersonaEmpresaRol>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public Page<InfoPersonaEmpresaRol> paginaListaPersonaEmpresaRolPor(PageDTO<InfoPersonaEmpresaRol> request) throws Exception {
		return infoPersonaEmpresaRolService.paginaListaPor(request);
	}
}
