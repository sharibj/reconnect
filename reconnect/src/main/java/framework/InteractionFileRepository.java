package framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import domain.interaction.Interaction;
import domain.interaction.InteractionRepository;

public class InteractionFileRepository implements InteractionRepository {
    private static final String DELIMITER = ",";
    private final List<Interaction> interactions = new ArrayList<>();
    private final String filePath;
    private final String fileName;

    public InteractionFileRepository(final String filePath, final String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
        loadInteractions(filePath, fileName);
    }

    private void loadInteractions(String filePath, String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            lines = FileRepositoryUtils.readLines(filePath, fileName);
        } catch (IOException exception) {
            // Ignore Exceptions
        }
        lines.stream().map(this::lineToInteraction).filter(Objects::nonNull).forEach(interactions::add);
    }

    private Interaction lineToInteraction(String line) {
        List<String> tokens = FileRepositoryUtils.readTokens(line, DELIMITER);
        if (tokens.size() < 2) {
            // ignore faulty lines
            return null;
        }
        Interaction.InteractionBuilder interactionBuilder = Interaction.builder()
                .id(tokens.get(0)).contact(tokens.get(1));
        return tokens.size() >= 3 ? interactionBuilder.timeStamp(Long.parseLong(tokens.get(2))).build() : interactionBuilder.build();
    }

    private String interactionToLine(Interaction interaction) {
        return interaction.getId() + DELIMITER + interaction.getContact() + DELIMITER + interaction.getTimeStamp() + DELIMITER + interaction.getNotes();
    }


    @Override
    public List<Interaction> findAll() {
        return new ArrayList<>(interactions);
    }

    @Override
    public List<Interaction> findAll(final String contact) {
        return interactions.stream().filter(interaction -> interaction.getContact().equals(contact)).toList();
    }

    @Override
    public Optional<Interaction> find(final String id) {
        return interactions.stream().filter(interaction -> interaction.getId().equals(id)).findFirst();
    }

    @Override
    public Interaction save(final Interaction interaction) {
        delete(interaction.getId());
        interactions.add(interaction);
        return interaction;
    }

    @Override
    public Interaction delete(final String id) {
        Optional<Interaction> matchingInteraction = interactions.stream().filter(interaction -> id.equals(interaction.getId())).findFirst();
        matchingInteraction.ifPresent(interactions::remove);
        return matchingInteraction.orElse(null);
    }

    public void commit() throws IOException {
        List<String> lines = interactions.stream().map(this::interactionToLine).toList();
        FileRepositoryUtils.appendLines(lines, filePath, fileName);
    }
}
