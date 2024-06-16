package application;

import java.util.List;
import java.util.concurrent.Callable;

import domain.ReconnectDomainService;
import domain.contact.Contact;
import framework.ContactFileRepository;
import framework.ContactFileService;
import framework.GroupFileRepository;
import framework.GroupFileService;
import framework.InteractionFileRepository;
import framework.InteractionFileService;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "reconnect", subcommands = { GroupCommand.class, ContactCommand.class, InteractionCommand.class }, mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Coming Soon")
public class ShellApplication implements Callable<Integer> {

    static final String FILE_PATH = "reconnect/";

    @Override
    public Integer call() throws Exception {
        println("ShellApplication Called");
        return 0;
    }

    static void println(String line) {
        System.out.println(line);
    }

    public static void main(String[] args) {
        int rc = new CommandLine(new ShellApplication()).execute(args);
        System.exit(rc);
    }

    @Command(name = "list")
    public Integer list() {

        GroupFileRepository groupRepository = new GroupFileRepository(FILE_PATH, "groups.csv");
        GroupFileService groupService = new GroupFileService(groupRepository);

        ContactFileRepository contactRepository = new ContactFileRepository(FILE_PATH, "contacts.csv");
        ContactFileService contactService = new ContactFileService(contactRepository, groupRepository);

        InteractionFileRepository interactionRepository = new InteractionFileRepository(FILE_PATH, "interactions.csv");
        InteractionFileService interactionService = new InteractionFileService(interactionRepository, contactRepository);

        ReconnectDomainService reconnectDomainService = new ReconnectDomainService(interactionService, contactService, groupService);

        List<Contact> orderedList = reconnectDomainService.getOrderedList();
        orderedList.stream().map(Contact::toHumanReadableString).forEach(contact -> ShellApplication.println(contact + "\n"));
        return 0;
    }
}
