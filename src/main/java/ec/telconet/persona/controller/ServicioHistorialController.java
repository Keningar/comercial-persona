package ec.telconet.persona.controller;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.telconet.microservicio.dependencia.util.general.Formato;
import ec.telconet.microservicio.dependencia.util.response.GenericListResponse;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.HistorialServicioPorFechaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaPorEmpresaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoServicioHistorial;
import ec.telconet.persona.service.ServicioHistorialService;

/**
 * Clase utilizada para publicar microservicios técnicos con información referente al historial del servicio
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@RestController
@RequestMapping
public class ServicioHistorialController {
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	ServicioHistorialService servicioHistorialService;
	
	/**
	 * Método que retorna la lista del historial de servicio por fecha
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param request {@linkplain HistorialServicioPorFechaReqDTO}
	 * @return {@linkplain GenericListResponse}
	 * @throws Exception
	 */
	@PostMapping(path = "listaHistorialServicioPorFecha", consumes = "application/json")
	public GenericListResponse<Object> listaHistorialServicioPorFecha(@RequestBody HistorialServicioPorFechaReqDTO request)
			throws Exception {
		log.info("Petición recibida: listaHistorialServicioPorFecha");
		GenericListResponse<Object> response = new GenericListResponse<>();
		response.setData(Collections.singletonList(servicioHistorialService.
				listaHistorialServicioPorFecha(Formato.mapearObjDeserializado(request, HistorialServicioPorFechaReqDTO.class))));
		return response;
	}
}
