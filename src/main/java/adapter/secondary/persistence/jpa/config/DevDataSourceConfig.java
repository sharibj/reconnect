package adapter.secondary.persistence.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("dev-old")  // Disabled - using SingleDatabaseConfig instead
@EnableJpaRepositories(basePackages = {
    "adapter.secondary.persistence.jpa.repository",
    "adapter.primary.http.security"
})
@EntityScan(basePackages = {
    "adapter.secondary.persistence.jpa.entity",
    "adapter.primary.http.security"
})
public class DevDataSourceConfig {
    // This configuration is disabled - using SingleDatabaseConfig for multi-tenant architecture
}