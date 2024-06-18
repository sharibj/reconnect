package domain.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        service.add(Contact.builder().nickName("sharib").build());

        // when
        Mockito.verify(repository).save(contactCaptor.capture());

        // then
        Contact contact = contactCaptor.getValue();
        assertEquals("sharib", contact.getNickName());
    }

    @Test
    void whenAddContactWithNameAndExistingGroup_thenSaveContact() throws IOException {
        // given
        Mockito.when(groupRepository.find("family")).thenReturn(Optional.ofNullable(Group.builder().name("family").frequencyInDays(7).build()));
        service.add(Contact.builder().nickName("sharib").group("family").build());

        // when
        Mockito.verify(repository).save(contactCaptor.capture());

        // then
        Contact contact = contactCaptor.getValue();
        assertEquals("sharib", contact.getNickName());
        assertEquals("family", contact.getGroup());
    }

    @Test
    void whenAddContactWithExistingName_thenThrowError() {
        // when
        Mockito.when(repository.find("sharib")).thenReturn(Optional.ofNullable(Contact.builder().nickName("family").build()));

        // then
        assertThrows(
                IOException.class,
                () -> service.add(Contact.builder().nickName("sharib").build())
        );
    }

    @Test
    void whenAddContactWithNameAndNonExistingGroup_thenThrowError() {
        // when
        Mockito.when(groupRepository.find("family")).thenReturn(Optional.empty());

        // then
        assertThrows(
                IOException.class,
                () -> service.add(Contact.builder().nickName("sharib").group("family").build())
        );
    }

    //endregion

    //region remove contact
    @Test
    void whenCalledWithNameOfAnExistingContact_thenRemoveContact() throws IOException {
        // given
        Contact contact = Contact.builder().nickName("sharib").build();
        Mockito.when(repository.find(contact.getNickName())).thenReturn(Optional.of(contact));

        // when
        service.remove(contact.getNickName());

        // then
        Mockito.verify(repository, Mockito.times(1)).delete(contact.getNickName());

    }

    @Test
    void whenCalledWithNameOfANonExistingContact_thenThrowException() {
        // given
        String nickName = "sharib";

        // when
        Mockito.when(repository.find(nickName)).thenReturn(Optional.empty());

        // then
        assertThrows(
                IOException.class,
                () -> service.remove(nickName)
        );

    }

    //endregion

    //region update contact

    @Test
    void whenContactWithNameExists_thenUpdateContact() throws IOException {
        // given
        Contact contact = Contact.builder().nickName("sharib").group("family").build();
        Mockito.when(repository.find(contact.getNickName())).thenReturn(Optional.of(contact));
        Mockito.when(groupRepository.find("friends")).thenReturn(Optional.ofNullable(Group.builder().name("friends").build()));
        // when
        service.update(Contact.builder().nickName("sharib").group("friends").build());
        // then
        Mockito.verify(repository, Mockito.times(1)).save(contactCaptor.capture());
        Contact capturedContact = contactCaptor.getValue();
        assertEquals("friends", capturedContact.getGroup());
    }

    @Test
    void whenContactDoesNotChange_thenDoNotUpdateContact() throws IOException {
        // given
        Contact contact = Contact.builder().nickName("sharib").group("family").build();
        Mockito.when(repository.find(contact.getNickName())).thenReturn(Optional.of(contact));
        Mockito.when(groupRepository.find("family")).thenReturn(Optional.ofNullable(Group.builder().name("family").build()));
        // when
        service.update(Contact.builder().nickName("sharib").group("family").build());
        // then
        Mockito.verify(repository, Mockito.times(0)).save(any());
    }

    @Test
    void whenContactWithNameDoesNotExist_thenThrowExceptionOnUpdate() {
        // given
        String contactName = "sharib";
        // when
        Mockito.when(repository.find(contactName)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.update(Contact.builder().nickName("sharib").build()));
    }

    @Test
    void whenUpdateContactWithNonExistingGroup_thenThrowError() {
        // when
        Mockito.when(groupRepository.find("family")).thenReturn(Optional.empty());
        // then
        assertThrows(
                IOException.class,
                () -> service.update(Contact.builder().nickName("sharib").group("family").build())
        );
    }
    //endregion

    //region get contact

    @Test
    void whenGetAll_thenReturnAllContacts() {
        // given
        Contact familyContact = Contact.builder().nickName("sharib").group("family").build();
        Contact friendsContact = Contact.builder().nickName("jafari").group("friends").build();

        Mockito.when(repository.findAll()).thenReturn(List.of(familyContact, friendsContact, familyContact));
        // when
        Set<Contact> allContacts = service.getAll();

        // then
        assertEquals(2, allContacts.size());
        assertTrue(allContacts.containsAll(List.of(familyContact, friendsContact)));
    }

    @Test
    void whenGetAllByGroup_thenReturnAllContactsForGroup() {
        // given
        Contact familyContact = Contact.builder().nickName("sharib").group("family").build();
        Contact friendsContact = Contact.builder().nickName("jafari").group("friends").build();

        Mockito.when(repository.findAll()).thenReturn(List.of(familyContact, friendsContact, familyContact));
        // when
        Set<Contact> allContacts = service.getAll("family");

        // then
        assertEquals(1, allContacts.size());
        assertTrue(allContacts.contains(familyContact));
    }

    @Test
    void whenContactWithNameExists_thenReturnContactOnGet() {
        // given
        Contact contact = Contact.builder().nickName("family").build();
        Mockito.when(repository.find(contact.getNickName())).thenReturn(Optional.of(contact));
        // when
        Contact returnedContact = service.get(contact.getNickName()).get();
        // then
        assertEquals(returnedContact, contact);
    }
    //endregion

}