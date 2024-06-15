package domain.group;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GroupDomainService {

    private static final Integer DEFAULT_FREQUENCY = 7;
    GroupRepository repository;

    public GroupDomainService(GroupRepository repository) {
        this.repository = repository;
    }

    public void addGroup(String name) throws IOException {
        addGroup(name, DEFAULT_FREQUENCY);
    }

    public void addGroup(String name, Integer frequencyInDays) throws IOException {
        if (repository.findByName(name).isPresent()) {
            throw new IOException("Group with name " + name + " already exists");
        }
        repository.save(Group.builder().name(name).frequencyInDays(frequencyInDays).build());
    }

    public void removeGroupById(String id) throws IOException {
        if (repository.findById(id).isEmpty()) {
            throw new IOException("Group with id = " + id + " doesn't exist");
        }
        repository.deleteById(id);
    }

    public void removeGroupByName(String name) throws IOException {
        if (repository.findByName(name).isEmpty()) {
            throw new IOException("Group with name = " + name + " doesn't exist");
        }
        repository.deleteByName(name);
    }

    public Set<Group> getAll() {
        return new HashSet<>(repository.findAll());
    }

    public Group getById(final String id) throws IOException {
        return repository
                .findById(id)
                .orElseThrow(() -> new IOException("Group with id = " + id + " does not exist."));
    }

    public Group getByName(final String name) throws IOException {
        return repository
                .findByName(name)
                .orElseThrow(() -> new IOException("Group with name = " + name + " does not exist."));
    }

    public void updateByName(final String name, final int frequencyInDays) throws IOException {
        Group group = getByName(name);
        if (group.getFrequencyInDays().equals(frequencyInDays)) {
            return;
        }
        Group updatedGroup = Group.builder()
                .id(group.getId())
                .name(group.getName())
                .frequencyInDays(frequencyInDays)
                .build();
        repository.save(updatedGroup);
    }

    public void updateById(final String id, final String name, final int frequencyInDays) throws IOException {
        Group group = getById(id);
        if (group.getName().equals(name) && group.getFrequencyInDays().equals(frequencyInDays)) {
            return;
        }
        Group updatedGroup = Group.builder()
                .id(group.getId())
                .name(name)
                .frequencyInDays(frequencyInDays)
                .build();
        repository.save(updatedGroup);

    }
}
