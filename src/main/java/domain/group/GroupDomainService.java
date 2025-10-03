package domain.group;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import domain.contact.ContactRepository;

@Service
public class GroupDomainService {


    GroupRepository repository;
    ContactRepository contactRepository;

    @Autowired
    public GroupDomainService(GroupRepository repository, ContactRepository contactRepository) {
        this.repository = repository;
        this.contactRepository = contactRepository;
    }

    public void add(Group group) throws IOException {
		Group updatedGroup = group.toBuilder().name(group.getName().toLowerCase()).build();
        if (repository.find(updatedGroup.getName()).isPresent()) {
            throw new IOException("Group with name " + updatedGroup.getName() + " already exists");
        }
        repository.save(updatedGroup);
    }

    public void remove(String name) throws IOException {
        if (repository.find(name.toLowerCase()).isEmpty()) {
            throw new IOException("Group with name = " + name + " doesn't exist");
        }
        
        // Check if any contacts are using this group
        boolean hasContacts = contactRepository.findAll().stream()
                .anyMatch(contact -> name.equalsIgnoreCase(contact.getGroup()));
        
        if (hasContacts) {
            throw new IOException("Cannot delete group '" + name + "' as it has contacts associated with it");
        }
        
        repository.delete(name.toLowerCase());
    }

    public Set<Group> getAll() {
        return new HashSet<>(repository.findAll());
    }

    public Group get(final String name) throws IOException {
        return repository
                .find(name.toLowerCase())
                .orElseThrow(() -> new IOException("Group with name = " + name + " does not exist."));
    }

    public void update(Group group) throws IOException {
		Group updatedGroup = group.toBuilder().name(group.getName().toLowerCase()).build();
        Group existingGroup = repository.find(updatedGroup.getName())
                .orElseThrow(() -> new IOException("Group with name = " + updatedGroup.getName() + " does not exist."));

        // Only save if the group has actually changed
        if (!existingGroup.equals(updatedGroup)) {
            repository.save(updatedGroup);
        }
    }
}
