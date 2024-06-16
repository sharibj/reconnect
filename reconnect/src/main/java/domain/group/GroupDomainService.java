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

    public void add(String name) throws IOException {
        add(name, DEFAULT_FREQUENCY);
    }

    public void add(String name, Integer frequencyInDays) throws IOException {
        if (repository.find(name).isPresent()) {
            throw new IOException("Group with name " + name + " already exists");
        }
        repository.save(Group.builder().name(name).frequencyInDays(frequencyInDays).build());
    }

    public void remove(String name) throws IOException {
        if (repository.find(name).isEmpty()) {
            throw new IOException("Group with name = " + name + " doesn't exist");
        }
        repository.delete(name);
    }

    public Set<Group> getAll() {
        return new HashSet<>(repository.findAll());
    }

    public Group get(final String name) throws IOException {
        return repository
                .find(name)
                .orElseThrow(() -> new IOException("Group with name = " + name + " does not exist."));
    }

    public void update(final String name, final int frequencyInDays) throws IOException {
        Group existingGroup = get(name);
        if (existingGroup.getFrequencyInDays().equals(frequencyInDays)) {
            return;
        }
        Group updatedGroup = Group.builder()
                .name(name)
                .frequencyInDays(frequencyInDays)
                .build();
        repository.save(updatedGroup);
    }
}
