package ec.telconet.persona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.telconet.microservicios.dependencias.esquema.comercial.dto.HistorialServicioPorFechaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoServicioHistorial;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoServicioHistorialService;

/**
 * Clase utilizada donde se encuentran los servicios del historial de servicio
 *
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@Service
public class ServicioHistorialService {
	@Autowired
	InfoServicioHistorialService infoServicioHistorialService;
	
	/**
	 * Método que retorna la lista del historial de servicio by fecha
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @return List<InfoServicioHistorial>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<InfoServicioHistorial> listaHistorialServicioPorFecha(HistorialServicioPorFechaReqDTO request) throws Exception {
		return infoServicioHistorialService.historialServicioPorFecha(request);
	}
}
