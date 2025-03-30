package spring.config;

import domain.contact.ContactDomainService;
import domain.contact.ContactRepository;
import domain.group.GroupDomainService;
import domain.group.GroupRepository;
import domain.interaction.InteractionDomainService;
import domain.interaction.InteractionRepository;
import domain.ReconnectDomainService;
import framework.ContactFileRepository;
import framework.GroupFileRepository;
import framework.InteractionFileRepository;
import framework.ContactFileService;
import framework.GroupFileService;
import framework.InteractionFileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    private static final String DATA_DIR = "data";

    @Bean
    public ContactRepository contactRepository() {
        return new ContactFileRepository(DATA_DIR, "contacts.txt");
    }

    @Bean
    public GroupRepository groupRepository() {
        return new GroupFileRepository(DATA_DIR, "groups.txt");
    }

    @Bean
    public InteractionRepository interactionRepository() {
        return new InteractionFileRepository(DATA_DIR, "interactions.txt");
    }

    @Bean
    public ContactDomainService contactDomainService(ContactRepository contactRepository, GroupRepository groupRepository) {
        return new ContactFileService(contactRepository, groupRepository);
    }

    @Bean
    public GroupDomainService groupDomainService(GroupRepository groupRepository, ContactRepository contactRepository) {
        return new GroupFileService(groupRepository, contactRepository);
    }

    @Bean
    public InteractionDomainService interactionDomainService(InteractionRepository interactionRepository, ContactRepository contactRepository) {
        return new InteractionFileService(interactionRepository, contactRepository);
    }

    @Bean
    public ReconnectDomainService reconnectDomainService(ContactDomainService contactDomainService, 
                                                       GroupDomainService groupDomainService,
                                                       InteractionDomainService interactionDomainService) {
        return new ReconnectDomainService(interactionDomainService, contactDomainService, groupDomainService);
    }
} 