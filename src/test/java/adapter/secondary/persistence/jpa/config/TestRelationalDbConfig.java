package adapter.secondary.persistence.jpa.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import adapter.secondary.persistence.jpa.service.ContactService;
import adapter.secondary.persistence.jpa.service.GroupService;
import adapter.secondary.persistence.jpa.service.InteractionService;
import adapter.secondary.persistence.jpa.mapper.ContactMapper;
import adapter.secondary.persistence.jpa.mapper.GroupMapper;
import adapter.secondary.persistence.jpa.mapper.InteractionMapper;
import adapter.secondary.persistence.jpa.repository.JpaContactRepository;
import adapter.secondary.persistence.jpa.repository.JpaGroupRepository;
import adapter.secondary.persistence.jpa.repository.JpaInteractionRepository;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"adapter.secondary.persistence.jpa.repository", "adapter.primary.http.security"})
@EntityScan(basePackages = {"adapter.secondary.persistence.jpa.entity", "adapter.primary.http.security"})
public class TestRelationalDbConfig {

    @Bean
    public ContactMapper contactMapper() {
        return new ContactMapper();
    }

    @Bean
    public GroupMapper groupMapper() {
        return new GroupMapper();
    }

    @Bean
    public InteractionMapper interactionMapper() {
        return new InteractionMapper();
    }

    @Bean
    public ContactService contactService(JpaContactRepository repository, ContactMapper mapper) {
        return new ContactService(repository, mapper);
    }

    @Bean
    public GroupService groupService(JpaGroupRepository repository, GroupMapper mapper) {
        return new GroupService(repository, mapper);
    }

    @Bean
    public InteractionService interactionService(JpaInteractionRepository repository, InteractionMapper mapper) {
        return new InteractionService(repository, mapper);
    }
}
