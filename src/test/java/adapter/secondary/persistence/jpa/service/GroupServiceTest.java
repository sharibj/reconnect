package adapter.secondary.persistence.jpa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import domain.group.Group;
import adapter.secondary.persistence.jpa.config.TestRelationalDbConfig;
import adapter.primary.http.security.TenantContext;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestRelationalDbConfig.class)
@ActiveProfiles("test")
@Transactional
class GroupServiceTest {

    @Autowired
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        // Set up tenant context for multi-tenant tests
        TenantContext.setCurrentTenant("test_user");
        // Clear any existing data
        groupService.findAll().forEach(group -> groupService.delete(group.getName()));
    }

    @Test
    void testSaveAndFindGroup() {
        // Given
        Group group = Group.builder()
            .name("friends")
            .frequencyInDays(7)
            .username("test_user")
            .build();

        // When
        Group savedGroup = groupService.save(group);

        // Then
        assertNotNull(savedGroup);
        Optional<Group> foundGroup = groupService.find("friends");
        assertTrue(foundGroup.isPresent());
        assertEquals("friends", foundGroup.get().getName());
        assertEquals(7, foundGroup.get().getFrequencyInDays());
    }

    @Test
    void testFindAll() {
        // Given
        Group group1 = Group.builder()
            .name("friends")
            .frequencyInDays(7)
            .username("test_user")
            .build();

        Group group2 = Group.builder()
            .name("family")
            .frequencyInDays(14)
            .username("test_user")
            .build();

        groupService.save(group1);
        groupService.save(group2);

        // When
        List<Group> groups = groupService.findAll();

        // Then
        assertEquals(2, groups.size());
        assertTrue(groups.stream().anyMatch(g -> g.getName().equals("friends")));
        assertTrue(groups.stream().anyMatch(g -> g.getName().equals("family")));
    }

    @Test
    void testDelete() {
        // Given
        Group group = Group.builder()
            .name("testgroup")
            .frequencyInDays(7)
            .username("test_user")
            .build();

        groupService.save(group);

        // When
        Group deletedGroup = groupService.delete("testgroup");

        // Then
        assertNotNull(deletedGroup);
        assertEquals("testgroup", deletedGroup.getName());
        assertTrue(groupService.findAll().isEmpty());
    }
}
