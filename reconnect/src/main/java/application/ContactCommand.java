package application;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;

import domain.contact.Contact;
import framework.ContactFileRepository;
import framework.ContactFileService;
import framework.GroupFileRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "contact")
public class ContactCommand implements Callable<Integer> {
    GroupFileRepository groupRepository = new GroupFileRepository("src/main/resources/", "groups.csv");
    ContactFileRepository contactRepository = new ContactFileRepository("src/main/resources/", "contacts.csv");
    ContactFileService contactService = new ContactFileService(contactRepository, groupRepository);

    @Override
    public Integer call() {
        ShellApplication.println("contact command called");
        return 0;
    }

    //region list
    @Command(name = "list")
    public Integer list(
            @Parameters(arity = "0..1", paramLabel = "CONTACT_NICKNAME") String nickName) {
        if (nickName != null) {
            return listByName(nickName.trim());
        } else {
            return listAll();
        }
    }


    private Integer listByName(final String name) {
        try {
            Contact contact = contactService.get(name);
            ShellApplication.println(contact.toHumanReadableString());
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return null;
    }

    private Integer listAll() {
        Set<Contact> allContacts = contactService.getAll();
        ShellApplication.println("Contacts: (" + allContacts.size() + ")\n");
        allContacts.stream().map(Contact::toHumanReadableString).forEach(contact -> ShellApplication.println(contact + "\n"));
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