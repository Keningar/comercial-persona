/*
 * @(#)ReporteriaController.java 1.0 20/04/2019
 * 
 * Copyright 2019 Telconet S.A. All rights reserved.
 */

package ec.telconet.persona.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;

import ec.telconet.mensaje.request.PersonaRequest;
import ec.telconet.mensaje.response.PersonaResponse;


public class PersonaController {

	private static final Log logger = LogFactory.getLog(PersonaController.class);
	

	@KafkaListener(topics = "${kafka.topic.persona.request}", containerFactory = "requestReplyListenerContainerFactory")
	@SendTo()
	public PersonaResponse receive(PersonaRequest vin) {
		logger.info("received request for VIN {} ");
		PersonaResponse car = new PersonaResponse();
		car.setError("0");
		car.setMensaje("prueba persona exitosa " + vin.getFilename());
		car.setStatus(200);
		logger.info("sending reply {} ");
		return car;
	}
}