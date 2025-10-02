package adapter.secondary.persistence.jpa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import domain.contact.Contact;
import domain.contact.ContactDetails;
import domain.contact.ContactInfo;
import adapter.secondary.persistence.jpa.config.TestRelationalDbConfig;
import adapter.primary.http.security.TenantContext;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestRelationalDbConfig.class)
@ActiveProfiles("test")
@Transactional
class ContactServiceTest {

    @Autowired
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        // Set up tenant context for multi-tenant tests
        TenantContext.setCurrentTenant("test_user");
        // Clear any existing data
        contactService.findAll().forEach(contact -> contactService.delete(contact.getNickName()));
    }

    @Test
    void testSaveAndFindContact() {
        // Given
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail("john@example.com");
        contactInfo.setPhone("123456789");

        ContactDetails details = new ContactDetails();
        details.setFirstName("John");
        details.setLastName("Doe");
        details.setNotes("Test notes");
        details.setContactInfo(contactInfo);

        Contact contact = Contact.builder()
            .nickName("johndoe")
            .group("friends")
            .details(details)
            .username("test_user")
            .build();

        // When
        Contact savedContact = contactService.save(contact);

        // Then
        assertNotNull(savedContact);
        Optional<Contact> foundContact = contactService.find("johndoe");
        assertTrue(foundContact.isPresent());
        assertEquals("johndoe", foundContact.get().getNickName());
        assertEquals("friends", foundContact.get().getGroup());
        assertEquals("John", foundContact.get().getDetails().getFirstName());
        assertEquals("john@example.com", foundContact.get().getDetails().getContactInfo().getEmail());
    }

    @Test
    void testFindAll() {
        // Given
        Contact contact1 = Contact.builder()
            .nickName("contact1")
            .group("friends")
            .username("test_user")
            .build();

        Contact contact2 = Contact.builder()
            .nickName("contact2")
            .group("family")
            .username("test_user")
            .build();

        contactService.save(contact1);
        contactService.save(contact2);

        // When
        List<Contact> contacts = contactService.findAll();

        // Then
        assertEquals(2, contacts.size());
        assertTrue(contacts.stream().anyMatch(c -> c.getNickName().equals("contact1")));
        assertTrue(contacts.stream().anyMatch(c -> c.getNickName().equals("contact2")));
    }

    @Test
    void testDelete() {
        // Given
        Contact contact = Contact.builder()
            .nickName("testcontact")
            .group("test")
            .username("test_user")
            .build();

        contactService.save(contact);

        // When
        Contact deletedContact = contactService.delete("testcontact");

        // Then
        assertNotNull(deletedContact);
        assertEquals("testcontact", deletedContact.getNickName());
        assertTrue(contactService.findAll().isEmpty());
    }
}
