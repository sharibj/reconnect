package relationaldb.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import relationaldb.service.ContactService;
import relationaldb.service.GroupService;
import relationaldb.service.InteractionService;
import relationaldb.mapper.ContactMapper;
import relationaldb.mapper.GroupMapper;
import relationaldb.mapper.InteractionMapper;
import relationaldb.repository.JpaContactRepository;
import relationaldb.repository.JpaGroupRepository;
import relationaldb.repository.JpaInteractionRepository;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"relationaldb.repository", "spring.security"})
@EntityScan(basePackages = {"relationaldb.entity", "spring.security"})
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
