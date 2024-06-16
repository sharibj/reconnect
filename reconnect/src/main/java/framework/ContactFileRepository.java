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
        if (tokens.size() < 1) {
            // ignore faulty lines
            return null;
        }
        Contact.ContactBuilder contactBuilder = Contact.builder()
                .nickName(tokens.get(0));
        return tokens.size() == 2 ? contactBuilder.group(tokens.get(1)).build() : contactBuilder.build();
    }

    private String contactToLine(Contact contact) {
        return contact.getNickName() + DELIMITER + contact.getGroup();
    }


    @Override
    public Optional<Contact> find(final String nickName) {
        return contacts.stream().filter(contact -> contact.getNickName().equals(nickName)).findFirst();
    }

    @Override
    public List<Contact> findAll() {
        return new ArrayList<>(contacts);
    }

    @Override
    public Contact save(final Contact contact) {
        delete(contact.getNickName());
        contacts.add(contact);
        return contact;
    }

    @Override
    public Contact delete(final String nickName) {
        Optional<Contact> matchingContact = contacts.stream().filter(savedContact -> nickName.equals(savedContact.getNickName())).findFirst();
        matchingContact.ifPresent(contacts::remove);
        return matchingContact.orElse(null);
    }

    public void commit() throws IOException {
        List<String> lines = contacts.stream().map(this::contactToLine).toList();
        FileRepositoryUtils.appendLines(lines, filePath, fileName);
    }
}
