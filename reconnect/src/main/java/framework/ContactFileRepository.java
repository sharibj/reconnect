package framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import domain.contact.Contact;
import domain.contact.ContactRepository;

public class ContactFileRepository implements ContactRepository {
    private static final String DELIMITER = ",";
    private final List<Contact> contacts = new ArrayList<>();
    private final String filePath;
    private final String fileName;

    public ContactFileRepository(final String filePath, final String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
        loadContacts(filePath, fileName);
    }

    private void loadContacts(String filePath, String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            lines = FileRepositoryUtils.readLines(filePath, fileName);
        } catch (IOException exception) {
            // Ignore Exceptions
        }
        lines.stream().map(this::lineToContact).filter(Objects::nonNull).forEach(contacts::add);
    }

    private Contact lineToContact(String line) {
        List<String> tokens = FileRepositoryUtils.readTokens(line, DELIMITER);
        if (tokens.size() < 2) {
            // ignore faulty lines
            return null;
        }
        return Contact.builder()
                .id(tokens.get(0))
                .nickName(tokens.get(1))
                .build();
    }

    private String contactToLine(Contact contact) {
        return contact.getId() + "," + contact.getNickName();
    }


    @Override
    public Optional<Contact> findById(final String id) {
        return contacts.stream().filter(contact -> contact.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Contact> findByName(final String nickName) {
        return contacts.stream().filter(contact -> contact.getNickName().equals(nickName)).findFirst();
    }

    @Override
    public List<Contact> findAll() {
        return new ArrayList<>(contacts);
    }

    @Override
    public Contact save(final Contact contact) {
        deleteById(contact.getId());
        contacts.add(contact);
        return contact;
    }

    @Override
    public Contact deleteById(final String id) {
        Optional<Contact> matchingContact = contacts.stream().filter(savedContact -> id.equals(savedContact.getId())).findFirst();
        matchingContact.ifPresent(contacts::remove);
        return matchingContact.orElse(null);
    }

    @Override
    public Contact deleteByName(final String nickName) {
        Optional<Contact> matchingContact = contacts.stream().filter(savedContact -> nickName.equals(savedContact.getNickName())).findFirst();
        matchingContact.ifPresent(contacts::remove);
        return matchingContact.orElse(null);
    }

    public void commit() throws IOException {
        List<String> lines = contacts.stream().map(this::contactToLine).toList();
        FileRepositoryUtils.appendLines(lines, filePath, fileName);
    }
}
