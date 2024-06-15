package domain.group;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GroupDomainService {

    private static final Integer DEFAULT_FREQUENCY = 7;
    GroupRepository repository;

    public GroupDomainService(GroupRepository repository) {
        this.repository = repository;
    }

    public Group addGroup(String name) throws IOException {
        return addGroup(name, DEFAULT_FREQUENCY);
    }

    public Group addGroup(String name, Integer frequencyInDays) throws IOException {
        if (repository.findByName(name).isPresent()) {
            throw new IOException("Group with name " + name + " already exists");
        }
        return repository.save(Group.builder().name(name).frequencyInDays(frequencyInDays).build());
    }

    public Group removeGroupById(String id) {

        //@TODO check if id exists
        return repository.deleteById(id);
    }

    public Group removeGroupByName(String name) {
        //@TODO check if id exists
        Optional<Group> group = repository.findByName(name);
        return group.map(value -> repository.deleteById(value.getId())).orElse(null);
    }

    public Set<Group> getAll() {
        //TODO add logic for de-dupe maybe
        return new HashSet<>(repository.findAll());
    }
}
