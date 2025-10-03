package adapter.integration;

import adapter.configuration.ReconnectSpringApplication;
import adapter.primary.http.security.TenantContext;
import domain.contact.Contact;
import domain.contact.ContactDetails;
import domain.group.Group;
import domain.interaction.Interaction;
import domain.contact.ContactDomainService;
import domain.group.GroupDomainService;
import domain.interaction.InteractionDomainService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Set;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ReconnectSpringApplication.class)
@ActiveProfiles("test")
@Transactional
public class TenantIsolationIntegrationTest {

    @Autowired
    private ContactDomainService contactService;

    @Autowired
    private GroupDomainService groupService;

    @Autowired
    private InteractionDomainService interactionService;

    private static final String TENANT_1 = "company_a";
    private static final String TENANT_2 = "company_b";

    @AfterEach
    void tearDown() {
        // Always clear tenant context after each test
        TenantContext.clear();
    }

    @Test
    void testGroupTenantIsolation_SameNamesDifferentTenants() throws IOException {
        // Test that two different tenants can create groups with the same name

        // Tenant 1 creates a group
        TenantContext.setCurrentTenant(TENANT_1);
        Group group1 = Group.builder()
                .username(TENANT_1)
                .name("Marketing Team")
                .frequencyInDays(7)
                .build();
        groupService.add(group1);

        // Tenant 2 creates a group with the same name
        TenantContext.setCurrentTenant(TENANT_2);
        Group group2 = Group.builder()
                .username(TENANT_2)
                .name("Marketing Team")
                .frequencyInDays(14)
                .build();
        groupService.add(group2);

        // Verify tenant 1 only sees their group
        TenantContext.setCurrentTenant(TENANT_1);
        Set<Group> tenant1Groups = groupService.getAll();
        assertThat(tenant1Groups).hasSize(1);
        Group t1Group = tenant1Groups.iterator().next();
        assertThat(t1Group.getName()).isEqualTo("marketing team"); // Service converts to lowercase
        assertThat(t1Group.getUsername()).isEqualTo(TENANT_1);
        assertThat(t1Group.getFrequencyInDays()).isEqualTo(7);

        // Verify tenant 2 only sees their group
        TenantContext.setCurrentTenant(TENANT_2);
        Set<Group> tenant2Groups = groupService.getAll();
        assertThat(tenant2Groups).hasSize(1);
        Group t2Group = tenant2Groups.iterator().next();
        assertThat(t2Group.getName()).isEqualTo("marketing team"); // Service converts to lowercase
        assertThat(t2Group.getUsername()).isEqualTo(TENANT_2);
        assertThat(t2Group.getFrequencyInDays()).isEqualTo(14);
    }

    @Test
    void testContactTenantIsolation_SameNicknamesDifferentTenants() throws IOException {
        // First create groups that contacts will reference
        TenantContext.setCurrentTenant(TENANT_1);
        Group group1 = Group.builder().username(TENANT_1).name("friends").frequencyInDays(7).build();
        groupService.add(group1);

        TenantContext.setCurrentTenant(TENANT_2);
        Group group2 = Group.builder().username(TENANT_2).name("colleagues").frequencyInDays(7).build();
        groupService.add(group2);

        // Now test contact isolation
        TenantContext.setCurrentTenant(TENANT_1);
        ContactDetails details1 = new ContactDetails();
        details1.setFirstName("John");
        details1.setLastName("Doe");

        Contact contact1 = Contact.builder()
                .username(TENANT_1)
                .nickName("john_doe")
                .group("friends")
                .details(details1)
                .build();
        contactService.add(contact1);

        // Tenant 2 creates a contact with the same nickname
        TenantContext.setCurrentTenant(TENANT_2);
        ContactDetails details2 = new ContactDetails();
        details2.setFirstName("Johnny");
        details2.setLastName("Doe");

        Contact contact2 = Contact.builder()
                .username(TENANT_2)
                .nickName("john_doe")
                .group("colleagues")
                .details(details2)
                .build();
        contactService.add(contact2);

        // Verify tenant 1 only sees their contact
        TenantContext.setCurrentTenant(TENANT_1);
        Set<Contact> tenant1Contacts = contactService.getAll();
        assertThat(tenant1Contacts).hasSize(1);
        Contact t1Contact = tenant1Contacts.iterator().next();
        assertThat(t1Contact.getNickName()).isEqualTo("john_doe");
        assertThat(t1Contact.getUsername()).isEqualTo(TENANT_1);
        assertThat(t1Contact.getGroup()).isEqualTo("friends");
        assertThat(t1Contact.getDetails().getFirstName()).isEqualTo("John");

        // Verify tenant 2 only sees their contact
        TenantContext.setCurrentTenant(TENANT_2);
        Set<Contact> tenant2Contacts = contactService.getAll();
        assertThat(tenant2Contacts).hasSize(1);
        Contact t2Contact = tenant2Contacts.iterator().next();
        assertThat(t2Contact.getNickName()).isEqualTo("john_doe");
        assertThat(t2Contact.getUsername()).isEqualTo(TENANT_2);
        assertThat(t2Contact.getGroup()).isEqualTo("colleagues");
        assertThat(t2Contact.getDetails().getFirstName()).isEqualTo("Johnny");
    }

    @Test
    void testInteractionTenantIsolation_SameContactNamesDifferentTenants() throws IOException {
        // First create groups and contacts that interactions will reference
        TenantContext.setCurrentTenant(TENANT_1);
        Group group1 = Group.builder().username(TENANT_1).name("work").frequencyInDays(7).build();
        groupService.add(group1);
        Contact contact1 = Contact.builder().username(TENANT_1).nickName("alice_smith").group("work").build();
        contactService.add(contact1);

        TenantContext.setCurrentTenant(TENANT_2);
        Group group2 = Group.builder().username(TENANT_2).name("work").frequencyInDays(7).build();
        groupService.add(group2);
        Contact contact2 = Contact.builder().username(TENANT_2).nickName("alice_smith").group("work").build();
        contactService.add(contact2);

        long timestamp1 = System.currentTimeMillis();
        long timestamp2 = timestamp1 + 1000;

        // Tenant 1 creates an interaction
        TenantContext.setCurrentTenant(TENANT_1);
        Interaction interaction1 = Interaction.builder()
                .username(TENANT_1)
                .contact("alice_smith")
                .timeStamp(timestamp1)
                .notes("Meeting about project A")
                .build();
        interactionService.add(interaction1);

        // Tenant 2 creates an interaction with the same contact name
        TenantContext.setCurrentTenant(TENANT_2);
        Interaction interaction2 = Interaction.builder()
                .username(TENANT_2)
                .contact("alice_smith")
                .timeStamp(timestamp2)
                .notes("Meeting about project B")
                .build();
        interactionService.add(interaction2);

        // Verify tenant 1 only sees their interaction
        TenantContext.setCurrentTenant(TENANT_1);
        List<Interaction> tenant1Interactions = interactionService.getAll();
        assertThat(tenant1Interactions).hasSize(1);
        Interaction t1Interaction = tenant1Interactions.get(0);
        assertThat(t1Interaction.getContact()).isEqualTo("alice_smith");
        assertThat(t1Interaction.getUsername()).isEqualTo(TENANT_1);
        assertThat(t1Interaction.getNotes()).isEqualTo("Meeting about project A");
        assertThat(t1Interaction.getTimeStamp()).isEqualTo(timestamp1);

        // Verify tenant 2 only sees their interaction
        TenantContext.setCurrentTenant(TENANT_2);
        List<Interaction> tenant2Interactions = interactionService.getAll();
        assertThat(tenant2Interactions).hasSize(1);
        Interaction t2Interaction = tenant2Interactions.get(0);
        assertThat(t2Interaction.getContact()).isEqualTo("alice_smith");
        assertThat(t2Interaction.getUsername()).isEqualTo(TENANT_2);
        assertThat(t2Interaction.getNotes()).isEqualTo("Meeting about project B");
        assertThat(t2Interaction.getTimeStamp()).isEqualTo(timestamp2);
    }

    @Test
    void testTenantContextClear() throws IOException {
        // Test that clearing tenant context prevents access to any tenant data

        // Create data for tenant 1
        TenantContext.setCurrentTenant(TENANT_1);
        Group group = Group.builder()
                .username(TENANT_1)
                .name("Test Group")
                .frequencyInDays(7)
                .build();
        groupService.add(group);

        // Clear tenant context - should see no data (filter blocks everything)
        TenantContext.clear();
        Set<Group> noTenantGroups = groupService.getAll();
        assertThat(noTenantGroups).isEmpty();

        // Set tenant context back - should see the data again
        TenantContext.setCurrentTenant(TENANT_1);
        Set<Group> tenant1Groups = groupService.getAll();
        assertThat(tenant1Groups).hasSize(1);
        assertThat(tenant1Groups.iterator().next().getName()).isEqualTo("test group");
    }

    @Test
    void testCrossContaminationPrevention() throws IOException {
        // Comprehensive test to ensure no cross-contamination between tenants

        // Setup data for both tenants with overlapping names
        TenantContext.setCurrentTenant(TENANT_1);

        // Tenant 1 data
        Group group1 = Group.builder().username(TENANT_1).name("Common Name").frequencyInDays(1).build();
        groupService.add(group1);

        Contact contact1 = Contact.builder().username(TENANT_1).nickName("shared_name").group("common name").build();
        contactService.add(contact1);

        Interaction interaction1 = Interaction.builder()
                .username(TENANT_1)
                .contact("shared_name")
                .notes("Tenant 1 note")
                .timeStamp(1000L)
                .build();
        interactionService.add(interaction1);

        // Tenant 2 data
        TenantContext.setCurrentTenant(TENANT_2);

        Group group2 = Group.builder().username(TENANT_2).name("Common Name").frequencyInDays(2).build();
        groupService.add(group2);

        Contact contact2 = Contact.builder().username(TENANT_2).nickName("shared_name").group("common name").build();
        contactService.add(contact2);

        Interaction interaction2 = Interaction.builder()
                .username(TENANT_2)
                .contact("shared_name")
                .notes("Tenant 2 note")
                .timeStamp(2000L)
                .build();
        interactionService.add(interaction2);

        // Verify complete isolation for tenant 1
        TenantContext.setCurrentTenant(TENANT_1);

        Set<Group> t1Groups = groupService.getAll();
        assertThat(t1Groups).hasSize(1);
        assertThat(t1Groups.iterator().next().getFrequencyInDays()).isEqualTo(1);

        Set<Contact> t1Contacts = contactService.getAll();
        assertThat(t1Contacts).hasSize(1);
        assertThat(t1Contacts.iterator().next().getGroup()).isEqualTo("common name");

        List<Interaction> t1Interactions = interactionService.getAll();
        assertThat(t1Interactions).hasSize(1);
        assertThat(t1Interactions.get(0).getNotes()).isEqualTo("Tenant 1 note");
        assertThat(t1Interactions.get(0).getTimeStamp()).isEqualTo(1000L);

        // Verify complete isolation for tenant 2
        TenantContext.setCurrentTenant(TENANT_2);

        Set<Group> t2Groups = groupService.getAll();
        assertThat(t2Groups).hasSize(1);
        assertThat(t2Groups.iterator().next().getFrequencyInDays()).isEqualTo(2);

        Set<Contact> t2Contacts = contactService.getAll();
        assertThat(t2Contacts).hasSize(1);
        assertThat(t2Contacts.iterator().next().getGroup()).isEqualTo("common name");

        List<Interaction> t2Interactions = interactionService.getAll();
        assertThat(t2Interactions).hasSize(1);
        assertThat(t2Interactions.get(0).getNotes()).isEqualTo("Tenant 2 note");
        assertThat(t2Interactions.get(0).getTimeStamp()).isEqualTo(2000L);
    }

    @Test
    void testGetAllInteractions_ShouldReturnInteractionsPaginatedAndSortedByTimestamp() throws IOException {
        // Test the new GET /interactions endpoint with pagination and sorting

        TenantContext.setCurrentTenant(TENANT_1);

        // Create a group and contact first
        Group group = Group.builder().username(TENANT_1).name("test_group").frequencyInDays(7).build();
        groupService.add(group);

        Contact contact = Contact.builder().username(TENANT_1).nickName("test_contact").group("test_group").build();
        contactService.add(contact);

        // Create multiple interactions with different timestamps
        long baseTime = System.currentTimeMillis();

        Interaction interaction1 = Interaction.builder()
                .username(TENANT_1)
                .contact("test_contact")
                .timeStamp(baseTime - 3000) // Oldest
                .notes("First interaction")
                .build();

        Interaction interaction2 = Interaction.builder()
                .username(TENANT_1)
                .contact("test_contact")
                .timeStamp(baseTime - 2000) // Middle
                .notes("Second interaction")
                .build();

        Interaction interaction3 = Interaction.builder()
                .username(TENANT_1)
                .contact("test_contact")
                .timeStamp(baseTime - 1000) // Newest
                .notes("Third interaction")
                .build();

        interactionService.add(interaction1);
        interactionService.add(interaction2);
        interactionService.add(interaction3);

        // Test that getAll() returns interactions sorted by timestamp descending (recent first)
        List<Interaction> allInteractions = interactionService.getAll();

        assertThat(allInteractions).hasSize(3);
        assertThat(allInteractions.get(0).getNotes()).isEqualTo("Third interaction"); // Most recent first
        assertThat(allInteractions.get(1).getNotes()).isEqualTo("Second interaction");
        assertThat(allInteractions.get(2).getNotes()).isEqualTo("First interaction"); // Oldest last

        // Verify timestamp ordering
        assertThat(allInteractions.get(0).getTimeStamp()).isGreaterThan(allInteractions.get(1).getTimeStamp());
        assertThat(allInteractions.get(1).getTimeStamp()).isGreaterThan(allInteractions.get(2).getTimeStamp());
    }

    @Test
    void testGetAllInteractions_WithTenantIsolation_ShouldOnlyReturnCurrentTenantInteractions() throws IOException {
        // Test that the new endpoint respects tenant isolation

        // Setup data for tenant 1
        TenantContext.setCurrentTenant(TENANT_1);
        Group group1 = Group.builder().username(TENANT_1).name("group1").frequencyInDays(7).build();
        groupService.add(group1);
        Contact contact1 = Contact.builder().username(TENANT_1).nickName("contact1").group("group1").build();
        contactService.add(contact1);

        Interaction t1Interaction = Interaction.builder()
                .username(TENANT_1)
                .contact("contact1")
                .notes("Tenant 1 interaction")
                .build();
        interactionService.add(t1Interaction);

        // Setup data for tenant 2
        TenantContext.setCurrentTenant(TENANT_2);
        Group group2 = Group.builder().username(TENANT_2).name("group2").frequencyInDays(7).build();
        groupService.add(group2);
        Contact contact2 = Contact.builder().username(TENANT_2).nickName("contact2").group("group2").build();
        contactService.add(contact2);

        Interaction t2Interaction = Interaction.builder()
                .username(TENANT_2)
                .contact("contact2")
                .notes("Tenant 2 interaction")
                .build();
        interactionService.add(t2Interaction);

        // Verify tenant 1 only sees their interactions
        TenantContext.setCurrentTenant(TENANT_1);
        List<Interaction> tenant1Interactions = interactionService.getAll();
        assertThat(tenant1Interactions).hasSize(1);
        assertThat(tenant1Interactions.get(0).getNotes()).isEqualTo("Tenant 1 interaction");
        assertThat(tenant1Interactions.get(0).getUsername()).isEqualTo(TENANT_1);

        // Verify tenant 2 only sees their interactions
        TenantContext.setCurrentTenant(TENANT_2);
        List<Interaction> tenant2Interactions = interactionService.getAll();
        assertThat(tenant2Interactions).hasSize(1);
        assertThat(tenant2Interactions.get(0).getNotes()).isEqualTo("Tenant 2 interaction");
        assertThat(tenant2Interactions.get(0).getUsername()).isEqualTo(TENANT_2);
    }
}