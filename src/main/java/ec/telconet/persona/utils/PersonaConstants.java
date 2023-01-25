package ec.telconet.persona.utils;

/**
 * Variables constantes del ms
 *
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 15/10/2020
 *
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.1 - 17/11/2022 - Se requiere adicionar el tipo de identificación y el tipo tributario.
 */
public abstract class PersonaConstants {
	private PersonaConstants() {
	}

	public static enum TIPO_IDENTIFICACION {RUC,
        CED,
        PAS
    };

    public static enum TIPO_TRIBUTARIO {NAT,
        JUR
    };

	public static final String MSG_ERROR_PAIS_NO_EXISTE = "No se encuentra el país";
	public static final String MSG_ERROR_PAIS_NO_VALIDO = "El país {{pais}} no se admite para la validación de forma de contactos";

	public static final String MSG_ERROR_CONTACTO_NO_EXISTE = "No se encuentra la forma de contacto";
	public static final String MSG_ERROR_CONTACTO_NO_FOUND = "No se requiere al menos una forma de contacto para realizar la validación";


	public static final String[] LIST_VALIDA_PAISES_NOMBRE = { "ECUADOR" };
	public static final String[] LIST_VALIDA_FORMATO_TELEFONO = { "TELEFONO FIJO", "FAX", "TELEFONO TRASLADO" };
	public static final String[] LIST_VALIDA_FORMATO_TELEFONO_INTERNACIONAL = { "TELEFONO INTERNACIONAL" };
	public static final String[] LIST_VALIDA_FORMATO_TELEFONO_MOVIL = { "TELEFONO MOVIL" };
	public static final String[] LIST_VALIDA_FORMATO_CORREO = { "CORREO", "CORREO ELECTRONICO", "FACEBOOK" };

	public static final String EXP_REG_TELEFONO = "^(0[2-8]{1}[0-9]{7})$";
	public static final String MSG_VALIDACION_TELEFONO = "Telefono Fijo Incorrecto debe poseer codigo de area valido, No cumple el formato permitido : {{valor}}.";

	public static final String EXP_REG_TELEFONO_INTERNACIONAL = "^([0-9]{7,15})$";
	public static final String MSG_VALIDACION_TELEFONO_INTERNACIONAL = "Telefono Internacional Incorrecto, Solo debe ingresar entre 7 y 15 digitos, No cumple el formato permitido : {{valor}}.";

	public static final String EXP_REG_TELEFONO_MOVIL = "^(09[0-9]{8})$";
	public static final String MSG_VALIDACION_TELEFONO_MOVIL = "Telefono Movil Incorrecto, No cumple el formato permitido : {{valor}}.";

	public static final String EXP_REG_CORREO = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]{8}+$";
	public static final String MSG_VALIDACION_CORREO = "Correo Electronico Incorrecto, No cumple el formato permitido :  {{valor}}.";
	public static final String MSG_VALIDACION_DOMINIO = "Dominio del Correo Electronico Incorrecto, No cumple el formato permitido:  {{valor}}.";
	public static final String MSG_VALIDACION_CORREO_REQ = "Debe Ingresar al menos un Correo Electronico valido.";


	public static final String STATUS_ERROR = "ERROR";
	public static final String MSG_VALIDACION_CONTACTO = "No se pudo crear el prospecto corregir las siguientes validaciones de contacto: ";

	public static final String ROL_LISTA_NEGRA = "negra";
    public static final String ROL_LISTA_BLANCA = "blanca";
    public static final String DESCRIPCION_TIPO_ROL = "listaPersona";
    
    public static final String ESTADO_ACTIVO = "Activo";

}
