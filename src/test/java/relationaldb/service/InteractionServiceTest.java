package relationaldb.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import domain.interaction.Interaction;
import domain.interaction.InteractionDetails;
import domain.interaction.InteractionType;
import relationaldb.config.TestRelationalDbConfig;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestRelationalDbConfig.class)
@ActiveProfiles("test")
@Transactional
class InteractionServiceTest {

    @Autowired
    private InteractionService interactionService;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        interactionService.findAll().forEach(interaction -> interactionService.delete(interaction.getId()));
    }

    @Test
    void testSaveAndFindInteraction() {
        // Given
        String id = UUID.randomUUID().toString();
        InteractionDetails details = new InteractionDetails();
        details.setSelfInitiated(true);
        details.setType(InteractionType.AUDIO_CALL);

        Interaction interaction = Interaction.builder()
            .id(id)
            .contact("testContact")
            .timeStamp(System.currentTimeMillis())
            .notes("Test notes")
            .interactionDetails(details)
            .build();

        // When
        Interaction savedInteraction = interactionService.save(interaction);

        // Then
        assertNotNull(savedInteraction);
        Optional<Interaction> foundInteraction = interactionService.find(id);
        assertTrue(foundInteraction.isPresent());
        assertEquals("testContact", foundInteraction.get().getContact());
        assertEquals("Test notes", foundInteraction.get().getNotes());
        assertTrue(foundInteraction.get().getInteractionDetails().getSelfInitiated());
        assertEquals(InteractionType.AUDIO_CALL, foundInteraction.get().getInteractionDetails().getType());
    }

    @Test
    void testFindAllByContact() {
        // Given
        String contact = "testContact";
        InteractionDetails details = new InteractionDetails();
        details.setSelfInitiated(true);
        details.setType(InteractionType.AUDIO_CALL);

        Interaction interaction1 = Interaction.builder()
            .id(UUID.randomUUID().toString())
            .contact(contact)
            .timeStamp(System.currentTimeMillis())
            .notes("Test notes 1")
            .interactionDetails(details)
            .build();

        Interaction interaction2 = Interaction.builder()
            .id(UUID.randomUUID().toString())
            .contact(contact)
            .timeStamp(System.currentTimeMillis())
            .notes("Test notes 2")
            .interactionDetails(details)
            .build();

        interactionService.save(interaction1);
        interactionService.save(interaction2);

        // When
        List<Interaction> foundInteractions = interactionService.findAll(contact);

        // Then
        assertEquals(2, foundInteractions.size());
        assertTrue(foundInteractions.stream().allMatch(i -> i.getContact().equals(contact)));
    }

    @Test
    void testDelete() {
        // Given
        String id = UUID.randomUUID().toString();
        InteractionDetails details = new InteractionDetails();
        details.setSelfInitiated(true);
        details.setType(InteractionType.AUDIO_CALL);

        Interaction interaction = Interaction.builder()
            .id(id)
            .contact("testContact")
            .timeStamp(System.currentTimeMillis())
            .notes("Test notes")
            .interactionDetails(details)
            .build();

        interactionService.save(interaction);

        // When
        Interaction deletedInteraction = interactionService.delete(id);

        // Then
        assertNotNull(deletedInteraction);
        assertEquals(id, deletedInteraction.getId());
        assertTrue(interactionService.findAll().isEmpty());
    }
}
