package ec.telconet.persona;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.DispatcherServlet;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;

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
	
	 @Value("${jaeger.tracer.host}")
	  private String host;
	  
	  @Value("${jaeger.tracer.port}")
	  private int port;
	  
	  @Value("${jaeger.tracer.logspan}")
	  private boolean logSpan;
	  
	  @Value("${jaeger.tracer.nametracer}")
	  private String nametracer;
	  
	  
	 /**
	   * Bean configuracion opentrancing
	   *
	   * @author Jose Vinueza <mailto:jdvinueza@telconet.ec>
	   * @version 1.0
	   * @since 11/03/2021
	   */
	  @Bean
	  public Tracer tracer() {
	      Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
	              .withType(ConstSampler.TYPE)
	              .withParam(1);

	      Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
	              .withLogSpans(logSpan)
	              .withSender(io.jaegertracing.Configuration.SenderConfiguration.fromEnv()
	            		      .withAgentHost(host)
	            		      .withAgentPort(port));

	      Configuration config = new Configuration(nametracer)
	              .withSampler(samplerConfig)
	              .withReporter(reporterConfig);

	      return config.getTracer();
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
