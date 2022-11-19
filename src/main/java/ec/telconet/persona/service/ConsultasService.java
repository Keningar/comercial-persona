package ec.telconet.persona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoClienteNotMasivaDetReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoClienteNotMasivaDetResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.AdmiCaracteristica;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.AdmiCaracteristicaService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.CmkgConsultaService;
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

	@Autowired
	AdmiCaracteristicaService carateristicaService;
	
	@Autowired
	CmkgConsultaService cmkgConsultaService;
	
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

	/**
	 * Método que retorna la información adicional de un usuario
	 * 
	 * @author Pedro Velez <mailto:psvelez@telconet.ec>
	 * @version 1.0
	 * @since 27/04/2022
	 * 
	 * @param request AdmiCaracteristica
	 * @return List<AdmiCaracteristica>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<AdmiCaracteristica> listarCaracteristicaPor(AdmiCaracteristica request) throws Exception{
        return  carateristicaService.listaPor(request);
	}

	/**
	 * Método que lista de clientes para enviar notificacion push por fallas masivas.
	 * 
	 * @author Pedro Velez <mailto:psvelez@telconet.ec>
	 * @version 1.0
	 * @since 10/05/2022
	 * 
	 * @param request InfoClienteNotMasivaDetReqDTO
	 * @return List<InfoClienteNotMasivaDetResDTO>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoClienteNotMasivaDetResDTO> listarClienteNotMasivaDet(InfoClienteNotMasivaDetReqDTO request) throws Exception{
        return cmkgConsultaService.obtenerClienteNotMasivaPush(request);
	}
	
}
