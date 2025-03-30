package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import domain.contact.ContactDomainService;
import domain.group.GroupDomainService;
import domain.interaction.InteractionDomainService;
import filedb.ContactFileRepository;
import filedb.ContactFileService;
import filedb.GroupFileRepository;
import filedb.GroupFileService;
import filedb.InteractionFileRepository;
import filedb.InteractionFileService;

@Configuration
public class FiledbConfig {
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
    public ContactDomainService contactDomainService(ContactFileRepository contactRepository,
            GroupFileRepository groupRepository) {
        return new ContactFileService(contactRepository, groupRepository);
    }

    @Bean
    public GroupDomainService groupDomainService(GroupFileRepository groupRepository,
            ContactFileRepository contactRepository) {
        return new GroupFileService(groupRepository, contactRepository);
    }

    @Bean
    public InteractionDomainService interactionDomainService(InteractionFileRepository interactionRepository,
            ContactFileRepository contactRepository) {
        return new InteractionFileService(interactionRepository, contactRepository);
    }
}