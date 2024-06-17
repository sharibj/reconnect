package domain;

import java.util.Comparator;
import java.util.List;

import domain.contact.Contact;
import domain.contact.ContactDomainService;
import domain.group.Group;
import domain.group.GroupDomainService;
import domain.interaction.Interaction;
import domain.interaction.InteractionDomainService;
import lombok.SneakyThrows;

public class ReconnectDomainService {
    private final InteractionDomainService interactionDomainService;
    private final ContactDomainService contactDomainService;
    private final GroupDomainService groupDomainService;

    public ReconnectDomainService(final InteractionDomainService interactionDomainService, final ContactDomainService contactDomainService, GroupDomainService groupDomainService) {
        this.interactionDomainService = interactionDomainService;
        this.contactDomainService = contactDomainService;
        this.groupDomainService = groupDomainService;
    }

    public List<ContactInteraction> getOutOfTouchContactList() {
        return contactDomainService.getAll().stream().map(this::getContactInteraction).filter(ContactInteraction::isOutOfTouch).sorted(Comparator.comparing(ContactInteraction::getNextContactTimestamp)).toList();
    }

    @SneakyThrows
    private ContactInteraction getContactInteraction(Contact contact) {
        Group group = groupDomainService.get(contact.getGroup());
        List<Interaction> allInteractions = interactionDomainService.getAll(contact.getNickName());
        Long lastInteractionTimeStamp = allInteractions.isEmpty() ? 0 : allInteractions.getFirst().getTimeStamp();
        return new ContactInteraction(contact.getNickName(), group.getName(), group.getFrequencyInDays(), lastInteractionTimeStamp);
    }
}
