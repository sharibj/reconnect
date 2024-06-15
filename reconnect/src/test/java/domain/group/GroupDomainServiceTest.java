package domain.group;

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

@ExtendWith(MockitoExtension.class)
class GroupDomainServiceTest {

    private static final Integer DEFAULT_FREQUENCY = 7;

    @Mock
    GroupRepository repository;
    @Captor
    ArgumentCaptor<Group> groupCaptor;

    @InjectMocks
    GroupDomainService service;

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
    void whenAddGroupWithExistingName_thenThrowError() {
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
}