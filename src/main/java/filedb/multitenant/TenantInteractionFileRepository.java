package filedb.multitenant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import domain.interaction.Interaction;
import domain.interaction.InteractionRepository;
import filedb.FileRepositoryUtils;
import lombok.SneakyThrows;

public class TenantInteractionFileRepository implements InteractionRepository {
	
	private static final String DELIMITER = "±";
	private static final String FILE_NAME = "interactions.txt";
	private final List<Interaction> interactions = new ArrayList<>();
	private boolean loaded = false;
	
	
	public TenantInteractionFileRepository() {
		// Don't load interactions during construction - load them lazily when needed
	}
	
	
	@SneakyThrows
	private void ensureLoaded() {
		if (!loaded) {
			loadInteractions();
			loaded = true;
		}
	}
	
	
	@SneakyThrows
	private void loadInteractions() {
		interactions.clear();
		try {
			FileRepositoryUtils.readLines(TenantFileManager.getTenantDirectory(), FILE_NAME).stream()
					.map(this::lineToInteraction)
					.filter(Objects::nonNull)
					.forEach(interactions::add);
		} catch (IOException e) {
			// File doesn't exist yet for this tenant - that's OK
		}
	}
	
	
	private Interaction lineToInteraction(String line) {
		List<String> tokens = FileRepositoryUtils.readTokens(line, DELIMITER);
		if (tokens.size() < 4) {
			return null;
		}
		
		try {
			return Interaction.builder()
					.id(tokens.get(0))
					.contact(tokens.get(1))
					.timeStamp(Long.parseLong(tokens.get(2)))
					.notes(tokens.get(3))
					.build();
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	
	private String interactionToLine(Interaction interaction) {
		return interaction.getId() + DELIMITER +
				interaction.getContact() + DELIMITER +
				interaction.getTimeStamp() + DELIMITER +
				interaction.getNotes();
	}
	
	
	@Override
	public List<Interaction> findAll() {
		ensureLoaded();
		return new ArrayList<>(interactions);
	}
	
	
	@Override
	public List<Interaction> findAll(String contact) {
		ensureLoaded();
		return interactions.stream()
				.filter(interaction -> interaction.getContact().equals(contact))
				.toList();
	}
	
	
	@Override
	public Optional<Interaction> find(String id) {
		ensureLoaded();
		return interactions.stream()
				.filter(interaction -> interaction.getId().equals(id))
				.findFirst();
	}
	
	
	@Override
	public Interaction save(Interaction interaction) {
		ensureLoaded();
		// Remove existing interaction with same ID
		interactions.removeIf(i -> i.getId().equals(interaction.getId()));
		interactions.add(interaction);
		saveInteractions();
		return interaction;
	}
	
	
	@Override
	public Interaction delete(String id) {
		ensureLoaded();
		Optional<Interaction> interaction = interactions.stream()
				.filter(i -> i.getId().equals(id))
				.findFirst();
		
		if (interaction.isPresent()) {
			interactions.removeIf(i -> i.getId().equals(id));
			saveInteractions();
			return interaction.get();
		}
		return null;
	}
	
	
	@SneakyThrows
	private void saveInteractions() {
		List<String> lines = interactions.stream()
				.map(this::interactionToLine)
				.toList();
		FileRepositoryUtils.writeLines(lines, TenantFileManager.getTenantDirectory(), FILE_NAME);
	}
}
