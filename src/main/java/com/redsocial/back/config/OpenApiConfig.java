package com.redsocial.back.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de la documentación OpenAPI (Swagger).
 * Define la información general de la API y el esquema de seguridad JWT Bearer.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura la instancia de OpenAPI con metadatos de la API y el esquema de autenticación Bearer.
     *
     * @return instancia configurada de OpenAPI
     */
    @Bean
    public OpenAPI configuracionOpenApi() {
        final String nombreEsquemaSeguridad = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Documentación API Red Social")
                        .version("1.0.0")
                        .description("Documentación de la API del Backend de Red Social (Microservicio)"))
                .addSecurityItem(new SecurityRequirement().addList(nombreEsquemaSeguridad))
                .components(new Components()
                        .addSecuritySchemes(nombreEsquemaSeguridad,
                                new SecurityScheme()
                                        .name(nombreEsquemaSeguridad)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
