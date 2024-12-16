package net.devgrr.springdemo.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Value("${jwt.refresh.header}")
  private String refreshHeader;

  @Bean
  public OpenAPI openAPI() {
    OpenAPI openAPI = new OpenAPI();
    openAPI.info(new Info().title("DEMO API").version("1.0").description("DEMO API Documentation"));
    openAPI.addSecurityItem(new SecurityRequirement().addList(accessHeader));
    openAPI.addSecurityItem(new SecurityRequirement().addList(refreshHeader));
    openAPI.components(
        new Components()
            .addSecuritySchemes(
                accessHeader,
                new SecurityScheme()
                    .name(accessHeader)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .in(SecurityScheme.In.HEADER)
                    .bearerFormat("JWT"))
            .addSecuritySchemes(
                refreshHeader,
                new SecurityScheme()
                    .name(refreshHeader)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .in(SecurityScheme.In.HEADER)
                    .bearerFormat("JWT")));
    return openAPI;
  }
}
