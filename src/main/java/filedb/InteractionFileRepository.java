package filedb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import domain.interaction.Interaction;
import domain.interaction.InteractionRepository;

public class InteractionFileRepository implements InteractionRepository {
    private static final String DELIMITER = "±";
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

        Interaction.InteractionBuilder interactionBuilder = Interaction.builder();
        for (int i = 0; i < tokens.size(); i++) {
            interactionBuilder = build(i, tokens.get(i), interactionBuilder);
        }
        return interactionBuilder.build();
    }

    private Interaction.InteractionBuilder build(Integer tokenNumber, String token, Interaction.InteractionBuilder builder) {
        return switch (tokenNumber) {
            case 0 -> builder.id(token);
            case 1 -> builder.contact(token);
            case 2 -> builder.timeStamp("null".equals(token) ? new java.util.Date().getTime() : Long.parseLong(token));
            case 3 -> builder.notes(unescapeNewlines(token));
            default -> builder;
        };
    }

    private String interactionToLine(Interaction interaction) {
        return interaction.getId() + DELIMITER + interaction.getContact() + DELIMITER + interaction.getTimeStamp() + DELIMITER + escapeNewlines(interaction.getNotes());
    }

    private String escapeNewlines(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\n", "\\n").replace("\r", "\\r");
    }

    private String unescapeNewlines(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.replace("\\n", "\n").replace("\\r", "\r");
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
