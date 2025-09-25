package config;

import domain.ReconnectDomainService;
import domain.contact.ContactDomainService;
import domain.contact.ContactRepository;
import domain.group.GroupDomainService;
import domain.group.GroupRepository;
import domain.interaction.InteractionDomainService;
import domain.interaction.InteractionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class RelationalDbConfig {
	
	@Bean
	public ContactDomainService contactDomainService(ContactRepository contactRepository,
			GroupRepository groupRepository) {
		return new ContactDomainService(contactRepository, groupRepository);
	}
	
	
	@Bean
	public GroupDomainService groupDomainService(GroupRepository groupRepository,
			ContactRepository contactRepository) {
		return new GroupDomainService(groupRepository, contactRepository);
	}
	
	
	@Bean
	public InteractionDomainService interactionDomainService(InteractionRepository interactionRepository,
			ContactRepository contactRepository) {
		return new InteractionDomainService(interactionRepository, contactRepository);
	}
	
	@Bean
	public ReconnectDomainService reconnectDomainService(InteractionDomainService interactionDomainService,
			ContactDomainService contactDomainService, GroupDomainService groupDomainService) {
		return new ReconnectDomainService(interactionDomainService, contactDomainService, groupDomainService);
	}
}
