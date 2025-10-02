package relationaldb.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("prod")
@EnableJpaRepositories(basePackages = {"relationaldb.repository", "spring.security"})
@EntityScan(basePackages = {"relationaldb.entity", "spring.security"})
public class JpaRelationalDbConfig {
}
