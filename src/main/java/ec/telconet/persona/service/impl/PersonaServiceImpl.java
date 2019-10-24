/*
 * @(#)ReporteServiceImpl.java 1.0 20/04/2019
 * 
 * Copyright 2019 Telconet S.A. All rights reserved.
 */

package ec.telconet.persona.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import ec.telconet.mensaje.request.PersonaRequest;
import ec.telconet.mensaje.response.PersonaResponse;
import ec.telconet.persona.service.PersonaService;

/**
 * Implementación de la generación del reporte
 *
 * @author Christian Jaramillo Espinoza <mailto:cjaramilloe@telconet.ec>
 * @version 1.0
 * @since 20/04/2019
 */
@Component
public class PersonaServiceImpl implements PersonaService {
	private static final Log logger = LogFactory.getLog(PersonaServiceImpl.class);

	// @Autowired
	// private KafkaMessageProducer mailMessageProducer;

	@Override
	public PersonaResponse consultarPersonaPorEstado(PersonaRequest reporteInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
