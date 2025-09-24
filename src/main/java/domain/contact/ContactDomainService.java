package domain.contact;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import domain.group.GroupRepository;

public class ContactDomainService {

    ContactRepository repository;
    GroupRepository groupRepository;

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
        validateContactExists(nickName);
        repository.delete(nickName);
    }

    public void update(final Contact contact) throws IOException {
        validateGroupExists(contact.getGroup());
        Contact existingContact = get(contact
                .getNickName())
                .orElseThrow(() -> new IOException("Contact not found: " + contact.getNickName()));
        if (!existingContact.equals(contact)) {
            repository.save(contact);
        }
    }

    public Optional<Contact> get(final String nickName) {
        return repository.find(nickName.toLowerCase());
    }

    public Set<Contact> getAll(final String groupName) {
        return repository.findAll().stream()
                .filter(contact -> contact.getGroup().equals(groupName))
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
