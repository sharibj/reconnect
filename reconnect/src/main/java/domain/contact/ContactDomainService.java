package domain.contact;

import java.io.IOException;
import java.util.HashSet;
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
        if (repository.find(contact.getNickName()).isPresent()) {
            throw new IOException("Contact with nickname = " + contact.getNickName() + " already exists.");
        }
        if (isNotBlank(contact.getGroup()) && groupRepository.find(contact.getGroup()).isEmpty()) {
            throw new IOException("Group with name = " + contact.getGroup() + " does not exist.");
        }
        repository.save(contact);
    }

    private boolean isNotBlank(final String str) {
        return null != str && !str.isBlank();
    }

    public void remove(final String nickName) throws IOException {
        if (repository.find(nickName).isEmpty()) {
            throw new IOException("Group with nickname = " + nickName + " doesn't exist");
        }
        repository.delete(nickName);
    }

    public void update(final Contact contact) throws IOException {
        if (isNotBlank(contact.getGroup()) && groupRepository.find(contact.getGroup()).isEmpty()) {
            throw new IOException("Group with name = " + contact.getGroup() + " does not exist.");
        }
        Contact existingContact = get(contact.getNickName());
        if (existingContact.equals(contact)) {
            return;
        }
        repository.save(contact);
    }

    public Contact get(final String groupName) throws IOException {
        return repository
                .find(groupName)
                .orElseThrow(() -> new IOException("Group with name = " + groupName + " does not exist."));
        //TODO Consider returning optional instead of throwing error
    }

    //TODO add test
    public Set<Contact> getAll(final String groupName) throws IOException {
        groupRepository
                .find(groupName)
                .orElseThrow(() -> new IOException("Group with name = " + groupName + " does not exist."));
        return repository.findAll().stream().filter(contact -> contact.getGroup().equals(groupName)).collect(Collectors.toSet());
    }

    public Set<Contact> getAll() {
        return new HashSet<>(repository.findAll());
    }

}
