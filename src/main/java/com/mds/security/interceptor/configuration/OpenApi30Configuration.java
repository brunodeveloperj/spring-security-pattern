package com.mds.security.interceptor.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3.0 configuration that registers a global {@code bearerAuth}
 * security scheme (HTTP Bearer with JWT format) and populates the API
 * title and version from Spring application properties.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "${spring.application.name}", version = "${spring.application.version}"))
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApi30Configuration {
}
