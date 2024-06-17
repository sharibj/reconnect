package framework;

import java.io.IOException;

import domain.contact.Contact;
import domain.contact.ContactDomainService;

public class ContactFileService extends ContactDomainService {
    private final ContactFileRepository contactFileRepository;

    public ContactFileService(final ContactFileRepository contactFileRepository, final GroupFileRepository groupFileRepository) {
        super(contactFileRepository, groupFileRepository);
        this.contactFileRepository = contactFileRepository;
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
