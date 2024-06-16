package framework;

import java.io.IOException;

import domain.contact.ContactRepository;
import domain.interaction.Interaction;
import domain.interaction.InteractionDomainService;
import domain.interaction.InteractionRepository;

public class InteractionFileService extends InteractionDomainService {
    private final InteractionFileRepository interactionRepository;

    public InteractionFileService(final InteractionFileRepository interactionRepository, final ContactFileRepository contactRepository) {
        super(interactionRepository, contactRepository);
        this.interactionRepository = interactionRepository;
    }

    @Override
    public void add(final Interaction interaction) throws IOException {
        super.add(interaction);
        interactionRepository.commit();
    }

    @Override
    public void remove(final String id) throws IOException {
        super.remove(id);
        interactionRepository.commit();
    }

    @Override
    public void update(final Interaction interaction) throws IOException {
        super.update(interaction);
        interactionRepository.commit();
    }
}
