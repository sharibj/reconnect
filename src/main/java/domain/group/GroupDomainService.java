package domain.group;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import domain.contact.ContactRepository;

public class GroupDomainService {


    GroupRepository repository;
    ContactRepository contactRepository;

    public GroupDomainService(GroupRepository repository, ContactRepository contactRepository) {
        this.repository = repository;
        this.contactRepository = contactRepository;
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
        
        // Check if any contacts are using this group
        boolean hasContacts = contactRepository.findAll().stream()
                .anyMatch(contact -> name.equals(contact.getGroup()));
        
        if (hasContacts) {
            throw new IOException("Cannot delete group '" + name + "' as it has contacts associated with it");
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
        if (repository.find(group.getName()).isEmpty()) {
            throw new IOException("Group with name = " + group.getName() + " does not exist.");
        }
        repository.save(group);
    }
}
