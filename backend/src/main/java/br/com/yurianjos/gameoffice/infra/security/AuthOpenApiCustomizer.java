package br.com.yurianjos.gameoffice.infra.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthOpenApiCustomizer implements OpenApiCustomizer {

    static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Override
    public void customise(OpenAPI openApi) {

        openApi.getComponents().addSecuritySchemes(
                SECURITY_SCHEME_NAME,
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        openApi.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}