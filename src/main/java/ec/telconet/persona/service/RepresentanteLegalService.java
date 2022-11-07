package ec.telconet.persona.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.RepresentanteLegalReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.RepresentanteLegalResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.CmkgRepresentanteLegalService;

/**
 * Consumo de paquete CmkgRepresentanteLegalService
 *
 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
 * @version 1.0
 * @since 20/08/2021
 */

@Service
public class RepresentanteLegalService {

	Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	CmkgRepresentanteLegalService cmkgRepresentanteLegalService;

	@Autowired
	ConsumoKafkaService consumoKafkaService;

	/**
	 * Método para verificar disponibilidad de representante legal
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 08/08/2022
	 * 
	 **/
	@Transactional(readOnly = true)
	public RepresentanteLegalResDTO verificarRepresentanteLegal(RepresentanteLegalReqDTO request) throws Exception {
		return cmkgRepresentanteLegalService.verificarRepresentanteLegal(request);
	}

	/**
	 * Método para consultar disponibilidad de representante legal
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 08/08/2022
	 * 
	 **/
	@Transactional(readOnly = true)
	public List<RepresentanteLegalResDTO> consultarRepresentanteLegal(RepresentanteLegalReqDTO request)
			throws Exception {
		return cmkgRepresentanteLegalService.consultarRepresentanteLegal(request);
	}

	/**
	 * Método para actualizar disponibilidad de representante legal
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 08/08/2022
	 * 
	 **/
	@Transactional(readOnly = true)
	public String actualizarRepresentanteLegal(RepresentanteLegalReqDTO request) throws Exception {
		return cmkgRepresentanteLegalService.actualizarRepresentanteLegal(request);
	}

}