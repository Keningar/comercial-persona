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
import ec.telconet.microservicio.dependencia.util.response.GenericListResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoClienteNotMasivaDetReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoClienteNotMasivaDetResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoUsuarioResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.AdmiCaracteristica;
import ec.telconet.persona.service.ConsultasService;

/**
 * Clase utilizada para publicar microservicios técnicos con información referente a las consultas DTO
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@RestController
@RequestMapping
public class ConsultasController {
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	ConsultasService consultasService;
	
	/**
	 * Método que retorna la información adicional de un usuario
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request {@linkplain InfoUsuarioReqDTO}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "infoUsuario", consumes = "application/json")
	public GenericListResponse<InfoUsuarioResDTO> infoUsuario(@RequestBody InfoUsuarioReqDTO request) throws Exception {
		log.info("Petición recibida: infoUsuario");
		GenericListResponse<InfoUsuarioResDTO> response = new GenericListResponse<InfoUsuarioResDTO>();
		response.setData(consultasService.infoUsuario(request));
		return response;
	}

	/**
	 * Método que retorna la información de una caracteristica
	 * 
	 * @author Pedro Velez <mailto:psvelez@telconet.ec>
	 * @version 1.0
	 * @since 28/04/2024
	 * 
	 * @param request {@linkplain Object}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "listaCaracteristicaPor", consumes = "application/json")
	public GenericListResponse<Object> listaCaracteristicaPor(@RequestBody Object request) throws Exception {
		log.info("Petición recibida: listaCaracteristicaPor");
		GenericListResponse<Object> response = new GenericListResponse<>();
		List<AdmiCaracteristica> listaCaracteristica = consultasService.listarCaracteristicaPor(Formato.mapearObjDeserializado(
			                                            request,AdmiCaracteristica.class));
		response.setData(Formato.mapearListObjDeserializado(listaCaracteristica, Object.class));

		return response;
	}

	/**
	 * Método que lista de clientes para enviar notificacion push por fallas masivas
	 * 
	 * @author Pedro Velez <mailto:psvelez@telconet.ec>
	 * @version 1.0
	 * @since 11/05/2024
	 * 
	 * @param request {@linkplain InfoUsuariInfoClienteNotMasivaDetReqDTOoReqDTO}
	 * @return {@linkplain InfoClienteNotMasivaDetResDTO}
	 * @throws Exception
	 */
	@PostMapping(path = "listaClienteNotMasivaDet", consumes = "application/json")
	public GenericListResponse<InfoClienteNotMasivaDetResDTO> listaClienteNotMasivaDet(@RequestBody InfoClienteNotMasivaDetReqDTO request)
	 throws Exception {
		log.info("Petición recibida: listaClienteNotMasivaDet");
		GenericListResponse<InfoClienteNotMasivaDetResDTO> response = new GenericListResponse<InfoClienteNotMasivaDetResDTO>();
		response.setData(consultasService.listarClienteNotMasivaDet(request));
		return response;
	}
}
