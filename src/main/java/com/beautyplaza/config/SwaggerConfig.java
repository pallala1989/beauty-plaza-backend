package com.beautyplaza.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for SpringDoc OpenAPI (Swagger UI).
 * This class defines custom information for your API documentation,
 * such as title, description, version, contact details, and security schemes.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Defines the custom OpenAPI bean.
     * This method configures the API information and security components
     * that will be displayed in the Swagger UI.
     * @return A customized OpenAPI object.
     */
    @Bean
    public OpenAPI beautyPlazaOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Beauty Plaza API") // Set the API title
                        .description("API documentation for the Beauty Plaza application. Manage users, services, technicians, appointments, loyalty points, and application settings.") // Set API description
                        .version("v1.0.0") // Set API version
                        .contact(new Contact().name("Beauty Plaza Support").email("support@beautyplaza.com")) // Set contact information
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))) // Set license information
                .externalDocs(new ExternalDocumentation() // Add external documentation link
                        .description("Beauty Plaza Wiki Documentation")
                        .url("https://beautyplaza.wiki.github.org/docs"))
                .components(new Components() // Define security schemes for JWT authentication
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT authentication token. Enter 'Bearer {token}'"))); // Description for JWT input
    }
}
