package ec.telconet.persona.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
