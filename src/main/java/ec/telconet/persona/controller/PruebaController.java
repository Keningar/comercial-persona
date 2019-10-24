package ec.telconet.persona.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ec.telconet.mensaje.request.PersonaRequest;

@RestController
@RequestMapping("persona")
public class PruebaController {
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET, produces = {"application/json"})
	public String getVehiculo(@RequestBody PersonaRequest request) {
		System.out.println("welcome backend persona traefix" + request.toString());
		return "welcome backend persona traefix ";
	}
	
	@RequestMapping(value = "/welcome2", method = RequestMethod.GET, produces = {"application/json"})
	public String getVehiculo2() {
		System.out.println("welcome2 backend persona traefix");
		return "welcome2 backend persona traefix ";
	}
	
	@RequestMapping(value = "/welcome3", method = RequestMethod.GET, produces = {"application/json"})
	public String getVehiculo3() {		
		System.out.println("welcome3 backend persona traefix");
		return "welcome3 backend persona traefix ";
	}

}
