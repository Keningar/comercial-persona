package ec.telconet.persona.service;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.CmkgPreClienteTransaccionService;

/**
 * Consumo de paquete CmkgPreClienteTransaccionService JAC3
 *
 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
 * @version 1.0
 * @since 20/08/2021
 */

@Service
public class PreClienteService {
	@Autowired
	CmkgPreClienteTransaccionService cmkgPreClienteTransaccionService;
	
	/**
	 * Método para crear pre cliente JAC3
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 20/08/2021
	 * 
	 * @param request InfoUsuarioReqDTO
	 * @return List<InfoUsuarioResDTO>
	 * @throws Exception
	 */
	@Transactional(readOnly = true) 
	public CrearPreClienteResDTO crearPreCliente(CrearPreClienteReqDTO request)   throws Exception {
		return cmkgPreClienteTransaccionService.crearPreCliente(request); 
	}

 
}
