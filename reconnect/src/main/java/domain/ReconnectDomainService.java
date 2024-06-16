package domain;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import domain.contact.Contact;
import domain.contact.ContactDomainService;
import domain.group.Group;
import domain.group.GroupDomainService;
import domain.interaction.Interaction;
import domain.interaction.InteractionDomainService;

public class ReconnectDomainService {
    private final InteractionDomainService interactionDomainService;
    private final ContactDomainService contactDomainService;
    private final GroupDomainService groupDomainService;

    public ReconnectDomainService(final InteractionDomainService interactionDomainService, final ContactDomainService contactDomainService, GroupDomainService groupDomainService) {
        this.interactionDomainService = interactionDomainService;
        this.contactDomainService = contactDomainService;
        this.groupDomainService = groupDomainService;
    }

    public List<Contact> getOrderedList() {
        Set<Contact> contacts = contactDomainService.getAll();
        return contacts.stream().sorted(this::sortByInteraction).toList();
    }

    /**
     *TODO add proper comment
     * @param contact1
     * @param contact2
     * @return negative, zero , positive
     * if contact 1 is
     * less, equal greater
     * than contact 2
     */
    private int sortByInteraction(Contact contact1, Contact contact2) {
        try {
            List<Interaction> contact1Interactions = interactionDomainService.getAll(contact1.getNickName());
            List<Interaction> contact2Interactions = interactionDomainService.getAll(contact2.getNickName());
            if (contact1Interactions.isEmpty() && contact2Interactions.isEmpty()) {
                return 0;
            } else if (contact1Interactions.isEmpty()) {
                return -1;
            } else if (contact2Interactions.isEmpty()) {
                return 1;
            } else {
                Date date1 = new Date(contact1Interactions.getFirst().getTimeStamp());
                Date date2 = new Date(contact2Interactions.getFirst().getTimeStamp());
                Integer frequency1 = groupDomainService.get(contact1.getGroup()).getFrequencyInDays();
                Integer frequency2 = groupDomainService.get(contact2.getGroup()).getFrequencyInDays();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date1);
                calendar1.add(Calendar.DATE, frequency1);

                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date2);
                calendar2.add(Calendar.DATE, frequency2);

                if (calendar1.before(calendar2)) {
                    return -1;
                } else if (calendar1.after(calendar2)) {
                    return 1;
                } else {
                    return 0;
                }
            }

        } catch (IOException e) {
            //TODO
        }
        return 0;
    }

    public List<Contact> getOutOfTouch() {
        Set<Contact> contacts = contactDomainService.getAll();
        return contacts.stream().filter(this::isOutOfTouch).toList();
    }

    private boolean isOutOfTouch(Contact contact) {
        try {
            List<Interaction> allInteractions = interactionDomainService.getAll(contact.getNickName());
            if (allInteractions.isEmpty()) {
                return true;
            }
            Group group = groupDomainService.get(contact.getGroup());
            Interaction latestInteraction = allInteractions.getFirst();
            Date interactionDate = new Date(latestInteraction.getTimeStamp());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(interactionDate);
            calendar.add(Calendar.DATE, group.getFrequencyInDays());
            return calendar.before(Calendar.getInstance().getTime());
        } catch (IOException e) {
            //TODO
        }
        return true;
    }
}
