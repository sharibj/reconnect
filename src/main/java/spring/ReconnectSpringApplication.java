package spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class})
@ComponentScan(basePackages = {"spring", "config", "relationaldb", "domain"})
@EnableJpaRepositories(basePackages = {"relationaldb.repository", "spring.security"})
@EntityScan(basePackages = {"spring.security", "relationaldb.entity"})
public class ReconnectSpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ReconnectSpringApplication.class, args);
    }
}
