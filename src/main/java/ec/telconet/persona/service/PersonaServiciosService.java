package ec.telconet.persona.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import ec.telconet.microservicio.core.comercial.kafka.request.PersonaPuntoServiciosKafkaReq;
import ec.telconet.microservicio.core.comercial.kafka.response.PuntoServicioKafkaRes;
import ec.telconet.microservicio.dependencia.util.enumerado.ProcesoHandler;
import ec.telconet.microservicio.dependencia.util.exception.GenericException;
import ec.telconet.microservicio.dependencia.util.general.Formato;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.DetPlanPorEstadosReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InformacionClienteReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InformacionClienteResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.ServiciosClienteParamsReqDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.ServiciosClienteParamsResDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.AdmiProducto;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPlanCab;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPlanDet;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPunto;
import ec.telconet.microservicios.dependencias.esquema.comercial.repository.AdmiProductoRepository;
import ec.telconet.microservicios.dependencias.esquema.comercial.repository.InfoPlanCabRepository;
import ec.telconet.microservicios.dependencias.esquema.comercial.repository.InfoPuntoRepository;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.CmkgConsultaService;
import ec.telconet.microservicios.dependencias.esquema.comercial.service.impl.InfoPlanDetImpl;

@Service
public class PersonaServiciosService 
{
	Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	CmkgConsultaService cmkgConsultaService;

	@Autowired
	AdmiProductoRepository admiProductoRepository;

	@Autowired
	InfoPlanCabRepository infoPlanCabRepository;

	@Autowired
	InfoPlanDetImpl infoPlanDetImpl;
	
	
	@Autowired
	InfoPuntoRepository  infoPuntoRepository;
	

	public List<PuntoServicioKafkaRes> puntoServicios(PersonaPuntoServiciosKafkaReq request) throws GenericException {

		if (request.getPersonaEmpresaRolId() == null || request.getPersonaEmpresaRolId() == 0L) {
			throw new GenericException("El parámetro personaEmpresaRolId es requerido");
		}

		if (request.getEstadosServicios() == null || request.getEstadosServicios().isEmpty()) {
			throw new GenericException("El parámetro estadoServicios es requerido");
		}
		
		List<PuntoServicioKafkaRes> response = new ArrayList<>();		
		List<InfoPunto> puntosPersona=infoPuntoRepository.findByPersonaEmpresaRolId(request.getPersonaEmpresaRolId());
		List<InfoPunto> listInfoPuntos=new ArrayList<InfoPunto>();
		if(puntosPersona.isEmpty()) 
		{
			throw new GenericException("No se encontró ningun punto para la personaEmpresaRolId"+request.getPersonaEmpresaRolId());
		}
		
		
		if(request.getEstadosPuntos().isEmpty())
		{
			listInfoPuntos.addAll(puntosPersona);
		}else
		{
			for(InfoPunto punto :puntosPersona ) {
				if(request.getEstadosPuntos().contains(punto.getEstado())) {
					listInfoPuntos.add(punto);
				}				
			}			
		}
		
	
		for (InfoPunto punto : listInfoPuntos) 
		{
			PuntoServicioKafkaRes nuevo = Formato.mapearObjDeserializado(punto, PuntoServicioKafkaRes.class);
			nuevo.setServicios(new ArrayList<>());
			for (String estado : request.getEstadosServicios()) {
				
				ServiciosClienteParamsReqDTO reqServiciosCliente = new ServiciosClienteParamsReqDTO();
				reqServiciosCliente.setPersonaEmpresaRolId(request.getPersonaEmpresaRolId());
				reqServiciosCliente.setPuntoId(punto.getIdPunto());
				reqServiciosCliente.setEstado(estado);
				log.info(new Gson().toJson(reqServiciosCliente));
				List<ServiciosClienteParamsResDTO> listServiciosCliente = cmkgConsultaService
						.serviciosClienteParams(reqServiciosCliente);
				List<PuntoServicioKafkaRes.ServiciosClienteParamsResDTO> listServicios = Formato
						.mapearListObjDeserializado(listServiciosCliente,
								PuntoServicioKafkaRes.ServiciosClienteParamsResDTO.class);

				for (PuntoServicioKafkaRes.ServiciosClienteParamsResDTO servicio : listServicios) 
				{
					if (servicio.getPlanId() != null) 
					{
						Optional<InfoPlanCab> planOptional = infoPlanCabRepository.findById(servicio.getPlanId());
						if (planOptional.isPresent()) 
						{
							PuntoServicioKafkaRes.Plan plan = new PuntoServicioKafkaRes.Plan();
							PuntoServicioKafkaRes.InfoPlanCab planCab = Formato.mapearObjDeserializado(
									planOptional.get(), PuntoServicioKafkaRes.InfoPlanCab.class);							
							plan.setPlanServicio(planCab);					
							DetPlanPorEstadosReqDTO detPorEstado = new DetPlanPorEstadosReqDTO();
							detPorEstado.setPlanId(servicio.getPlanId());
							List<String> estados = new ArrayList<>();
							estados.add(ProcesoHandler.Activo.toString());
							detPorEstado.setListEstados(estados);							
							List<InfoPlanDet> listDetalle = infoPlanDetImpl.detPlanPorEstados(detPorEstado);							
							List<PuntoServicioKafkaRes.InfoPlanDet> listDet = Formato
									.mapearListObjDeserializado(listDetalle, PuntoServicioKafkaRes.InfoPlanDet.class);
							plan.setDetalle(listDet);
							servicio.setPlan(plan);
						}
					}
					if (servicio.getProductoId() != null) {
						Optional<AdmiProducto> productoOptional = admiProductoRepository
								.findById(servicio.getProductoId());
						if (productoOptional.isPresent()) {
							PuntoServicioKafkaRes.AdmiProducto producto = Formato.mapearObjDeserializado(
									productoOptional.get(), PuntoServicioKafkaRes.AdmiProducto.class);
							servicio.setProducto(producto);

						}
					}
				}
				nuevo.getServicios().addAll(listServicios);
			}
			response.add(nuevo);
		}
		return response;
	}
}
