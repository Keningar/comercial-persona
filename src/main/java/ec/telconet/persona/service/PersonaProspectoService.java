package ec.telconet.persona.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaReferidoService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.InfoPersonaService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.impl.CmkgPersonaConsultaImpl;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.impl.InfoPersonaFormaContactoImpl;
import ec.telconet.microservicios.dependencias.esquema.comercial.utils.ComercialValidators;
import ec.telconet.microservicios.dependencias.esquema.general.entity.AdmiBanco;
import ec.telconet.microservicios.dependencias.esquema.general.entity.AdmiBancoTipoCuenta;
import ec.telconet.microservicios.dependencias.esquema.general.entity.AdmiRol;
import ec.telconet.microservicios.dependencias.esquema.general.entity.AdmiTipoCuenta;
import ec.telconet.microservicios.dependencias.esquema.general.repository.AdmiBancoRepository;
import ec.telconet.microservicios.dependencias.esquema.general.repository.AdmiBancoTipoCuentaRepository;
import ec.telconet.microservicios.dependencias.esquema.general.repository.AdmiRolRepository;
import ec.telconet.microservicios.dependencias.esquema.general.repository.AdmiTipoCuentaRepository;
import ec.telconet.persona.dto.PersonaProspectoRespDto;
import ec.telconet.persona.utils.PersonaUtils;
import ec.telconet.microservicio.dependencia.util.enumerado.StatusHandler;
import ec.telconet.microservicio.dependencia.util.exception.GenericException;
import ec.telconet.microservicio.dependencia.util.general.ConsumoWebService;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.DataPersonaDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.DatosEquifax;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.EquifaxReqDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoPersonaEmpFormaPagoDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PerfilPersonaReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaEmpresaRolPorEmpresaActivoReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaEquifaxRecomendacionResDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.PersonaProspectoReqDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.SeguPerfilPersonaDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.TarjetasEquifaxRecomendacionResDto;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoEmpresaGrupo;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoEmpresaRol;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersona;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRol;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaFormaContacto;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaReferido;
import ec.telconet.microservicios.dependencias.esquema.comercial.repository.InfoEmpresaGrupoRepository;
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
	
	@Value("${ms.equifax.codEmpresa}")
	String codEmpresaEquifax;
	
	@Autowired
	AdmiBancoTipoCuentaRepository admiBancoTipoCuentaRepository;
	
	
	@Autowired
	AdmiBancoRepository admiBancoRepository;
	
	@Autowired
	AdmiTipoCuentaRepository admiTipoCuentaRepository;
	
	@Autowired
	InfoPersonaFormaContactoImpl infoPersonaFormaContactoImpl;
	
	@Autowired 
    InfoEmpresaGrupoRepository infoEmpresaGrupoRepository;
	
	public PersonaProspectoRespDto personaProspecto(@RequestBody PersonaProspectoReqDto request) throws Exception {	    
		validators.validarConsultaPersonaProspecto(request);
		PersonaProspectoRespDto response=new PersonaProspectoRespDto();
	    InfoPersona persona=new InfoPersona();
	    persona.setIdentificacionCliente(request.getIdentificacion()); 
	    if(request.getTipoIdentificacion()!=null && !request.getTipoIdentificacion().isEmpty()) {
	    	persona.setTipoIdentificacion(request.getTipoIdentificacion());	
	    }
	    
	    List<InfoPersona> personas= infoPersonaService.listaPor(persona);
	    ///Si encuentro la persona empiezo a buscar los otros datos
	    if(!personas.isEmpty()) {	      
	  	    response.setPersona(new DataPersonaDTO(personas.get(0)));	  	    
	  	    InfoPersonaFormaContacto contactoRequest=new InfoPersonaFormaContacto();
	        contactoRequest.setEstado(StatusHandler.Activo.toString());
	        contactoRequest.setPersonaId(personas.get(0).getIdPersona());
	        
	        
	        response.setFormasContacto(infoPersonaFormaContactoImpl.listaPor(contactoRequest));
	  	    
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
		  	  for(InfoPersonaEmpresaRol itemEmpresaRol: response.getEmpresaRoles()) 
	          {
	            Optional<InfoEmpresaRol> empresaRolOption=  infoEmpresaRolRepository.findById(itemEmpresaRol.getEmpresaRolId());
	            if(empresaRolOption.isPresent()) 
	            {
	                InfoEmpresaRol empresaRol=empresaRolOption.get();
	                Optional<AdmiRol> admiRoles=  admiRolRepository.findById(empresaRol.getRolId());
	                response.getAdmiRoles().add(admiRoles.get());  
		            itemEmpresaRol.setEmpresaRol(empresaRol);
	            }    
	            
	          }  	    
	  	    ///Obtengo todas ls formas de pago 
	  		List<InfoPersonaEmpFormaPagoDTO> formasPago= formaPagoProspecto(requestConsulta);
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
	    	
	    }else 
		{	    	   			
    		throw new GenericException("La identificación ingresada no pertenece a un cliente");
	    }	  	    
	  	return response;
	  }
	
	
	public List<InfoPersonaEmpFormaPagoDTO>  formaPagoProspecto(PersonaEmpresaRolPorEmpresaActivoReqDTO request) throws GenericException {
        //Obtengo todas las formas de pago 
  		List<InfoPersonaEmpFormaPagoDTO> formasPago= cmkgPersonaConsultaImpl.datosPersonaEmpFormaPago(request);		
  		for(InfoPersonaEmpFormaPagoDTO pagos:formasPago) {
  			
  			if(pagos.getBancoTipoCuentaId()!=null) {
  				
  				Optional<AdmiBancoTipoCuenta> admiBancoTipoCuenta=	admiBancoTipoCuentaRepository.findById(pagos.getBancoTipoCuentaId());
  								
  				Optional<AdmiBanco> admiBanco=admiBancoRepository.findById(admiBancoTipoCuenta.get().getBancoId());  
  				pagos.setIdBanco(admiBanco.get().getIdBanco());
  				pagos.setDescripcionBanco(admiBanco.get().getDescripcionBanco());
  			
  				
  				Optional<AdmiTipoCuenta> admiTipoCuenta=admiTipoCuentaRepository.findById(admiBancoTipoCuenta.get().getTipoCuentaId());  
  				pagos.setTipoCuentaId(admiTipoCuenta.get().getIdTipoCuenta());
  				pagos.setDescripcionCuenta(admiTipoCuenta.get().getDescripcionCuenta());  				
  				
  			}
  			
  			
  		}
  		
  		
  		
	    return formasPago;
	}
	
	
	
	public List<SeguPerfilPersonaDTO> validarPerfilPersona(PerfilPersonaReqDTO request) throws GenericException{
         return cmkgPersonaConsultaImpl.validarPerfilPersona(request);
	}
	
	
	
	
	public EquifaxReqDto convertRequestPersonaRecomendacion(PersonaProspectoReqDto request)throws Exception {      
		
		Optional<InfoEmpresaGrupo> grupoOptional = infoEmpresaGrupoRepository.findById(Long.parseLong(request.getEmpresaCod())); 
        String prefijoEmpresa= grupoOptional.get().getPrefijo(); 
		EquifaxReqDto nuevo=new  EquifaxReqDto();
		nuevo.setIpCreacion(personaUtils.getClientIp());
		nuevo.setOpcion("CONSULTA_DATOS_PERSONA");
		nuevo.setComandoConfiguracion("NO");
		nuevo.setEjecutaComando("NO");
		nuevo.setActualizaDatos("NO");
		nuevo.setEmpresa(prefijoEmpresa);
		nuevo.setUsrCreacion(request.getUser());
		DatosEquifax datos = new DatosEquifax();
		datos.setIdentificacion(request.getIdentificacion());
		String tipoIdentificacion=request.getTipoIdentificacion().substring(0, 1);
		datos.setTipoIdentificacion(tipoIdentificacion.toUpperCase());
		nuevo.setDatos(datos);
		return nuevo;
	}
	
	
	
	
	public PersonaEquifaxRecomendacionResDto personaRecomendacion(EquifaxReqDto request)throws GenericException {
		log.info("Dentro de PersonaEquifaxRecomendacionResDto");
		 Gson gson = new Gson();		
		request.setComandoConfiguracion("NO");
		request.setEjecutaComando("NO");
		request.setIpCreacion(personaUtils.getClientIp());
		request.setActualizaDatos("NO");
		request.setEmpresa(codEmpresaEquifax);
		validators.validarConsultaEquifaxRecomendacion(request);
		PersonaEquifaxRecomendacionResDto response;
		Map<String, Object> requestPost = new Gson().fromJson(
			    gson.toJson(request), new TypeToken<HashMap<String, Object>>() {}.getType()
			);
		log.info(gson.toJson(requestPost));
		ResponseEntity<Object> resWsEquiFax = consumoWebService.genericObjectRest(wsEquifaxProcesar, MediaType.APPLICATION_JSON, HttpMethod.POST, requestPost,
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
		log.info("Dentro de TarjetasEquifaxRecomendacionResDto");
		Gson gson = new Gson();
		request.setComandoConfiguracion("NO");
		request.setEjecutaComando("NO");
		request.setActualizaDatos("NO");
		request.setIpCreacion(personaUtils.getClientIp());
		request.setEmpresa(codEmpresaEquifax);
		validators.validarConsultaEquifaxRecomendacion(request);

		Map<String, Object> requestPost = new Gson().fromJson(
			    gson.toJson(request), new TypeToken<HashMap<String, Object>>() {}.getType()
			);
		log.info(gson.toJson(requestPost));

		TarjetasEquifaxRecomendacionResDto response;
		 ResponseEntity<Object> resWsEquiFax = consumoWebService.genericObjectRest(wsEquifaxProcesar, MediaType.APPLICATION_JSON, HttpMethod.POST, requestPost,
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
