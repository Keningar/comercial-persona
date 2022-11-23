package ec.telconet.persona.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.telconet.microservicio.dependencia.util.exception.GenericException;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.AgregarPersonaListaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.AgregarPersonaListaResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.BusquedaPersonaListaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.BusquedaPersonaListaResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoClienteNotMasivaDetReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoClienteNotMasivaDetResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.AdmiCaracteristica;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.AdmiCaracteristicaService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.CmkgConsultaService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.CmkgListaPersonaService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.CmkgPersonaConsultaService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoEmpresaRolService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaEmpRolEnunService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaEmpresaRolService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaService;
import ec.telconet.microservicios.dependencias.esquema.general.service.AdmiRolService;
import ec.telconet.microservicios.dependencias.esquema.general.service.AdmiTipoRolService;
import ec.telconet.persona.utils.PersonaConstants;

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
	
    @Autowired
    CmkgListaPersonaService cmkgListaPersonaService;

    @Autowired
    InfoPersonaEmpRolEnunService infoPersonaEmpRolEnunService;

    @Autowired
    InfoPersonaService infoPersonaService;

    @Autowired
    AdmiTipoRolService admiTipoRolService;

    @Autowired
    AdmiRolService admiRolService;

    @Autowired
    InfoEmpresaRolService infoEmpresaRolService;

    @Autowired
    InfoPersonaEmpresaRolService infoPersonaEmpresaRolService;
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
	
	
    @Transactional(readOnly = true)
    public AgregarPersonaListaResDTO agregarPersonaLista(AgregarPersonaListaReqDTO request) throws Exception {
        if(request.getTipoIdentificacion() != null) {
            boolean isValidTipoIdent = Stream.of(PersonaConstants.TIPO_IDENTIFICACION.values()).anyMatch(v -> v.name().equals(request.getTipoIdentificacion()));
            if(!isValidTipoIdent) {
                throw new GenericException("El tipo identificación ingresado es invalido "+request.getTipoIdentificacion());
            }
        }
        if(request.getTipoPersona() != null) {
            boolean isValidTipoTributatio = Stream.of(PersonaConstants.TIPO_TRIBUTARIO.values()).anyMatch(v -> v.name().equals(request.getTipoPersona()));
            if(!isValidTipoTributatio) {
                throw new GenericException("El tipo persona ingresado es invalido "+request.getTipoPersona());
            }
        }
        return cmkgListaPersonaService.agregarPersonaLista(request);
    }

    public List<BusquedaPersonaListaResDTO> buscarPersonaLista(BusquedaPersonaListaReqDTO request) throws Exception {
        String estados = String.join(",", request.getArrayEstados());
        request.setEstado(estados);
        return cmkgListaPersonaService.buscarPersonaLista(request);
    }
    
}
