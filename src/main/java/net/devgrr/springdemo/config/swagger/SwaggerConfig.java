package net.devgrr.springdemo.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		OpenAPI openAPI = new OpenAPI();
		openAPI.info(
				new Info().title("DEMO API").version("1.0").description("DEMO API Documentation"));
		return openAPI;
	}
}
