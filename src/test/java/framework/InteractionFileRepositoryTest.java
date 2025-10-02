package framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.interaction.Interaction;
import adapter.secondary.persistence.file.FileRepositoryUtils;
import adapter.secondary.persistence.file.InteractionFileRepository;

class InteractionFileRepositoryTest {
    public static final String FILE_PATH = "src/test/resources/";
    public static final String FILE_NAME = "interactions.csv";
    InteractionFileRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_PATH, FILE_NAME)));
        writer.append("id1± sharib\n");
        writer.append("id2± jafari\n");
        writer.close();
        repository = new InteractionFileRepository(FILE_PATH, FILE_NAME);

    }

    @Test
    void whenGetById_thenReturnInteraction() {
        // given
        String id = "id1";

        // when
        Optional<Interaction> interactionOptional = repository.find(id);

        // then
        assertTrue(interactionOptional.isPresent());
        Interaction interaction = interactionOptional.get();
        assertEquals("id1", interaction.getId());
        assertEquals("sharib", interaction.getContact());

    }

    @Test
    void whenFindAll_thenReturnAllInteractions() {
        // when
        List<Interaction> allInteractions = repository.findAll();
        // then
        assertEquals(2, allInteractions.size());
    }

    @Test
    void whenFindAllByContact_thenReturnAllInteractionsForContact() {
        // when
        List<Interaction> allInteractions = repository.findAll("sharib");
        // then
        assertEquals(1, allInteractions.size());
    }

    @Test
    void whenInteractionDoesNotExist_thenSaveInteraction() {
        // given
        Interaction interaction = Interaction.builder().contact("testSave").build();

        // when
        repository.save(interaction);

        // then
        assertEquals(interaction, repository.find(interaction.getId()).get());
    }

    @Test
    void whenInteractionDoesExists_thenUpdateInteraction() {
        // given
        Interaction interaction = Interaction.builder().id("testUpdate").contact("testContact").build();
        repository.save(interaction);
        Interaction updatedInteraction = Interaction.builder().id("testUpdate").contact("newTestContact").build();
        // when
        repository.save(updatedInteraction);

        // then
        assertEquals(updatedInteraction, repository.find(updatedInteraction.getId()).get());

    }

    @Test
    void whenDeleteByName_thenDeleteInteraction() {
        // given
        Interaction interaction = Interaction.builder().id("testDelete").contact("deleteContact").build();
        repository.save(interaction);
        Optional<Interaction> interactionOptional = repository.find(interaction.getId());
        assertTrue(interactionOptional.isPresent());

        // when
        repository.delete(interaction.getId());

        // then
        interactionOptional = repository.find(interaction.getId());
        assertTrue(interactionOptional.isEmpty());
    }

    @Test
    void whenCommit_thenPersistChangesToFile() throws IOException {
        // given
        FileRepositoryUtils.appendLines(List.of(), FILE_PATH, FILE_NAME);
        InteractionFileRepository newRepository = new InteractionFileRepository(FILE_PATH, FILE_NAME);
        Interaction interaction1 = Interaction.builder().id("test1").contact("contact1").build();
        Interaction interaction2 = Interaction.builder().id("test2").contact("contact2").build();
        newRepository.save(interaction1);
        newRepository.save(interaction2);

        List<String> lines = FileRepositoryUtils.readLines(FILE_PATH, FILE_NAME);
        assertEquals(0, lines.size());

        // when
        newRepository.commit();

        // then
        lines = FileRepositoryUtils.readLines(FILE_PATH, FILE_NAME);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).startsWith("test1±contact1±"));
        assertTrue(lines.get(1).startsWith("test2±contact2±"));
    }

	@Test
	void whenMultilineInteraction_thenSaveWithNewlineCharacters() throws IOException {
		// given
		FileRepositoryUtils.appendLines(List.of(), FILE_PATH, FILE_NAME);
		InteractionFileRepository newRepository = new InteractionFileRepository(FILE_PATH, FILE_NAME);
		String notesWithNewline = "This is a note\nwith a newline character.";
		Interaction interaction = Interaction.builder().id("testNewline").contact("contactNewline").notes(notesWithNewline).build();
		newRepository.save(interaction);

		// when
		newRepository.commit();

		// then
		List<String> lines = FileRepositoryUtils.readLines(FILE_PATH, FILE_NAME);
		assertEquals(1, lines.size());
		assertTrue(lines.get(0).contains("testNewline±contactNewline±"));
		assertTrue(lines.get(0).contains("This is a note\\nwith a newline character."));
	}
	@Test
	void whenMultilineInteraction_thenReadWithNewlineCharacters() throws IOException {
		// given
		String notesWithNewline = "This is a note\\nwith a newline character.";
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_PATH, FILE_NAME)));
		writer.append("testReadNewline± contactReadNewline±123456789±" + notesWithNewline + "\n");
		writer.close();
		InteractionFileRepository newRepository = new InteractionFileRepository(FILE_PATH, FILE_NAME);

		// when
		Optional<Interaction> interactionOptional = newRepository.find("testReadNewline");

		// then
		assertTrue(interactionOptional.isPresent());
		Interaction interaction = interactionOptional.get();
		assertEquals("testReadNewline", interaction.getId());
		assertEquals("contactReadNewline", interaction.getContact());
		assertEquals("This is a note\nwith a newline character.", interaction.getNotes());
	}
}