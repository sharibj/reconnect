package domain.group;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GroupDomainService {


    GroupRepository repository;

    public GroupDomainService(GroupRepository repository) {
        this.repository = repository;
    }

    public void add(Group group) throws IOException {
        if (repository.find(group.getName()).isPresent()) {
            throw new IOException("Group with name " + group.getName() + " already exists");
        }
        repository.save(Group.builder().name(group.getName()).frequencyInDays(group.getFrequencyInDays()).build());
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

    public void update(Group group) throws IOException {
        if (!get(group.getName()).equals(group)) {
            repository.save(group);
        }
    }
}
