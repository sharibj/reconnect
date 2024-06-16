package domain.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.group.Group;
import domain.group.GroupRepository;

@ExtendWith(MockitoExtension.class)
class ContactDomainServiceTest {

    @Mock
    ContactRepository repository;
    @Mock
    GroupRepository groupRepository;
    @Captor
    ArgumentCaptor<Contact> contactCaptor;

    @InjectMocks
    ContactDomainService service;

    //region add contact
    @Test
    void whenAddContactWithName_thenSaveContact() throws IOException {
        // given
        service.add("sharib");

        // when
        Mockito.verify(repository).save(contactCaptor.capture());

        // then
        Contact contact = contactCaptor.getValue();
        assertEquals("sharib", contact.nickName);
    }

    @Test
    void whenAddContactWithNameAndExistingGroup_thenSaveContact() throws IOException {
        // given
        Mockito.when(groupRepository.find("family")).thenReturn(Optional.ofNullable(Group.builder().name("family").frequencyInDays(7).build()));
        service.add("sharib", "family");

        // when
        Mockito.verify(repository).save(contactCaptor.capture());

        // then
        Contact contact = contactCaptor.getValue();
        assertEquals("sharib", contact.nickName);
        assertEquals("family", contact.group);
    }

    @Test
    void whenAddContactWithExistingName_thenThrowError() {
        // when
        Mockito.when(repository.find("sharib")).thenReturn(Optional.ofNullable(Contact.builder().nickName("family").build()));

        // then
        assertThrows(
                IOException.class,
                () -> service.add("sharib")
        );
    }

    @Test
    void whenAddContactWithNameAndNonExistingGroup_thenThrowError() {
        // when
        Mockito.when(groupRepository.find("family")).thenReturn(Optional.empty());

        // then
        assertThrows(
                IOException.class,
                () -> service.add("sharib", "family")
        );
    }

    //endregion
/*

    //region remove contact
    @Test
    void whenCalledWithNameOfAnExistingContact_thenRemoveContact() throws IOException {
        // given
        Contact contact = Contact.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.find(contact.getName())).thenReturn(Optional.of(contact));

        // when
        service.remove(contact.getName());

        // then
        Mockito.verify(repository, Mockito.times(1)).delete(contact.getName());

    }

    @Test
    void whenCalledWithNameOfANonExistingContact_thenThrowException() throws IOException {
        // given
        String name = "family";

        // when
        Mockito.when(repository.find(name)).thenReturn(Optional.empty());

        // then
        assertThrows(
                IOException.class,
                () -> service.remove(name)
        );

    }

    //endregion

    //region update contact

    @Test
    void whenContactWithNameExists_thenUpdateContact() throws IOException {
        // given
        Contact contact = Contact.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.find(contact.getName())).thenReturn(Optional.of(contact));
        // when
        service.update("family", 20);
        // then
        Mockito.verify(repository, Mockito.times(1)).save(contactCaptor.capture());
        Contact capturedContact = contactCaptor.getValue();
        assertEquals(20, capturedContact.getFrequencyInDays().intValue());
    }

    @Test
    void whenContactDoesNotChange_thenDoNotUpdateContact() throws IOException {
        // given
        Contact contact = Contact.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.find(contact.getName())).thenReturn(Optional.of(contact));
        // when
        service.update("family", DEFAULT_FREQUENCY);
        // then
        Mockito.verify(repository, Mockito.times(0)).save(any());
    }

    @Test
    void whenContactWithNameDoesNotExist_thenThrowExceptionOnUpdate() {
        // given
        String contactName = "family";
        // when
        Mockito.when(repository.find(contactName)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.update(contactName, DEFAULT_FREQUENCY));
    }


    //endregion

    //region get contact

    @Test
    void whenGetAll_thenReturnAllContacts() {
        // given
        Contact familyContact = Contact.builder().name("family").frequencyInDays(1).build();
        Contact friendsContact = Contact.builder().name("friends").frequencyInDays(1).build();

        Mockito.when(repository.findAll()).thenReturn(List.of(familyContact, friendsContact, familyContact));
        // when
        Set<Contact> allContacts = service.getAll();

        // then
        assertEquals(2, allContacts.size());
        assertTrue(allContacts.containsAll(List.of(familyContact, friendsContact)));
    }

    @Test
    void whenContactWithNameExists_thenReturnContactOnGet() throws IOException {
        // given
        Contact contact = Contact.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.find(contact.getName())).thenReturn(Optional.of(contact));
        // when
        Contact returnedContact = service.get(contact.getName());
        // then
        assertEquals(returnedContact, contact);
    }

    @Test
    void whenContactWithNameDoesNotExist_thenThrowExceptionOnGet() {
        // given
        String name = "nonExistingName";
        // when
        Mockito.when(repository.find(name)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.get(name));
    }
    //endregion
*/

}