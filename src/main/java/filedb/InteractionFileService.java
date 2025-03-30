package filedb;

import java.io.IOException;

import domain.interaction.Interaction;
import domain.interaction.InteractionDomainService;
import domain.interaction.InteractionRepository;
import domain.contact.ContactRepository;

public class InteractionFileService extends InteractionDomainService {
    private final InteractionFileRepository interactionFileRepository;

    public InteractionFileService(final InteractionRepository interactionRepository, final ContactRepository contactRepository) {
        super(interactionRepository, contactRepository);
        this.interactionFileRepository = (InteractionFileRepository) interactionRepository;
    }

    @Override
    public void add(final Interaction interaction) throws IOException {
        super.add(interaction);
        interactionFileRepository.commit();
    }

    @Override
    public void remove(final String id) throws IOException {
        super.remove(id);
        interactionFileRepository.commit();
    }

    @Override
    public void update(final Interaction interaction) throws IOException {
        super.update(interaction);
        interactionFileRepository.commit();
    }
}
