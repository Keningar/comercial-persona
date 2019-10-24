/*
 * @(#)ReportResponse.java 1.0 20/04/2019
 * 
 * Copyright 2019 Telconet S.A. All rights reserved.
 */

package ec.telconet.mensaje.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para la respuesta de la petición POST
 *
 * @author Christian Jaramillo Espinoza <mailto:cjaramilloe@telconet.ec>
 * @version 1.0
 * @since 20/04/2019
 */

@NoArgsConstructor
@Data
public class PersonaResponse {
    private int status = 500;
    private String mensaje;
    private String error;
}
