package ec.telconet.persona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRolCarac;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaEmpresaRolCaracService;

/**
 * Clase utilizada donde se encuentran los servicios de persona empresa rol carac
 *
 * @author David De La Cruz <mailto:ddelacruz@telconet.ec>
 * @version 1.0
 * @since 03/01/2022
 */
@Service
public class PersonaEmpresaRolCaracService {
    
    @Autowired
    InfoPersonaEmpresaRolCaracService infoPersonaEmpresaRolCaracService;

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
	@Transactional()
	public InfoPersonaEmpresaRolCarac guardar(InfoPersonaEmpresaRolCarac request) throws Exception {
		return infoPersonaEmpresaRolCaracService.guardar(request);
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
	@Transactional()
	public InfoPersonaEmpresaRolCarac actualizar(InfoPersonaEmpresaRolCarac request) throws Exception {
		return infoPersonaEmpresaRolCaracService.actualizar(request);
	}

     /**
	 * Método que retorna lista de caracteristicas con su respectivos valores sobre personas empresas rol según los filtros
	 * 
	 * @author David De La Cruz <mailto:ddelacruz@telconet.ec>
	 * @version 1.0
	 * @since 03/01/2022
	 * 
	 * @return List<InfoPersonaEmpresaRolCarac>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoPersonaEmpresaRolCarac> listaPor(InfoPersonaEmpresaRolCarac request) throws Exception {
		return infoPersonaEmpresaRolCaracService.listaPor(request);
	}
}
