package org.example.first.groundingappapis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("1st Grounding Service API")
                        .description("1st Grounding Service LIST")
                        .version("0.0.1"))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .servers(Arrays.asList(
                        new io.swagger.v3.oas.models.servers.Server().url("http://3.39.108.39:8033").description("Production server"),
                        new io.swagger.v3.oas.models.servers.Server().url("http://localhost:8033").description("Local development server")
                ));
    }
}
