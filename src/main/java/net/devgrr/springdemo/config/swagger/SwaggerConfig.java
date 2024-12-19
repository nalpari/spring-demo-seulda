package net.devgrr.springdemo.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.HashMap;
import java.util.Map;
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

    // Security Scheme
    openAPI.addSecurityItem(new SecurityRequirement().addList(accessHeader));
    openAPI.addSecurityItem(new SecurityRequirement().addList(refreshHeader));
    openAPI.components(createJwtComponents());

    // login endpoint
    openAPI.path("/login", createLoginPathItem());

    return openAPI;
  }

  private Components createJwtComponents() {
    return new Components()
        .addSecuritySchemes(
            accessHeader,
            new SecurityScheme()
                .name(accessHeader)
                .description("Format: {access_token}")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT"))
        .addSecuritySchemes(
            refreshHeader,
            new SecurityScheme()
                .name(refreshHeader)
                .description("Format: Bearer {refresh_token}")
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER));
  }

  private PathItem createLoginPathItem() {
    Map<String, Schema> properties = new HashMap<>();
    properties.put("userId", new StringSchema());
    properties.put("password", new StringSchema());
    Schema<?> schema = new ObjectSchema().properties(properties);
    RequestBody requestBody =
        new RequestBody()
            .content(
                new Content()
                    .addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().schema(schema)));
    Operation operation =
        new Operation()
            .requestBody(requestBody)
            .responses(
                new ApiResponses()
                    .addApiResponse("200", new ApiResponse().description("OK"))
                    .addApiResponse("400", new ApiResponse().description("Bad Request")))
            .addTagsItem("login");
    return new PathItem().post(operation);
  }
}
