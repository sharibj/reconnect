package config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reconnectOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reconnect API")
                        .description("API for managing contacts, interactions, and groups")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Sharib Jafari")
                                .email("jafarisharib@gmail.com")));
    }
} 