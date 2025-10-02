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
import spring.security.TenantContext;
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
        // Set up tenant context for multi-tenant tests
        TenantContext.setCurrentTenant("test_user");
        // Clear any existing data
        interactionService.findAll().forEach(interaction -> interactionService.delete(interaction.getId()));
    }

    @Test
    void testSaveAndFindInteraction() {
        // Given
        InteractionDetails details = new InteractionDetails();
        details.setSelfInitiated(true);
        details.setType(InteractionType.AUDIO_CALL);

        Interaction interaction = Interaction.builder()
            .contact("testContact")
            .timeStamp(System.currentTimeMillis())
            .notes("Test notes")
            .interactionDetails(details)
            .username("test_user")
            .build();

        // When
        Interaction savedInteraction = interactionService.save(interaction);

        // Then
        assertNotNull(savedInteraction);
        assertNotNull(savedInteraction.getId()); // ID should be auto-generated
        Optional<Interaction> foundInteraction = interactionService.find(savedInteraction.getId());
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
            .contact(contact)
            .timeStamp(System.currentTimeMillis())
            .notes("Test notes 1")
            .interactionDetails(details)
            .username("test_user")
            .build();

        Interaction interaction2 = Interaction.builder()
            .contact(contact)
            .timeStamp(System.currentTimeMillis())
            .notes("Test notes 2")
            .interactionDetails(details)
            .username("test_user")
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
        InteractionDetails details = new InteractionDetails();
        details.setSelfInitiated(true);
        details.setType(InteractionType.AUDIO_CALL);

        Interaction interaction = Interaction.builder()
            .contact("testContact")
            .timeStamp(System.currentTimeMillis())
            .notes("Test notes")
            .interactionDetails(details)
            .username("test_user")
            .build();

        Interaction savedInteraction = interactionService.save(interaction);
        String savedId = savedInteraction.getId();

        // When
        Interaction deletedInteraction = interactionService.delete(savedId);

        // Then
        assertNotNull(deletedInteraction);
        assertEquals(savedId, deletedInteraction.getId());
        assertTrue(interactionService.findAll().isEmpty());
    }
}
