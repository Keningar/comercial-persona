package ec.telconet.persona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Clase utilizada para el despliegue del ms
 *
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@SpringBootApplication
@ComponentScan({ "ec.telconet" })
public class PersonaApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PersonaApplication.class, args);
	}
	
	/**
	 * Bean que sirve para agregar un servlet en los endpoint de las clases RestController
	 *
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 */
	@Bean
	public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean<DispatcherServlet> bean = new ServletRegistrationBean<DispatcherServlet>(dispatcherServlet, "/persona/*");
		bean.setAsyncSupported(true);
		bean.setName("persona");
		bean.setLoadOnStartup(1);
		return bean;
	}
}
