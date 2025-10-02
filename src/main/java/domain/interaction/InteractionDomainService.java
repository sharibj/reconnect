package domain.interaction;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import domain.contact.ContactRepository;

@Service
public class InteractionDomainService {

    InteractionRepository interactionRepository;
    ContactRepository contactRepository;

    @Autowired
    public InteractionDomainService(final InteractionRepository interactionRepository, final ContactRepository contactRepository) {
        this.interactionRepository = interactionRepository;
        this.contactRepository = contactRepository;
    }

    public void add(final Interaction interaction) throws IOException {
        validateInteractionDoesNotExist(interaction.getId());
        validateContactExists(interaction.getContact());
        interactionRepository.save(interaction);
    }

    public void remove(final String id) throws IOException {
        validateInteractionExists(id);
        interactionRepository.delete(id);
    }

    public void update(final Interaction interaction) throws IOException {
        validateContactExists(interaction.getContact());
        if (!get(interaction.getId()).equals(interaction)) {
            interactionRepository.save(interaction);
        }
    }

    public Interaction get(final String interactionId) throws IOException {
        return interactionRepository
                .find(interactionId)
                .orElseThrow(() -> new IOException("Interaction not found: " + interactionId));
    }

    public List<Interaction> getAll(final String contact) throws IOException {
        validateContactExists(contact);
        return (new HashSet<>(interactionRepository.findAll(contact))).stream()
                .sorted(Comparator.comparing(Interaction::getTimeStamp).reversed())
                .toList();
    }

    public List<Interaction> getAll() {
        return (new HashSet<>(interactionRepository.findAll())).stream()
                .sorted(Comparator.comparing(Interaction::getTimeStamp).reversed())
                .toList();
    }

    private void validateContactExists(final String contact) throws IOException {
        if (isNotBlank(contact) && contactRepository.find(contact).isEmpty()) {
            throw new IOException("Contact not found: " + contact);
        }
    }

    private void validateInteractionDoesNotExist(final String interactionId) throws IOException {
        if (interactionRepository.find(interactionId).isPresent()) {
            throw new IOException("Interaction found: " + interactionId);
        }
    }

    private void validateInteractionExists(final String interactionId) throws IOException {
        if (interactionRepository.find(interactionId).isEmpty()) {
            throw new IOException("Interaction not found: " + interactionId);
        }
    }

    private boolean isNotBlank(final String str) {
        return null != str && !str.isBlank();
    }
}
