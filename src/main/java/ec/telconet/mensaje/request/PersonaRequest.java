/*
 * @(#)ReportRequest.java 1.0 20/04/2019
 * 
 * Copyright 2019 Telconet S.A. All rights reserved.
 */

package ec.telconet.mensaje.request;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class PersonaRequest {
	protected String filename;

	@Override
	public String toString() {
		return "PersonaRequest [filename=" + filename + "]";
	}

}
