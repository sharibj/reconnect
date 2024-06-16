package domain.contact;

import java.io.IOException;

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
        if (isNotBlank(contact.group) && groupRepository.find(contact.group).isEmpty()) {
            throw new IOException("Group with name = " + contact.group + " does not exist.");
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
        Contact existingContact = get(contact.getNickName());
        if (existingContact.getGroup().equals(contact.group)) {
            return;
        }
        repository.save(contact);
    }

    public Contact get(final String nickName) throws IOException {
        return repository
                .find(nickName)
                .orElseThrow(() -> new IOException("Contact with name = " + nickName + " does not exist."));
    }
}
