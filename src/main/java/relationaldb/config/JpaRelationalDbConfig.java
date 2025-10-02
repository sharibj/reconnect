package relationaldb.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@EntityScan(basePackages = {"relationaldb.entity", "spring.security"})
public class JpaRelationalDbConfig {
}
