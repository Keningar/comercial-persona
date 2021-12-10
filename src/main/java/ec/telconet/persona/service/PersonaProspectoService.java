package ec.telconet.persona.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.Gson;

import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaReferidoService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.impl.CmkgPersonaConsultaImpl;
import ec.telconet.microservicios.dependencias.esquema.comercial.utils.ComercialValidators;
import ec.telconet.microservicios.dependencias.esquema.general.entity.AdmiRol;
import ec.telconet.microservicios.dependencias.esquema.general.repository.AdmiRolRepository;
import ec.telconet.persona.dto.PersonaProspectoRespDto;
import ec.telconet.persona.utils.PersonaUtils;
import ec.telconet.microservicio.dependencia.util.exception.GenericException;
import ec.telconet.microservicio.dependencia.util.general.ConsumoWebService;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.BuscarEmpresasRolEstadosReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.DataPersonaDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.DatosEquifax;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.EquifaxReqDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaEmpresaRolPorEmpresaActivoReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaEquifaxRecomendacionResDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaProspectoReqDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.TarjetasEquifaxRecomendacionResDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoEmpresaRol;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersona;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpFormaPago;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRol;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaReferido;
import ec.telconet.microservicios.dependencias.esquema.comercial.repository.InfoEmpresaRolRepository;
import ec.telconet.microservicios.dependencias.esquema.comercial.repository.InfoPersonaRepository;

@Service
public class PersonaProspectoService {
	
	Logger log = LogManager.getLogger(this.getClass());

	
	@Autowired
	InfoPersonaService infoPersonaService;
	
	@Autowired
	PersonaEmpresaRolService personaEmpresaRolService;
	
	
	@Autowired
	InfoPersonaReferidoService infoReferidoService;
	
	
	@Autowired
	InfoPersonaRepository infoPersonaRepository;
	
	@Autowired
	InfoEmpresaRolRepository infoEmpresaRolRepository;
	
	@Autowired
	AdmiRolRepository admiRolRepository;
	
	@Autowired
	CmkgPersonaConsultaImpl cmkgPersonaConsultaImpl;
	

	@Autowired
    ConsumoWebService consumoWebService;
	
	@Autowired
	ComercialValidators validators;
	
	@Autowired
	PersonaUtils personaUtils;
	
	@Value("${ms.equifax.procesar}")
    String wsEquifaxProcesar;

	
	public PersonaProspectoRespDto personaProspecto(@RequestBody PersonaProspectoReqDto request) throws Exception {	    
		validators.validarConsultaPersonaProspecto(request);
		PersonaProspectoRespDto response=new PersonaProspectoRespDto();
	    InfoPersona persona=new InfoPersona();
	    persona.setIdentificacionCliente(request.getIdentificacion());    
	    List<InfoPersona> personas= infoPersonaService.listaPor(persona);
	    ///Si encuentro la persona empiezo a buscar los otros datos
	    if(!personas.isEmpty()) {	      
	  	    response.setPersona(new DataPersonaDTO(personas.get(0)));	  	    
	  	    PersonaEmpresaRolPorEmpresaActivoReqDTO requestConsulta = new PersonaEmpresaRolPorEmpresaActivoReqDTO();
	  	    requestConsulta.setIdentificacion(response.getPersona().getIdentificacionCliente());	  	   
	  	    ///Obtengo el referido de la persona
	  	    List<InfoPersonaReferido>infoReferido=  cmkgPersonaConsultaImpl.infoReferidoByIdentificacion(requestConsulta); 
	  	    if(!infoReferido.isEmpty()) {	    	
	  			Optional<InfoPersona> personaReferido=infoPersonaRepository.findById(infoReferido.get(0).getReferidoId());
	  			response.setPersonaReferido(personaReferido.get());
	  	    }	  	    
	  	    ///Obtengo todos los roles  de la persona 
	  	    requestConsulta.setEmpresaCod(request.getEmpresaCod());    
	  	    List<InfoPersonaEmpresaRol>empresaRoles= cmkgPersonaConsultaImpl.personaEmpresaRolPorEmpresaActivo(requestConsulta);
	  	    response.setEmpresaRoles(empresaRoles);
	  	    for(int i=0; i<empresaRoles.size(); i++)
	      	{	
	      		Optional<InfoEmpresaRol> empresaRol=	infoEmpresaRolRepository.findById(empresaRoles.get(i).getEmpresaRolId());
	      		if(empresaRol.isPresent()) {
	      			
	      		    Optional<AdmiRol> admiRoles=	admiRolRepository.findById(empresaRol.get().getRolId());
	          		response.getAdmiRoles().add(admiRoles.get());	

	      		}  	
	      	}	  	    
	  	    ///Obtengo todas ls formas de pago 
	  		List<InfoPersonaEmpFormaPago> formasPago= cmkgPersonaConsultaImpl.datosPersonaEmpFormaPago(requestConsulta);
	  		if(!formasPago.isEmpty()) 
	  		{	    	
	  		 response.setFormaPago(formasPago.get(0));
	  		}	  		  
	    }else if(request.getRequireRecomendacion()) {
	            PersonaEquifaxRecomendacionResDto  responseEquifax=personaRecomendacion(convertRequestPersonaRecomendacion(request));	            
	            if(!responseEquifax.getStatus().equalsIgnoreCase("OK")) {
		    		throw new GenericException("Ocurrio un problema para encontrar la recomendación para la identificación "+ request.getIdentificacion());
	            }
	            response.setIsRecomendado(true);
	            response.setPersona(responseEquifax.convertDataPersonaDTO());
	            response.getPersona().setTipoIdentificacion(request.getTipoIdentificacion());
	    	
	    }else {
    		throw new GenericException("No se encontro persona para la identificación "+ request.getIdentificacion());

	    }	  	    
	  	return response;
	  }
	
	public EquifaxReqDto convertRequestPersonaRecomendacion(PersonaProspectoReqDto request)throws Exception {       
		EquifaxReqDto nuevo=new  EquifaxReqDto();
		nuevo.setIpCreacion(personaUtils.getClientIp());
		nuevo.setOpcion("CONSULTA_DATOS_PERSONA");
		nuevo.setComandoConfiguracion("NO");
		nuevo.setEjecutaComando("NO");
		nuevo.setActualiza_datos("NO");
		nuevo.setEmpresa(request.getPrefijoEmPresa());
		nuevo.setUsrCreacion(request.getUser());
		DatosEquifax datos = new DatosEquifax();
		datos.setIdentificacion(request.getIdentificacion());
		String tipoIdentificacion=request.getTipoIdentificacion().substring(0, 1);
		datos.setTipoIdentificacion(tipoIdentificacion.toUpperCase());
		nuevo.setDatos(datos);
		return nuevo;
	}
	
	
	
	
	public PersonaEquifaxRecomendacionResDto personaRecomendacion(EquifaxReqDto request)throws GenericException {	
		 Gson gson = new Gson();		
		validators.validarConsultaEquifaxRecomendacion(request);
		PersonaEquifaxRecomendacionResDto response;
		ResponseEntity<Object> resWsEquiFax = consumoWebService.genericObjectRest(wsEquifaxProcesar, MediaType.APPLICATION_JSON, HttpMethod.POST, request,
	                null,
	                false);	
		String jsonResponse = gson.toJson( resWsEquiFax.getBody(), LinkedHashMap.class);
		response= gson.fromJson(jsonResponse, PersonaEquifaxRecomendacionResDto.class);
		 if(!response.getStatus().equalsIgnoreCase("OK")) {
	    		throw new GenericException("Ocurrio un problema para encontrar la recomendación para la identificación "+ 
		 request.getDatos().getIdentificacion()+" "+response.getMensaje() );
         }		 		 		 
		return  response;
	}
	
	
	
	public TarjetasEquifaxRecomendacionResDto tarjetaRecomendacion(EquifaxReqDto request)throws GenericException {	
		 Gson gson = new Gson();
		validators.validarConsultaEquifaxRecomendacion(request);
		TarjetasEquifaxRecomendacionResDto response;
		 ResponseEntity<Object> resWsEquiFax = consumoWebService.genericObjectRest(wsEquifaxProcesar, MediaType.APPLICATION_JSON, HttpMethod.POST, request,
	                null,
	                false);	
		String jsonResponse = gson.toJson( resWsEquiFax.getBody(), LinkedHashMap.class);
		response= gson.fromJson(jsonResponse, TarjetasEquifaxRecomendacionResDto.class);
		 if(!response.getStatus().equalsIgnoreCase("OK")) {
	    		throw new GenericException("Ocurrio un problema para encontrar la recomendación de las tarjetas para la identificación "+
		        request.getDatos().getIdentificacion()+" "+response.getMensaje());
         }
	 		 
		return  response;
	}
	
	
}
