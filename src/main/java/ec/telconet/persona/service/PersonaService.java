/*
 * @(#)ReportService.java 1.0 20/04/2019
 * 
 * Copyright 2019 Telconet S.A. All rights reserved.
 */

package ec.telconet.persona.service;

import ec.telconet.mensaje.request.PersonaRequest;
import ec.telconet.mensaje.response.PersonaResponse;


/**
 * Interfaz que define los procedimientos necesarios usados para generción y configuración del reporte
 *
 * @author Jose Vinueza Herrera <mailto:jdvinueza@telconet.ec>
 * @version 1.0
 * @since 20/04/2019
 */

public interface PersonaService {
	PersonaResponse consultarPersonaPorEstado(PersonaRequest reporteInfo);
}