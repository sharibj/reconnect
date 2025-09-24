package filedb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import domain.contact.Contact;
import domain.contact.ContactRepository;
import domain.contact.ContactDetails;
import lombok.SneakyThrows;

public class ContactFileRepository implements ContactRepository {
    private static final String DELIMITER = "±";
    private final List<Contact> contacts = new ArrayList<>();
    private final String filePath;
    private final String fileName;

    public ContactFileRepository(final String filePath, final String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
        loadContacts(filePath, fileName);
    }

    @SneakyThrows
    private void loadContacts(String filePath, String fileName) {
        FileRepositoryUtils.readLines(filePath, fileName).stream()
                .map(this::lineToContact)
                .filter(Objects::nonNull)
                .forEach(contacts::add);
    }

    private Contact lineToContact(String line) {
        List<String> tokens = FileRepositoryUtils.readTokens(line, DELIMITER);
        if (tokens.isEmpty()) {
            return null;
        }
        Contact.ContactBuilder contactBuilder = Contact.builder()
                .nickName(tokens.get(0));

        if (tokens.size() > 1) {
            contactBuilder.group(tokens.get(1));
        }

        if (tokens.size() > 3) {
            ContactDetails details = new ContactDetails();
            details.setFirstName(tokens.get(2));
            details.setLastName(tokens.get(3));
            contactBuilder.details(details);
        }

        return contactBuilder.build();
    }

    private String contactToLine(Contact contact) {
        StringBuilder line = new StringBuilder(contact.getNickName() + DELIMITER + contact.getGroup());
        
        if (contact.getDetails() != null) {
            line.append(DELIMITER)
                .append(contact.getDetails().getFirstName())
                .append(DELIMITER)
                .append(contact.getDetails().getLastName());
        }
        
        return line.toString();
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
    public Contact save(Contact contact) {
        contacts.removeIf(c -> c.getNickName().equals(contact.getNickName()));
        contacts.add(contact);
        return contact;
    }

    @Override
    public Contact delete(String nickName) {
        Optional<Contact> contact = find(nickName);
        if (contact.isPresent()) {
            contacts.remove(contact.get());
        }
        return contact.orElse(null);
    }

    public void commit() throws IOException {
        List<String> lines = contacts.stream().map(this::contactToLine).toList();
        FileRepositoryUtils.appendLines(lines, filePath, fileName);
    }
}
