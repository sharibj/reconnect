package adapter.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class})
@ComponentScan(basePackages = {"adapter", "domain"})
@EnableAspectJAutoProxy
public class ReconnectSpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ReconnectSpringApplication.class, args);
    }
}
