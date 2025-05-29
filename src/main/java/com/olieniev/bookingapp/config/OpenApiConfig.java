package com.olieniev.bookingapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    public static final String BEARER_AUTH_SCHEME_NAME = "BearerAuth";
    public static final String BEARER_SECURITY_SCHEME_NAME = "bearer";
    public static final String JWT_BEARER_FORMAT = "JWT";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
      .components(new Components().addSecuritySchemes(BEARER_AUTH_SCHEME_NAME,
          new SecurityScheme()
          .type(SecurityScheme.Type.HTTP)
          .scheme(BEARER_SECURITY_SCHEME_NAME)
          .bearerFormat(JWT_BEARER_FORMAT)))
      .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH_SCHEME_NAME));
    }
}
