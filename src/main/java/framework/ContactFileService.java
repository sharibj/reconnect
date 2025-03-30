package framework;

import java.io.IOException;

import domain.contact.Contact;
import domain.contact.ContactDomainService;
import domain.contact.ContactRepository;
import domain.group.GroupRepository;

public class ContactFileService extends ContactDomainService {
    private final ContactFileRepository contactFileRepository;

    public ContactFileService(final ContactRepository contactRepository, final GroupRepository groupRepository) {
        super(contactRepository, groupRepository);
        this.contactFileRepository = (ContactFileRepository) contactRepository;
    }

    @Override
    public void add(final Contact contact) throws IOException {
        super.add(contact);
        contactFileRepository.commit();
    }

    @Override
    public void update(final Contact contact) throws IOException {
        super.update(contact);
        contactFileRepository.commit();
    }

    @Override
    public void remove(final String name) throws IOException {
        super.remove(name);
        contactFileRepository.commit();
    }
}
