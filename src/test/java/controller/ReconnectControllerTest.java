package controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import adapter.primary.http.controller.ReconnectController;
import domain.contact.ContactDomainService;
import domain.group.GroupDomainService;
import domain.interaction.InteractionDomainService;
import domain.ReconnectDomainService;
import domain.interaction.Interaction;

@WebMvcTest(controllers = ReconnectController.class, excludeAutoConfiguration = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
class ReconnectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactDomainService contactDomainService;

    @MockBean
    private InteractionDomainService interactionDomainService;

    @MockBean
    private GroupDomainService groupDomainService;

    @MockBean
    private ReconnectDomainService reconnectDomainService;

    @Test
    void listAllInteractions_ShouldReturnInteractionsPaginatedAndSortedByTimeStampDescending() throws Exception {
        // given
        long timestamp1 = new Date().getTime() - 2000;
        long timestamp2 = new Date().getTime() - 1000;
        long timestamp3 = new Date().getTime();

        Interaction interaction1 = Interaction.builder()
                .id("1")
                .username("testuser")
                .contact("alice")
                .timeStamp(timestamp1)
                .notes("Oldest interaction")
                .build();

        Interaction interaction2 = Interaction.builder()
                .id("2")
                .username("testuser")
                .contact("bob")
                .timeStamp(timestamp2)
                .notes("Middle interaction")
                .build();

        Interaction interaction3 = Interaction.builder()
                .id("3")
                .username("testuser")
                .contact("charlie")
                .timeStamp(timestamp3)
                .notes("Newest interaction")
                .build();

        // Mock returns interactions sorted by timestamp descending (newest first)
        List<Interaction> sortedInteractions = List.of(interaction3, interaction2, interaction1);
        when(interactionDomainService.getAll()).thenReturn(sortedInteractions);

        // when & then
        mockMvc.perform(get("/api/reconnect/interactions")
                .param("page", "0")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("3"))
                .andExpect(jsonPath("$[0].contact").value("charlie"))
                .andExpect(jsonPath("$[0].notes").value("Newest interaction"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].contact").value("bob"))
                .andExpect(jsonPath("$[1].notes").value("Middle interaction"));

        verify(interactionDomainService).getAll();
    }

    @Test
    void listAllInteractions_WithDefaultPagination_ShouldReturnFirstPageOfTenItems() throws Exception {
        // given
        List<Interaction> allInteractions = createTestInteractions(15);
        when(interactionDomainService.getAll()).thenReturn(allInteractions);

        // when & then
        mockMvc.perform(get("/api/reconnect/interactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(10));

        verify(interactionDomainService).getAll();
    }

    @Test
    void listAllInteractions_WithSecondPage_ShouldReturnCorrectItems() throws Exception {
        // given
        List<Interaction> allInteractions = createTestInteractions(15);
        when(interactionDomainService.getAll()).thenReturn(allInteractions);

        // when & then
        mockMvc.perform(get("/api/reconnect/interactions")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(5));

        verify(interactionDomainService).getAll();
    }

    @Test
    void listAllInteractions_WithEmptyResult_ShouldReturnEmptyArray() throws Exception {
        // given
        when(interactionDomainService.getAll()).thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/reconnect/interactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(interactionDomainService).getAll();
    }

    private List<Interaction> createTestInteractions(int count) {
        return java.util.stream.IntStream.range(0, count)
                .mapToObj(i -> Interaction.builder()
                        .id("id" + i)
                        .username("testuser")
                        .contact("contact" + i)
                        .timeStamp(new Date().getTime() - (count - i) * 1000L)
                        .notes("Note " + i)
                        .build())
                .toList();
    }
}