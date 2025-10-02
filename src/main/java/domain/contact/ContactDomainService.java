package domain.contact;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import domain.group.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactDomainService {

    ContactRepository repository;
    GroupRepository groupRepository;

    @Autowired
    public ContactDomainService(final ContactRepository repository, final GroupRepository groupRepository) {
        this.repository = repository;
        this.groupRepository = groupRepository;
    }

    public void add(final Contact contact) throws IOException {
        Contact updatedContact = contact.toBuilder().nickName(contact.getNickName().toLowerCase()).build();
        validateContactDoesNotExist(updatedContact.getNickName());
        validateGroupExists(updatedContact.getGroup());
        repository.save(updatedContact);
    }

    public void remove(final String nickName) throws IOException {
		String lowerNickName = nickName.toLowerCase();
        validateContactExists(lowerNickName);
        repository.delete(lowerNickName);
    }

    public void update(final Contact contact) throws IOException {
		Contact updatedContact = contact.toBuilder().nickName(contact.getNickName().toLowerCase()).build();
        validateGroupExists(updatedContact.getGroup());
        Contact existingContact = get(updatedContact
                .getNickName())
                .orElseThrow(() -> new IOException("Contact not found: " + updatedContact.getNickName()));
        if (!existingContact.equals(updatedContact)) {
            repository.save(updatedContact);
        }
    }

    public Optional<Contact> get(final String nickName) {
        return repository.find(nickName.toLowerCase());
    }

    public Set<Contact> getAll(final String groupName) {
        return repository.findAll().stream()
                .filter(contact -> contact.getGroup().equals(groupName.toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<Contact> getAll() {
        return new HashSet<>(repository.findAll());
    }

    private void validateContactExists(final String nickName) throws IOException {
        if (isNotBlank(nickName) && repository.find(nickName).isEmpty()) {
            throw new IOException("Contact not found: " + nickName);
        }
    }

    private void validateContactDoesNotExist(final String nickName) throws IOException {
        if (isNotBlank(nickName) && repository.find(nickName).isPresent()) {
            throw new IOException("Contact found: " + nickName);
        }
    }

    private void validateGroupExists(final String group) throws IOException {
        if (isNotBlank(group) && groupRepository.find(group).isEmpty()) {
            throw new IOException("Group not found: " + group);
        }
    }

    private boolean isNotBlank(final String str) {
        return null != str && !str.isBlank();
    }
}
