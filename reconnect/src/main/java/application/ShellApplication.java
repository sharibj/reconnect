package application;

import java.util.List;
import java.util.concurrent.Callable;

import domain.ContactInteraction;
import domain.ReconnectDomainService;
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
    public static final String GROUPS_FILE = "groups.csv";
    public static final String CONTACTS_FILE = "contacts.csv";
    public static final String INTERACTIONS_FILE = "interactions.csv";

    private final ReconnectDomainService reconnectDomainService;

    public ShellApplication() {
        GroupFileRepository groupRepository = new GroupFileRepository(FILE_PATH, GROUPS_FILE);
        ContactFileRepository contactRepository = new ContactFileRepository(FILE_PATH, CONTACTS_FILE);
        InteractionFileRepository interactionRepository = new InteractionFileRepository(FILE_PATH, INTERACTIONS_FILE);

        GroupFileService groupService = new GroupFileService(groupRepository);
//      //TODO Consider using group service instead of repository
        ContactFileService contactService = new ContactFileService(contactRepository, groupRepository);
        InteractionFileService interactionService = new InteractionFileService(interactionRepository, contactRepository);

        reconnectDomainService = new ReconnectDomainService(interactionService, contactService, groupService);
    }

    @Override
    public Integer call() throws Exception {
        return listOutOfTouch();
    }

    static void println(String line) {
        System.out.println(line);
    }

    public static void main(String[] args) {
        int rc = new CommandLine(new ShellApplication()).execute(args);
        System.exit(rc);
    }

    private Integer listOutOfTouch() {
        List<ContactInteraction> outOfTouchList = reconnectDomainService.getOutOfTouchContactList();
        println("Out of touch contacts (" + outOfTouchList.size() + ")\n");
        outOfTouchList.stream().map(ContactInteraction::toHumanReadableString).forEach(contact -> ShellApplication.println(contact + "\n"));
        return 0;
    }
}
