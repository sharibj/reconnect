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
        service.add(Group.builder().name("family").build());

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
        service.add(Group.builder().name("family").frequencyInDays(customFrequency).build());

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
        Mockito.when(repository.find("family")).thenReturn(Optional.ofNullable(Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build()));

        // then
        assertThrows(
                IOException.class,
                () -> service.add(Group.builder().name("family").build())
        );
    }

    @Test
    void whenAddGroupWithExistingNameAndCustomFrequency_thenThrowError() {
        // when
        Mockito.when(repository.find("family")).thenReturn(Optional.ofNullable(Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build()));

        // then
        assertThrows(
                IOException.class,
                () -> service.add(Group.builder().name("family").frequencyInDays(100).build())
        );
    }

    //endregion

    //region remove group
    @Test
    void whenCalledWithNameOfAnExistingGroup_thenRemoveGroup() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.find(group.getName())).thenReturn(Optional.of(group));

        // when
        service.remove(group.getName());

        // then
        Mockito.verify(repository, Mockito.times(1)).delete(group.getName());

    }

    @Test
    void whenCalledWithNameOfANonExistingGroup_thenThrowException() throws IOException {
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

    //region update group

    @Test
    void whenGroupWithNameExists_thenUpdateGroup() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.find(group.getName())).thenReturn(Optional.of(group));
        // when
        service.update(Group.builder().name("family").frequencyInDays(20).build());
        // then
        Mockito.verify(repository, Mockito.times(1)).save(groupCaptor.capture());
        Group capturedGroup = groupCaptor.getValue();
        assertEquals(20, capturedGroup.getFrequencyInDays().intValue());
    }

    @Test
    void whenGroupDoesNotChange_thenDoNotUpdateGroup() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.find(group.getName())).thenReturn(Optional.of(group));
        // when
        service.update(Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build());
        // then
        Mockito.verify(repository, Mockito.times(0)).save(any());
    }

    @Test
    void whenGroupWithNameDoesNotExist_thenThrowExceptionOnUpdate() {
        // given
        String groupName = "family";
        // when
        Mockito.when(repository.find(groupName)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.update(Group.builder().name(groupName).frequencyInDays(DEFAULT_FREQUENCY).build()));
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
    void whenGroupWithNameExists_thenReturnGroupOnGet() throws IOException {
        // given
        Group group = Group.builder().name("family").frequencyInDays(DEFAULT_FREQUENCY).build();
        Mockito.when(repository.find(group.getName())).thenReturn(Optional.of(group));
        // when
        Group returnedGroup = service.get(group.getName());
        // then
        assertEquals(returnedGroup, group);
    }

    @Test
    void whenGroupWithNameDoesNotExist_thenThrowExceptionOnGet() {
        // given
        String name = "nonExistingName";
        // when
        Mockito.when(repository.find(name)).thenReturn(Optional.empty());
        // then
        assertThrows(IOException.class, () -> service.get(name));
    }
    //endregion

}