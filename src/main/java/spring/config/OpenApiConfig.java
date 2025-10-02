package spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reconnectOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("Reconnect API")
                        .description("API for managing contacts, interactions, and groups. " +
                                   "To authenticate: 1) Use the /api/auth/login endpoint to get a JWT token, " +
                                   "2) Click the 'Authorize' button and enter 'Bearer <your-token>'")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Sharib Jafari")
                                .email("jafarisharib@gmail.com")))
                .addServersItem(new Server().url("/").description("Default Server URL"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Authorization header using the Bearer scheme. " +
                                                   "Enter 'Bearer' [space] and then your token in the text input below. " +
                                                   "Example: 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'")
                        )
                );
    }
}
