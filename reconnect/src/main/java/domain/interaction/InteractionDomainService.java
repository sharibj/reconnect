package domain.interaction;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import domain.contact.ContactRepository;

public class InteractionDomainService {

    InteractionRepository interactionRepository;
    ContactRepository contactRepository;

    public InteractionDomainService(final InteractionRepository interactionRepository, final ContactRepository contactRepository) {
        this.interactionRepository = interactionRepository;
        this.contactRepository = contactRepository;
    }

    public void add(final Interaction interaction) throws IOException {
        if (interactionRepository.find(interaction.getId()).isPresent()) {
            throw new IOException("Interaction with id = " + interaction.getId() + " already exists.");
        }
        if (isNotBlank(interaction.getContact()) && contactRepository.find(interaction.getContact()).isEmpty()) {
            throw new IOException("Contact with nickname = " + interaction.getContact() + " does not exist.");
        }
        interactionRepository.save(interaction);
    }

    private boolean isNotBlank(final String str) {
        return null != str && !str.isBlank();
    }

    public void remove(final String id) throws IOException {
        if (interactionRepository.find(id).isEmpty()) {
            throw new IOException("Interaction with id = " + id + " does not exist");
        }
        interactionRepository.delete(id);
    }

    public void update(final Interaction interaction) throws IOException {
        if (isNotBlank(interaction.getContact()) && contactRepository.find(interaction.getContact()).isEmpty()) {
            throw new IOException("Contact with nickname = " + interaction.getContact() + " does not exist.");
        }
        Interaction existingInteraction = get(interaction.getId());
        if (existingInteraction.equals(interaction)) {
            return;
        }
        interactionRepository.save(interaction);
    }

    public Interaction get(final String id) throws IOException {
        return interactionRepository
                .find(id)
                .orElseThrow(() -> new IOException("Interaction with id = " + id + " does not exist."));
    }

    public List<Interaction> getAll(final String contact) throws IOException {
        if (contactRepository.find(contact).isEmpty()) {
            throw new IOException("Contact with nickname = " + contact + " does not exist.");
        }
        //TODO add test for sorting
        return (new HashSet<>(interactionRepository.findAll(contact))).stream().sorted(Comparator.comparing(Interaction::getTimeStamp).reversed()).toList();
    }

    public List<Interaction> getAll() {
        return (new HashSet<>(interactionRepository.findAll())).stream().sorted(Comparator.comparing(Interaction::getTimeStamp).reversed()).toList();
    }

}
