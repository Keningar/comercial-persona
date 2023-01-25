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

import ec.telconet.microservicio.core.comercial.kafka.request.ValidarFormaContactoKafkaReq;
import ec.telconet.microservicio.dependencia.util.exception.GenericException;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.CrearPreClienteResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.FormaContactoReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.AdmiFormaContacto;
import ec.telconet.microservicios.dependencias.esquema.comercial.repository.AdmiFormaContactoRepository;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.CmkgPreClienteTransaccionService;
import ec.telconet.microservicios.dependencias.esquema.general.entity.AdmiPais;
import ec.telconet.microservicios.dependencias.esquema.general.repository.AdmiPaisRepository;
import ec.telconet.persona.utils.PersonaConstants;
import ec.telconet.persona.utils.PersonaUtils;
import ec.telconet.microservicio.dependencia.util.general.Formato;

/**
 * Consumo de paquete CmkgPreClienteTransaccionService
 *
 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
 * @version 1.0
 * @since 20/08/2021
 */

@Service
public class PreClienteService {

	Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	CmkgPreClienteTransaccionService cmkgPreClienteTransaccionService;

	@Autowired
	ConsumoKafkaService consumoKafkaService;

	@Autowired
	AdmiPaisRepository admiPaisRepository;

	@Autowired
	AdmiFormaContactoRepository admiFormaContactoRepository;

	@Autowired
	PersonaUtils personaUtils;

	/**
	 * Método para crear pre cliente
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 20/08/2021
	 */
	@Transactional(readOnly = true)
	public CrearPreClienteResDTO crearPreCliente(CrearPreClienteReqDTO request) throws Exception {
		return cmkgPreClienteTransaccionService.crearPreCliente(request);
	}

	/**
	 * Método para validar formas de contacto
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 01/07/2022
	 */

	public List<String> validarFormaContacto(ValidarFormaContactoKafkaReq request) throws Exception {
		Boolean exteCorreo = false;
		
		List<FormaContactoReqDTO> contactos = Formato.mapearListObjDeserializado(request.getFormasContacto(),
				FormaContactoReqDTO.class);

		if(contactos.isEmpty()) {
			throw new GenericException(PersonaConstants.MSG_ERROR_CONTACTO_NO_FOUND);

		}
		

		List<String> listaValidaciones = new ArrayList<String>();
		Optional<AdmiPais> optionalPais = admiPaisRepository.findById(request.getIdPais());
		if (!optionalPais.isPresent()) {
			throw new GenericException(PersonaConstants.MSG_ERROR_PAIS_NO_EXISTE);
		}

		AdmiPais pais = optionalPais.get();

		if (Arrays.asList(PersonaConstants.LIST_VALIDA_PAISES_NOMBRE)
				.indexOf(pais.getNombrePais().toUpperCase()) == -1) {
			throw new GenericException(
					PersonaConstants.MSG_ERROR_PAIS_NO_VALIDO.replace("{{pais}}", pais.getNombrePais()));
		}

		
		for (FormaContactoReqDTO itemformaContacto : contactos) {

			Optional<AdmiFormaContacto> opcionAdminFormaContacto = admiFormaContactoRepository
					.findById((long) itemformaContacto.getFormaContactoId());

			if (!opcionAdminFormaContacto.isPresent()) {
				throw new GenericException(PersonaConstants.MSG_ERROR_CONTACTO_NO_EXISTE);
			}

			AdmiFormaContacto adminFormaContacto = opcionAdminFormaContacto.get();
			String contactoNombre = adminFormaContacto.getDescripcionFormaContacto().toUpperCase();
			String contactoValor = itemformaContacto.getValor();
			String mensajeValidacion = "";

			mensajeValidacion = PersonaUtils.validadorContacto(contactoNombre, contactoValor,
					PersonaConstants.LIST_VALIDA_FORMATO_TELEFONO, PersonaConstants.EXP_REG_TELEFONO,
					PersonaConstants.MSG_VALIDACION_TELEFONO);

			if (mensajeValidacion == "") {
				mensajeValidacion = PersonaUtils.validadorContacto(contactoNombre, contactoValor,
						PersonaConstants.LIST_VALIDA_FORMATO_TELEFONO_INTERNACIONAL,
						PersonaConstants.EXP_REG_TELEFONO_INTERNACIONAL,
						PersonaConstants.MSG_VALIDACION_TELEFONO_INTERNACIONAL);
			}

			if (mensajeValidacion == "") {
				mensajeValidacion = PersonaUtils.validadorContacto(contactoNombre, contactoValor,
						PersonaConstants.LIST_VALIDA_FORMATO_TELEFONO_MOVIL, PersonaConstants.EXP_REG_TELEFONO_MOVIL,
						PersonaConstants.MSG_VALIDACION_TELEFONO_MOVIL);
			}

			if (mensajeValidacion == "") {

				for (String item : PersonaConstants.LIST_VALIDA_FORMATO_CORREO) {
					if (contactoNombre.indexOf(item) != -1) {
						if (!Formato.validarEmail(contactoValor)) {
							mensajeValidacion = PersonaConstants.MSG_VALIDACION_CORREO.replace("{{valor}}",
									contactoValor);

						} else {

							String[] parts = contactoValor.split("@");
							String dominio = parts.length != 0 ? parts[parts.length - 1] : "";
							Boolean validoDominio = personaUtils.verificarMailDNS(dominio);
							if (!validoDominio) {
								mensajeValidacion = PersonaConstants.MSG_VALIDACION_DOMINIO.replace("{{valor}}",
										contactoValor);
							} else {
								exteCorreo = true;
							}

						}
					}

				}

			}

			if (mensajeValidacion != "") {
				listaValidaciones.add(mensajeValidacion);
			}

		}
		
		if (!exteCorreo && request.getRequiereCorreo()) {
			listaValidaciones.add(PersonaConstants.MSG_VALIDACION_CORREO_REQ);
		}

		return listaValidaciones;
	}
}