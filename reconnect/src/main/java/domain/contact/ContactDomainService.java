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

    public void add(final String nickName) throws IOException {
        add(nickName, "");
    }

    public void add(final String nickName, final String group) throws IOException {
        if (repository.find(nickName).isPresent()) {
            throw new IOException("Contact with nickname = " + nickName + " already exists.");
        }
        if (isNotBlank(group) && groupRepository.find(group).isEmpty()) {
            throw new IOException("Group with name = " + group + " does not exist.");
        }
        repository.save(Contact.builder().nickName(nickName).group(group).build());
    }

    private boolean isNotBlank(final String str) {
        return null != str && !str.isBlank();
    }
}
