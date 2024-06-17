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

import domain.group.Group;

class GroupFileRepositoryTest {

    public static final String FILE_PATH = "src/test/resources/";
    public static final String FILE_NAME = "groups.csv";
    GroupFileRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_PATH, FILE_NAME)));
        writer.append("friends, 3\nfamily, 6");
        writer.close();
        repository = new GroupFileRepository(FILE_PATH, FILE_NAME);
    }

    @Test
    void whenGetByName_thenReturnGroup() {
        // given
        String name = "friends";

        // when
        Optional<Group> groupOptional = repository.find(name);

        // then
        assertTrue(groupOptional.isPresent());
        Group group = groupOptional.get();
        assertEquals("friends", group.getName());

    }

    @Test
    void whenFindAll_thenReturnAllGroups() {
        // when
        List<Group> allGroups = repository.findAll();
        // then
        assertEquals(2, allGroups.size());
    }

    @Test
    void whenGroupDoesNotExist_thenSaveGroup() {
        // given
        Group group = Group.builder().name("testSave").frequencyInDays(10).build();

        // when
        repository.save(group);

        // then
        assertEquals(group, repository.find("testSave").get());
    }

    @Test
    void whenGroupDoesExists_thenUpdateGroup() {
        // given
        Group group = Group.builder().name("testUpdate").frequencyInDays(10).build();
        repository.save(group);
        Group updatedGroup = Group.builder().name("new testUpdate").frequencyInDays(12).build();
        // when
        repository.save(updatedGroup);

        // then
        assertEquals(updatedGroup, repository.find(updatedGroup.getName()).get());

    }


    @Test
    void whenDeleteByName_thenDeleteGroup() {
        // given
        Group group = Group.builder().name("testDelete").frequencyInDays(10).build();
        repository.save(group);
        Optional<Group> groupOptional = repository.find(group.getName());
        assertTrue(groupOptional.isPresent());

        // when
        repository.delete(group.getName());

        // then
        groupOptional = repository.find(group.getName());
        assertTrue(groupOptional.isEmpty());
    }

    @Test
    void whenCommit_thenPersistChangesToFile() throws IOException {
        // given
        FileRepositoryUtils.appendLines(List.of(), FILE_PATH, FILE_NAME);
        GroupFileRepository newRepository = new GroupFileRepository(FILE_PATH, FILE_NAME);
        Group group1 = Group.builder().name("test1").frequencyInDays(1).build();
        Group group2 = Group.builder().name("test2").frequencyInDays(2).build();
        newRepository.save(group1);
        newRepository.save(group2);

        List<String> lines = FileRepositoryUtils.readLines(FILE_PATH, FILE_NAME);
        assertEquals(0, lines.size());

        // when
        newRepository.commit();

        // then
        lines = FileRepositoryUtils.readLines(FILE_PATH, FILE_NAME);
        assertEquals(2, lines.size());
        assertEquals("test1,1", lines.get(0));
        assertEquals("test2,2", lines.get(1));
    }
}