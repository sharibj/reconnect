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

import domain.contact.Contact;
import filedb.ContactFileRepository;
import filedb.FileRepositoryUtils;

class ContactFileRepositoryTest {

    public static final String FILE_PATH = "src/test/resources/";
    public static final String FILE_NAME = "contacts.csv";
    ContactFileRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_PATH, FILE_NAME)));
        writer.append("sharib, family\n");
        writer.append("jafari, friends\n");
        writer.close();
        repository = new ContactFileRepository(FILE_PATH, FILE_NAME);

    }

    @Test
    void whenGetByName_thenReturnContact() {
        // given
        String name = "sharib";

        // when
        Optional<Contact> contactOptional = repository.find(name);

        // then
        assertTrue(contactOptional.isPresent());
        Contact contact = contactOptional.get();
        assertEquals("sharib", contact.getNickName());
        assertEquals("family", contact.getGroup());

    }

    @Test
    void whenFindAll_thenReturnAllContacts() {
        // when
        List<Contact> allContacts = repository.findAll();
        // then
        assertEquals(2, allContacts.size());
    }

    @Test
    void whenContactDoesNotExist_thenSaveContact() {
        // given
        Contact contact = Contact.builder().nickName("testSave").build();

        // when
        repository.save(contact);

        // then
        assertEquals(contact, repository.find("testSave").get());
    }

    @Test
    void whenContactDoesExists_thenUpdateContact() {
        // given
        Contact contact = Contact.builder().nickName("testUpdate").group("testGroup").build();
        repository.save(contact);
        Contact updatedContact = Contact.builder().nickName("testUpdate").group("newTestGroup").build();
        // when
        repository.save(updatedContact);

        // then
        assertEquals(updatedContact, repository.find(updatedContact.getNickName()).get());

    }

    @Test
    void whenDeleteByName_thenDeleteContact() {
        // given
        Contact contact = Contact.builder().nickName("testDelete").build();
        repository.save(contact);
        Optional<Contact> contactOptional = repository.find(contact.getNickName());
        assertTrue(contactOptional.isPresent());

        // when
        repository.delete(contact.getNickName());

        // then
        contactOptional = repository.find(contact.getNickName());
        assertTrue(contactOptional.isEmpty());
    }

    @Test
    void whenCommit_thenPersistChangesToFile() throws IOException {
        // given
        FileRepositoryUtils.appendLines(List.of(), FILE_PATH, FILE_NAME);
        ContactFileRepository newRepository = new ContactFileRepository(FILE_PATH, FILE_NAME);
        Contact contact1 = Contact.builder().nickName("test1").group("group1").build();
        Contact contact2 = Contact.builder().nickName("test2").group("group2").build();
        newRepository.save(contact1);
        newRepository.save(contact2);

        List<String> lines = FileRepositoryUtils.readLines(FILE_PATH, FILE_NAME);
        assertEquals(0, lines.size());

        // when
        newRepository.commit();

        // then
        lines = FileRepositoryUtils.readLines(FILE_PATH, FILE_NAME);
        assertEquals(2, lines.size());
        assertEquals("test1,group1", lines.get(0));
        assertEquals("test2,group2", lines.get(1));
    }


}