package domain.interaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.contact.Contact;
import domain.contact.ContactRepository;

@ExtendWith(MockitoExtension.class)
class InteractionDomainServiceTest {
    @Mock
    InteractionRepository interactionRepository;
    @Mock
    ContactRepository contactRepository;
    @Captor
    ArgumentCaptor<Interaction> interactionCaptor;
    @InjectMocks
    InteractionDomainService service;


    //region add interaction
    @Test
    void whenAddInteractionWithExistingContact_thenSaveInteraction() throws IOException {
        // given
        Mockito.when(contactRepository.find("sharib")).thenReturn(Optional.ofNullable(Contact.builder().nickName("sharib").build()));
        Interaction interaction = Interaction.builder()
                .contact("sharib")
                .build();

        Mockito.when(interactionRepository.save(any(Interaction.class))).thenReturn(interaction);

        // when
        service.add(interaction);

        // then
        Mockito.verify(interactionRepository).save(interactionCaptor.capture());
    }

    @Test
    void whenAddInteractionWithNonExistingContact_thenThrowError() {
        // when
        Mockito.when(contactRepository.find("sharib")).thenReturn(Optional.empty());

        // then
        assertThrows(
                IOException.class,
                () -> service.add(Interaction.builder().contact("sharib").build())
        );
    }

    //endregion

    //region remove interaction
    @Test
    void whenCalledWithIdOfAnExistingInteraction_thenRemoveInteraction() throws IOException {
        // given
        Interaction interaction = Interaction.builder().contact("sharib").build();
        Mockito.when(interactionRepository.find(interaction.getId())).thenReturn(Optional.of(interaction));

        // when
        service.remove(interaction.getId());

        // then
        Mockito.verify(interactionRepository, Mockito.times(1)).delete(interaction.getId());

    }

    @Test
    void whenCalledWithIdOfANonExistingInteraction_thenThrowException() {
        // given
        String id = "1234ABCD";

        // when
        Mockito.when(interactionRepository.find(id)).thenReturn(Optional.empty());

        // then
        assertThrows(
                IOException.class,
                () -> service.remove(id)
        );

    }

    //endregion

    //region update interaction

    @Test
    void whenInteractionWithIdExists_thenUpdateInteraction() throws IOException {
        // given
        Interaction interaction = Interaction.builder().contact("jafari").build();
        Mockito.when(interactionRepository.find(interaction.getId())).thenReturn(Optional.of(interaction));
        Mockito.when(contactRepository.find("sharib")).thenReturn(Optional.ofNullable(Contact.builder().nickName("sharib").build()));
        // when
        service.update(Interaction.builder().id(interaction.getId()).contact("sharib").build());
        // then
        Mockito.verify(interactionRepository, Mockito.times(1)).save(interactionCaptor.capture());
        Interaction capturedInteraction = interactionCaptor.getValue();
        assertEquals(interaction.getId(), capturedInteraction.getId());
        assertEquals("sharib", capturedInteraction.getContact());
    }

    @Test
    void whenInteractionDoesNotChange_thenDoNotUpdateInteraction() throws IOException {
        // given
        Interaction interaction = Interaction.builder().contact("sharib").build();
        Mockito.when(interactionRepository.find(interaction.getId())).thenReturn(Optional.of(interaction));
        Mockito.when(contactRepository.find("sharib")).thenReturn(Optional.ofNullable(Contact.builder().nickName("sharib").build()));
        // when
        service.update(Interaction.builder().id(interaction.getId()).contact("sharib").timeStamp(interaction.getTimeStamp()).build());
        // then
        Mockito.verify(interactionRepository, Mockito.times(0)).save(any());
    }

    @Test
    void whenInteractionWithIdDoesNotExist_thenThrowExceptionOnUpdate() {
        // given
        Mockito.when(contactRepository.find("sharib")).thenReturn(Optional.ofNullable(Contact.builder().nickName("sharib").build()));
        String id = "1234ABC";

        // when
        Mockito.when(interactionRepository.find(id)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.update(Interaction.builder().id(id).contact("sharib").build()));
    }

    @Test
    void whenUpdateInteractionWithNonExistingContact_thenThrowError() {
        // when
        Mockito.when(contactRepository.find("sharib")).thenReturn(Optional.empty());
        // then
        assertThrows(
                IOException.class,
                () -> service.update(Interaction.builder().contact("sharib").build())
        );
    }
    //endregion

    //region get interaction

    @Test
    void whenGetAll_thenReturnAllInteractionsSortedByTimeStamp() {
        // given
        Interaction sharibInteraction = Interaction.builder().contact("sharib").timeStamp(new Date().getTime() - 1000).build();
        Interaction jafariInteraction = Interaction.builder().contact("jafari").timeStamp(new Date().getTime()).build();

        Mockito.when(interactionRepository.findAll()).thenReturn(List.of(sharibInteraction, sharibInteraction, jafariInteraction));
        // when
        List<Interaction> allInteractions = service.getAll();

        // then
        assertEquals(2, allInteractions.size());
        assertEquals(jafariInteraction, allInteractions.get(0));
        assertEquals(sharibInteraction, allInteractions.get(1));
    }

    @Test
    void whenInteractionWithIdExists_thenReturnInteractionOnGet() throws IOException {
        // given
        Interaction interaction = Interaction.builder().contact("sharib").build();
        Mockito.when(interactionRepository.find(interaction.getId())).thenReturn(Optional.of(interaction));
        // when
        Interaction returnedInteraction = service.get(interaction.getId());
        // then
        assertEquals(returnedInteraction, interaction);
    }

    @Test
    void whenInteractionWithIdDoesNotExist_thenThrowExceptionOnGet() {
        // given
        String id = "nonExistingId";
        // when
        Mockito.when(interactionRepository.find(id)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.get(id));
    }


    @Test
    void whenContactExists_thenReturnAllInteractionsOnGetByContactSortedByTimeStamp() throws IOException {
        // given
        Interaction sharibInteraction = Interaction.builder().contact("sharib").timeStamp(new Date().getTime() - 1000).build();
        Interaction jafariInteraction = Interaction.builder().contact("jafari").timeStamp(new Date().getTime()).build();

        Mockito.when(contactRepository.find("sharib")).thenReturn(Optional.ofNullable(Contact.builder().nickName("sharib").build()));
        Mockito.when(interactionRepository.findAll("sharib")).thenReturn(List.of(sharibInteraction, jafariInteraction, jafariInteraction));
        // when
        List<Interaction> interactions = service.getAll("sharib");
        // then
        assertEquals(2, interactions.size());
        assertEquals(jafariInteraction, interactions.get(0));
        assertEquals(sharibInteraction, interactions.get(1));
    }

    @Test
    void whenContactDoesNotExist_thenThrowExceptionOnGetByContact() {
        // given
        String contactName = "nonExistingContact";
        // when
        Mockito.when(contactRepository.find(contactName)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.getAll(contactName));
    }
    //endregion


}