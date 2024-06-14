package framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import domain.group.Group;

class GroupFileRepositoryTest {

    GroupFileRepository repository = new GroupFileRepository("src/test/resources/", "groups.csv");

    @Test
    void whenGetById_thenReturnGroup() {
        // given
        String id = "id1";

        // when
        Optional<Group> groupOptional = repository.findById(id);

        // then
        assertTrue(groupOptional.isPresent());
        Group group = groupOptional.get();
        assertEquals("id1", group.getId());
        assertEquals("friends", group.getName());
    }

    @Test
    void whenGetByName_thenReturnGroup() {
        // given
        String name = "friends";

        // when
        Optional<Group> groupOptional = repository.findByName(name);

        // then
        assertTrue(groupOptional.isPresent());
        Group group = groupOptional.get();
        assertEquals("id1", group.getId());
        assertEquals("friends", group.getName());

    }

    @Test
    void whenFindAll_thenReturnAllGroups() {
        // when
        List<Group> allGroups = repository.findAll();
        // then
        assertEquals(2, allGroups.size());
    }
}