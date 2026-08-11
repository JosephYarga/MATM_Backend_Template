package bf.gov.matm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API MATM - Burkina Faso")
                        .version("1.0.0")
                        .description("API REST pour le Ministère de l’Administration territoriale et de la Mobilité du Burkina Faso. " +
                                "Cette API permet la gestion des utilisateurs, articles, projets, documents et contacts.")
                        .contact(new Contact()
                                .name("MATM - Direction des Systèmes d'Information")
                                .email("contact@matm.gov.bf")
                                .url("https://www.matm.gov.bf"))
                        .license(new License()
                                .name("Gouvernement du Burkina Faso")
                                .url("https://www.gouvernement.gov.bf")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Serveur de développement")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Entrez votre token JWT")));
    }
}
