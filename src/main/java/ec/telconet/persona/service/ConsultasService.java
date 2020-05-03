package ec.telconet.persona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.CmkgPersonaConsultaService;

/**
 * Clase utilizada donde se encuentran los servicios de consultas DTO
 *
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@Service
public class ConsultasService {
	@Autowired
	CmkgPersonaConsultaService cmkgPersonaConsultaService;
	
	/**
	 * Método que retorna la información adicional de un usuario
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request InfoUsuarioReqDTO
	 * @return List<InfoUsuarioResDTO>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoUsuarioResDTO> infoUsuario(InfoUsuarioReqDTO request) throws Exception {
		return cmkgPersonaConsultaService.infoUsuario(request);
	}
}
