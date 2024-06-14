package domain.group;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GroupDomainService {

    private static final Integer DEFAULT_FREQUENCY = 7;
    GroupRepository repository;

    public GroupDomainService(GroupRepository repository) {
        this.repository = repository;
    }

    public Group addGroup(String name) {
        //@TODO check if name already exists and throw exception if yes
        return repository.save(Group.builder().name(name).frequencyInDays(DEFAULT_FREQUENCY).build());
    }

    public Group addGroup(String name, Integer frequencyInDays) {
        //@TODO check if name already exists and throw exception if yes
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
