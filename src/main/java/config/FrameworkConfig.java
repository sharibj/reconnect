package config;

import domain.contact.ContactDomainService;
import domain.contact.ContactRepository;
import domain.group.GroupDomainService;
import domain.group.GroupRepository;
import domain.interaction.InteractionDomainService;
import domain.interaction.InteractionRepository;
import framework.ContactFileRepository;
import framework.GroupFileRepository;
import framework.InteractionFileRepository;
import framework.ContactFileService;
import framework.GroupFileService;
import framework.InteractionFileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FrameworkConfig {
    private static final String DATA_DIR = "data";

    @Bean
    public ContactFileRepository contactRepository() {
        return new ContactFileRepository(DATA_DIR, "contacts.txt");
    }

    @Bean
    public GroupFileRepository groupRepository() {
        return new GroupFileRepository(DATA_DIR, "groups.txt");
    }

    @Bean
    public InteractionFileRepository interactionRepository() {
        return new InteractionFileRepository(DATA_DIR, "interactions.txt");
    }

    @Bean
    public ContactDomainService contactDomainService(ContactFileRepository contactRepository, GroupFileRepository groupRepository) {
        return new ContactFileService(contactRepository, groupRepository);
    }

    @Bean
    public GroupDomainService groupDomainService(GroupFileRepository groupRepository) {
        return new GroupFileService(groupRepository);
    }

    @Bean
    public InteractionDomainService interactionDomainService(InteractionFileRepository interactionRepository, ContactFileRepository contactRepository) {
        return new InteractionFileService(interactionRepository, contactRepository);
    }
} 