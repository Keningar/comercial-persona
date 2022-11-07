package ec.telconet.persona.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.HashMap;
import java.util.Hashtable;

@Component
public class PersonaUtils {

	Logger log = LogManager.getLogger(this.getClass());

	private HttpServletRequest request;

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Método para obtener la ip del cliente
	 *
	 * @return String Ip Cliente
	 *
	 * @author Calros Caguana
	 * @version 1.0
	 * @since 18/08/2021
	 */
	public String getClientIp() {
		String remoteAddr;
		String ipXRealIp = request.getHeader("X-Real-IP");
		String ipXForwardedFor = request.getHeader("X-Forwarded-For");
		if (ipXRealIp != null) {
			return ipXRealIp;
		}
		if (ipXForwardedFor != null) {
			String[] Ips = ipXForwardedFor.split(",");
			return Ips[0];
		}
		remoteAddr = request.getRemoteAddr();
		return remoteAddr;
	}

	/**
	 * Metodo para la verificación de existencia del dominio del servidor de correo.
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 28/06/2022
	 */

	public Boolean verificarMailDNS(String dominioEmail) {
		List<String> records = this.resolveDNS(dominioEmail,true); 
		return (records != null && records.size() > 0) ; 
	}

	/**
	 * Metodo para validar expresiones regulares
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 28/06/2022
	 */
	public static boolean isValidaExpReg(String expReg, String value) {
		Pattern pattern = Pattern.compile(expReg);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	/**
	 * Metodo para validar lista de contactos
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 28/06/2022
	 */

	public static String validadorContacto(
			String contactoNombre,
			String contactoValor,
			String[] listaFormaContacto,
			String expReg,
			String mensaje) {

		String mensajeValidacion = "";
		for (String item : listaFormaContacto) {

			if (contactoNombre.indexOf(item) != -1) {
				boolean valido = !isValidaExpReg(expReg, contactoValor);
				if (valido) {
					mensajeValidacion = mensaje.replace("{{valor}}", contactoValor);

				}
			}

		}

		return mensajeValidacion;
	}

 

	/**
	 * Resolve MX DNS.
	 * 
	 * @author jacarrillo <mailto:jacarrillo@telconet.ec>
	 * @version 1.0
	 * @since 28/06/2022
	 * 
	 * @param hostname hostname
	 * @return list of MXs
	 */
	public List<String> resolveDNS(String hostname, boolean mx) {
		List<String> result = new ArrayList<String>();

		try {

			final Properties jndiProperties = new Properties();

			log.trace("DNS validation: resolving DNS for " + hostname + " " + (mx ? "(MX)" : "(A/CNAME)"));

			jndiProperties.setProperty("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
			jndiProperties.setProperty("com.sun.jndi.dns.timeout.initial", "5000"); /* quite short... too short? */
			jndiProperties.setProperty("com.sun.jndi.dns.timeout.retries", "1");

			DirContext ictx =  new InitialDirContext( jndiProperties);
			String[] ids = (mx ? new String[] { "MX" } : new String[] { "A", "CNAME" });
			Attributes attrs = ictx.getAttributes(hostname, ids);

			if (mx) {
				Attribute attr = attrs.get("MX");

				if (attr != null && attr.size() > 0) {
					NamingEnumeration e = attr.getAll();

					while (e.hasMore()) {
						String mxs = (String) e.next();
						String f[] = mxs.split("\\s+");

						for (int i = 0; i < f.length; i++) {
							if (f[i].endsWith(".")) {
								result.add(f[i].substring(0, f[i].length() - 1));
							}
						}
					}
					return result;
				} else {
					log.trace("DNS validation: DNS query of '" + hostname + "' failed");
					return null;
				}
			} else {
				Attribute attr = attrs.get("A");
				if (attr != null && attr.size() > 0) {
					NamingEnumeration e = attr.getAll();
					while (e.hasMore()) {
						result.add((String) e.next());
					}
					return result;
				} else {
					attr = attrs.get("CNAME");
					if (attr != null && attr.size() > 0) {
						NamingEnumeration e = attr.getAll();
						while (e.hasMore()) {
							String h = (String) e.next();

							if (h.endsWith(".")) {
								h = h.substring(0, h.lastIndexOf('.'));
							}
							log.trace("DNS validation: recursing on CNAME record towards host " + h);
							result.addAll(resolveDNS(h, false));
						}
						return result;
					} else {
						log.trace("DNS validation: DNS query of '" + hostname + "' failed");
						return null;
					}
				}
			}
		} catch (NamingException ne) {
			log.trace("DNS validation: DNS MX query failed: " + ne.getMessage());
			return null;
		}
	}

}
