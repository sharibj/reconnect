package adapter.secondary.persistence.file.multitenant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import domain.contact.Contact;
import domain.contact.ContactRepository;
import domain.contact.ContactDetails;
import adapter.secondary.persistence.file.FileRepositoryUtils;
import lombok.SneakyThrows;

public class TenantContactFileRepository implements ContactRepository {
    private static final String DELIMITER = "±";
    private static final String FILE_NAME = "contacts.txt";
    private final List<Contact> contacts = new ArrayList<>();
    private boolean loaded = false;

    public TenantContactFileRepository() {
        // Don't load contacts during construction - load them lazily when needed
    }

    @SneakyThrows
    private void ensureLoaded() {
        if (!loaded) {
            loadContacts();
            loaded = true;
        }
    }

    @SneakyThrows
    private void loadContacts() {
        contacts.clear();
        try {
            FileRepositoryUtils.readLines(TenantFileManager.getTenantDirectory(), FILE_NAME).stream()
                    .map(this::lineToContact)
                    .filter(Objects::nonNull)
                    .forEach(contacts::add);
        } catch (IOException e) {
            // File doesn't exist yet for this tenant - that's OK
        }
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
        StringBuilder sb = new StringBuilder();
        sb.append(contact.getNickName()).append(DELIMITER);
        sb.append(contact.getGroup()).append(DELIMITER);
        
        if (contact.getDetails() != null) {
            sb.append(contact.getDetails().getFirstName() != null ? contact.getDetails().getFirstName() : "").append(DELIMITER);
            sb.append(contact.getDetails().getLastName() != null ? contact.getDetails().getLastName() : "");
        }
        
        return sb.toString();
    }

    @Override
    public List<Contact> findAll() {
        ensureLoaded();
        return new ArrayList<>(contacts);
    }

    @Override
    public Optional<Contact> find(String nickName) {
        ensureLoaded();
        return contacts.stream()
                .filter(contact -> contact.getNickName().equals(nickName))
                .findFirst();
    }

    @Override
    public Contact save(Contact contact) {
        ensureLoaded();
        contacts.removeIf(c -> c.getNickName().equals(contact.getNickName()));
        contacts.add(contact);
        saveContacts();
        return contact;
    }

    @Override
    public Contact delete(String nickName) {
        ensureLoaded();
        Optional<Contact> contact = contacts.stream()
                .filter(c -> c.getNickName().equals(nickName))
                .findFirst();
        
        if (contact.isPresent()) {
            contacts.removeIf(c -> c.getNickName().equals(nickName));
            saveContacts();
            return contact.get();
        }
        return null;
    }

    @SneakyThrows
    private void saveContacts() {
        List<String> lines = contacts.stream()
                .map(this::contactToLine)
                .toList();
        FileRepositoryUtils.writeLines(lines, TenantFileManager.getTenantDirectory(), FILE_NAME);
    }
}
