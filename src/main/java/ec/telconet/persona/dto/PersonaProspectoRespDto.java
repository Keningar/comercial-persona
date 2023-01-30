package ec.telconet.persona.dto;

import java.util.ArrayList;
import java.util.List;

import ec.telconet.microservicios.dependencias.esquema.comercial.dto.DataPersonaDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.dto.InfoPersonaEmpFormaPagoDTO;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersona;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaEmpresaRol;
import ec.telconet.microservicios.dependencias.esquema.comercial.entity.InfoPersonaFormaContacto;
import ec.telconet.microservicios.dependencias.esquema.general.entity.AdmiRol;
import lombok.Data;


@Data
public class PersonaProspectoRespDto {
	DataPersonaDTO persona;
	List<AdmiRol>admiRoles=new ArrayList<AdmiRol>();
	List<InfoPersonaEmpresaRol>empresaRoles=new ArrayList<InfoPersonaEmpresaRol>();
	InfoPersona personaReferido;
	InfoPersonaEmpFormaPagoDTO formaPago;
	Boolean isRecomendado=false;
	List<InfoPersonaFormaContacto>formasContacto=new ArrayList<>();
}
