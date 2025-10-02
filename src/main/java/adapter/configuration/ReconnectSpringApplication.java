package adapter.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class})
@ComponentScan(basePackages = {"adapter", "domain"})
@EnableJpaRepositories(basePackages = {"adapter.secondary.persistence.jpa.repository", "adapter.primary.http.security"})
@EntityScan(basePackages = {"adapter.primary.http.security", "adapter.secondary.persistence.jpa.entity"})
public class ReconnectSpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ReconnectSpringApplication.class, args);
    }
}
