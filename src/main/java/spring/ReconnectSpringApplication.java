package spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"spring", "config", "relationaldb"})
@EntityScan(basePackages = {"spring.security", "relationaldb.entity"})
public class ReconnectSpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ReconnectSpringApplication.class, args);
    }
}
