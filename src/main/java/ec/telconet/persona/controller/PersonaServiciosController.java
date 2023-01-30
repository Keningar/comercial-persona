package ec.telconet.persona.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.telconet.microservicio.core.comercial.kafka.request.PersonaPuntoServiciosKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.response.PuntoServicioKafkaRes;
import ec.telconet.microservicio.dependencia.util.response.GenericListResponse;
import ec.telconet.persona.service.PersonaServiciosService;


@RestController
@RequestMapping(path = "servicios")
public class PersonaServiciosController {
	
	Logger log = LogManager.getLogger(this.getClass());
	
    @Autowired
    PersonaServiciosService service;

    /**
     * Web-Service encargado de obtener los puntos y servicios
     *
     * @author Carlos Caguana <mailto:ccaguana@telconet.ec>
     * @version 1.0
     */
    @PostMapping(path = "/puntoServicios", consumes = "application/json")
    public GenericListResponse<PuntoServicioKafkaRes> puntoServicios(
            @RequestBody PersonaPuntoServiciosKafkaReq request) throws Exception {
    	GenericListResponse<PuntoServicioKafkaRes> response = new GenericListResponse<>();
        response.setData(service.puntoServicios(request));
        return response;
    }
    
    
}
