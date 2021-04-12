package ec.telconet.persona.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerDocConfig {
	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Documentación API del microservicio ms-core-com-persona")
				.description("Documentación Centraliza usando Spring Boot y spring-fox swagger 2 ")
				.termsOfServiceUrl("").version("1.0")
				.contact(new Contact("Jose Vinueza", "", "")).build();
	}

	@Bean
	public Docket configureControllerPackageAndConvertors() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("ec.telconet.persona.controller")).build()
				.apiInfo(apiInfo());
	}
}
