package cli;

import static cli.ShellApplication.filePath;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

import domain.contact.Contact;
import adapter.secondary.persistence.file.ContactFileRepository;
import adapter.secondary.persistence.file.ContactFileService;
import adapter.secondary.persistence.file.GroupFileRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "contact")
public class ContactCommand implements Callable<Integer> {
    GroupFileRepository groupRepository = new GroupFileRepository(filePath, "groups.csv");
    ContactFileRepository contactRepository = new ContactFileRepository(filePath, "contacts.csv");
    ContactFileService contactService = new ContactFileService(contactRepository, groupRepository);

    @Override
    public Integer call() {
        ShellApplication.println("contact command called");
        return 0;
    }

    //region list
    @Command(name = "list")
    public Integer list(
            @Option(names = { "-n", "nickname" }, arity = "0..1", paramLabel = "CONTACT_NICKNAME") String nickName,
            @Option(names = { "-g", "group" }, arity = "0..1", paramLabel = "GROUP_NAME") String group) {
        if (nickName != null) {
            return listByName(nickName.trim());
        } else if (group != null) {
            return listByGroup(group.trim());
        } else {
            return listAll();
        }
    }

    private Integer listByGroup(final String group) {
        Set<Contact> allContacts = contactService.getAll(group);
        ShellApplication.println("Contacts: (" + allContacts.size() + ")\n");
        allContacts.stream().map(Contact::toHumanReadableString).forEach(contact -> ShellApplication.println(contact + "\n"));
        return 0;
    }


    private Integer listByName(final String name) {
        Optional<Contact> optionalContact = contactService.get(name);
        if (optionalContact.isPresent()) {
            ShellApplication.println(optionalContact.get().toHumanReadableString());
            return 0;
        } else {
            ShellApplication.println("Contact not found: " + name);
            return 1;
        }
    }

    private Integer listAll() {
        Set<Contact> allContacts = contactService.getAll();
        ShellApplication.println("Contacts: (" + allContacts.size() + ")\n");
        allContacts.stream().sorted(Comparator.comparing(Contact::getGroup)).map(Contact::toHumanReadableString).forEach(contact -> ShellApplication.println(contact + "\n"));
        return 0;
    }
    //endregion

    //region add
    @Command(name = "add")
    public Integer add(@Parameters(arity = "1", paramLabel = "NICKNAME") String nickName,
            @Parameters(arity = "0..1", paramLabel = "GROUP") String groupName) {
        if (null == groupName) {
            groupName = "";
        }
        try {
            contactService.add(Contact.builder().nickName(nickName).group(groupName).build());
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }
    //endregion

    //region update
    @Command(name = "update")
    public Integer update(@Parameters(arity = "1", paramLabel = "NICKNAME") String nickName,
            @Parameters(arity = "1", paramLabel = "GROUP") String groupName) {
        if (null == groupName) {
            groupName = "";
        }
        try {
            contactService.update(Contact.builder().nickName(nickName).group(groupName).build());
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }
    //endregion

    //region remove
    @Command(name = "remove")
    public Integer remove(@Parameters(arity = "1", paramLabel = "NICKNAME") String nickName) {
        try {
            contactService.remove(nickName);
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }
    //endregion

}
