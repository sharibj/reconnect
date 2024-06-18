package framework;

import java.io.IOException;

import domain.interaction.Interaction;
import domain.interaction.InteractionDomainService;

public class InteractionFileService extends InteractionDomainService {
    private final InteractionFileRepository interactionFileRepository;

    public InteractionFileService(final InteractionFileRepository interactionFileRepository, final ContactFileRepository contactRepository) {
        super(interactionFileRepository, contactRepository);
        this.interactionFileRepository = interactionFileRepository;
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
