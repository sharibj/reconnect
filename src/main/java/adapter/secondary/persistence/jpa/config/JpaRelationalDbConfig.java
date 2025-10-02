package adapter.secondary.persistence.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@EntityScan(basePackages = {"adapter.secondary.persistence.jpa.entity", "adapter.primary.http.security"})
public class JpaRelationalDbConfig {
}
