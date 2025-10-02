package adapter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import domain.contact.ContactDomainService;
import domain.group.GroupDomainService;
import domain.interaction.InteractionDomainService;
import domain.ReconnectDomainService;
import adapter.secondary.persistence.file.ContactFileService;
import adapter.secondary.persistence.file.GroupFileService;
import adapter.secondary.persistence.file.InteractionFileService;
import adapter.secondary.persistence.file.multitenant.TenantContactFileRepository;
import adapter.secondary.persistence.file.multitenant.TenantGroupFileRepository;
import adapter.secondary.persistence.file.multitenant.TenantInteractionFileRepository;

@Configuration
@Profile("dev")
public class FiledbConfig {

    @Bean
    @Scope("prototype") // New instance per request to ensure tenant isolation
    public TenantContactFileRepository contactRepository() {
        return new TenantContactFileRepository();
    }

    @Bean
    @Scope("prototype")
    public TenantGroupFileRepository groupRepository() {
        return new TenantGroupFileRepository();
    }

    @Bean
    @Scope("prototype")
    public TenantInteractionFileRepository interactionRepository() {
        return new TenantInteractionFileRepository();
    }

    @Bean
    @Scope("prototype")
    public ContactDomainService contactDomainService() {
        return new ContactDomainService(contactRepository(), groupRepository());
    }

    @Bean
    @Scope("prototype")
    public GroupDomainService groupDomainService() {
        return new GroupDomainService(groupRepository(), contactRepository());
    }

    @Bean
    @Scope("prototype")
    public InteractionDomainService interactionDomainService() {
        return new InteractionDomainService(interactionRepository(), contactRepository());
    }

    @Bean
    @Scope("prototype")
    public ReconnectDomainService reconnectDomainService() {
        return new ReconnectDomainService(interactionDomainService(), contactDomainService(), groupDomainService());
    }
}