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

    public List<ReconnectModel> getOutOfTouchContactList() {
        return contactDomainService.getAll().stream().map(this::getContactInteraction).filter(ReconnectModel::isOutOfTouch).sorted(Comparator.comparing(ReconnectModel::getNextContactTimestamp)).toList();
    }

    @SneakyThrows
    private ReconnectModel getContactInteraction(Contact contact) {
        Group group = groupDomainService.get(contact.getGroup());
        List<Interaction> allInteractions = interactionDomainService.getAll(contact.getNickName());
        Long lastInteractionTimeStamp = allInteractions.isEmpty() ? 0L : allInteractions.get(0).getTimeStamp();
        return new ReconnectModel(contact.getNickName(), group.getName(), group.getFrequencyInDays(), lastInteractionTimeStamp);
    }
}
