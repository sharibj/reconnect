package adapter.secondary.persistence.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@Profile({"dev", "prod"})
@EnableJpaRepositories(basePackages = {
    "adapter.secondary.persistence.jpa.repository",
    "adapter.primary.http.security"
})
@EntityScan(basePackages = {
    "adapter.secondary.persistence.jpa.entity",
    "adapter.primary.http.security"
})
public class SingleDatabaseConfig {
    // This configuration enables both tenant and domain repositories in single database mode
}