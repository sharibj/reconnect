package domain.group;

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

@ExtendWith(MockitoExtension.class)
class GroupDomainServiceTest {

    private static final Integer DEFAULT_FREQUENCY = 7;

    @Mock
    GroupRepository repository;
    @Captor
    ArgumentCaptor<Group> groupCaptor;

    @InjectMocks
    GroupDomainService service;

    //region add group
    @Test
    void whenAddGroupWithName_thenSaveWithDefaultFrequency() throws IOException {
        // given
        service.addGroup("family");

        // when
        Mockito.verify(repository).save(groupCaptor.capture());

        // then
        Group group = groupCaptor.getValue();
        assertEquals("family", group.name);
        assertEquals(DEFAULT_FREQUENCY, group.frequencyInDays);
    }

    @Test
    void whenAddGroupWithNameAndFrequency_thenSaveWithCustomFrequency() throws IOException {
        // given
        Integer customFrequency = 100;
        service.addGroup("family", customFrequency);

        // when
        Mockito.verify(repository).save(groupCaptor.capture());

        // then
        Group group = groupCaptor.getValue();
        assertEquals("family", group.name);
        assertEquals(customFrequency, group.frequencyInDays);
    }

    @Test
    void whenAddGroupWithExistingName_thenThrowErrorOnSave() {
        // when
        Mockito.when(repository.findByName("family")).thenReturn(Optional.ofNullable(Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build()));

        // then
        assertThrows(
                IOException.class,
                () -> service.addGroup("family")
        );
    }

    @Test
    void whenAddGroupWithExistingNameAndCustomFrequency_thenThrowError() {
        // when
        Mockito.when(repository.findByName("family")).thenReturn(Optional.ofNullable(Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build()));

        // then
        assertThrows(
                IOException.class,
                () -> service.addGroup("family", 100)
        );
    }
    //endregion

    //region remove group

    @Test
    void whenCalledWithIdOfAnExistingGroup_thenRemoveGroup() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.findById(group.getId())).thenReturn(Optional.of(group));

        // when
        service.removeGroupById(group.getId());

        // then
        Mockito.verify(repository, Mockito.times(1)).deleteById(group.getId());

    }

    @Test
    void whenCalledWithIdOfANonExistingGroup_thenThrowException() throws IOException {
        // given
        String id = "id";

        // when
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        // then
        assertThrows(
                IOException.class,
                () -> service.removeGroupById(id)
        );

    }


    @Test
    void whenCalledWithNameOfAnExistingGroup_thenRemoveGroup() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.findByName(group.getName())).thenReturn(Optional.of(group));

        // when
        service.removeGroupByName(group.getName());

        // then
        Mockito.verify(repository, Mockito.times(1)).deleteByName(group.getName());

    }

    @Test
    void whenCalledWithNameOfANonExistingGroup_thenThrowException() throws IOException {
        // given
        String name = "family";

        // when
        Mockito.when(repository.findByName(name)).thenReturn(Optional.empty());

        // then
        assertThrows(
                IOException.class,
                () -> service.removeGroupByName(name)
        );

    }

    //endregion

    //region update group

    @Test
    void whenGroupWithNameExists_thenUpdateGroupByName() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.findByName(group.getName())).thenReturn(Optional.of(group));
        // when
        service.updateByName("family", 20);
        // then
        Mockito.verify(repository, Mockito.times(1)).save(groupCaptor.capture());
        Group capturedGroup = groupCaptor.getValue();
        assertEquals(20, capturedGroup.getFrequencyInDays().intValue());
    }

    @Test
    void whenGroupDoesNotChange_thenDoNotUpdateGroupByName() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.findByName(group.getName())).thenReturn(Optional.of(group));
        // when
        service.updateByName("family", DEFAULT_FREQUENCY);
        // then
        Mockito.verify(repository, Mockito.times(0)).save(any());
    }

    @Test
    void whenGroupWithNameDoesNotExist_thenThrowExceptionOnUpdate() {
        // given
        String groupName = "family";
        // when
        Mockito.when(repository.findByName(groupName)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.updateByName(groupName, DEFAULT_FREQUENCY));
    }


    @Test
    void whenGroupWithIdExists_thenUpdateGroupById() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.findById(group.getId())).thenReturn(Optional.of(group));
        // when
        service.updateById(group.getId(), "friends", 20);
        // then
        Mockito.verify(repository, Mockito.times(1)).save(groupCaptor.capture());
        Group capturedGroup = groupCaptor.getValue();
        assertEquals(group.getId(), capturedGroup.getId());
        assertEquals("friends", capturedGroup.getName());
        assertEquals(20, capturedGroup.getFrequencyInDays().intValue());
    }

    @Test
    void whenGroupDoesNotChange_thenDoNotUpdateGroupById() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.findById(group.getId())).thenReturn(Optional.of(group));
        // when
        service.updateById(group.getId(), "family", DEFAULT_FREQUENCY);
        // then
        Mockito.verify(repository, Mockito.times(0)).save(any());
    }

    @Test
    void whenGroupWithIdDoesNotExist_thenThrowExceptionOnUpdate() {
        // given
        String groupId = "nonExistingId";
        // when
        Mockito.when(repository.findById(groupId)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.updateById(groupId, "family", DEFAULT_FREQUENCY));
    }


    //endregion

    //region get group

    @Test
    void whenGetAll_thenReturnAllGroups() {
        // given
        Group familyGroup = Group.builder().name("family").frequencyInDays(1).build();
        Group friendsGroup = Group.builder().name("friends").frequencyInDays(1).build();

        Mockito.when(repository.findAll()).thenReturn(List.of(familyGroup, friendsGroup, familyGroup));
        // when
        Set<Group> allGroups = service.getAll();

        // then
        assertEquals(2, allGroups.size());
        assertTrue(allGroups.containsAll(List.of(familyGroup, friendsGroup)));
    }

    @Test
    void whenGroupWithIdExists_thenReturnGroupOnGetById() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.findById(group.getId())).thenReturn(Optional.of(group));
        // when
        Group returnedGroup = service.getById(group.getId());
        // then
        assertEquals(returnedGroup, group);
    }

    @Test
    void whenGroupWithIdDoesNotExist_thenThrowExceptionOnGetById() {
        // given
        String id = "nonExistingId";
        // when
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.getById(id));
    }

    @Test
    void whenGroupWithNameExists_thenReturnGroupOnGetByName() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.findByName(group.getName())).thenReturn(Optional.of(group));
        // when
        Group returnedGroup = service.getByName(group.getName());
        // then
        assertEquals(returnedGroup, group);
    }

    @Test
    void whenGroupWithNameDoesNotExist_thenThrowExceptionOnGetByName() {
        // given
        String name = "nonExistingName";
        // when
        Mockito.when(repository.findByName(name)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.getByName(name));
    }
    //endregion

}