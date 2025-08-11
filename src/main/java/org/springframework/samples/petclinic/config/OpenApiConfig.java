package org.springframework.samples.petclinic.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Pet Clinic API", version = "1.0", description = "Documentation for Pet Clinic API"))
public class OpenApiConfig {
}
