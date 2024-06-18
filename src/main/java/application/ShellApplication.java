package application;

import java.util.List;
import java.util.concurrent.Callable;

import domain.ReconnectModel;
import domain.ReconnectDomainService;
import framework.ContactFileRepository;
import framework.ContactFileService;
import framework.GroupFileRepository;
import framework.GroupFileService;
import framework.InteractionFileRepository;
import framework.InteractionFileService;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "reconnect",
        subcommands = { GroupCommand.class, ContactCommand.class, InteractionCommand.class }, mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Stay connected by never loosing touch with your contacts.")
public class ShellApplication implements Callable<Integer> {

    @Parameters(defaultValue = "./", hidden = true)
    static String filePath = "";
    public static final String GROUPS_FILE = "groups.csv";
    public static final String CONTACTS_FILE = "contacts.csv";
    public static final String INTERACTIONS_FILE = "interactions.csv";

    @Override
    public Integer call() {
        GroupFileRepository groupRepository = new GroupFileRepository(filePath, GROUPS_FILE);
        ContactFileRepository contactRepository = new ContactFileRepository(filePath, CONTACTS_FILE);
        InteractionFileRepository interactionRepository = new InteractionFileRepository(filePath, INTERACTIONS_FILE);

        GroupFileService groupService = new GroupFileService(groupRepository);
        //TODO Consider using group service instead of repository
        ContactFileService contactService = new ContactFileService(contactRepository, groupRepository);
        InteractionFileService interactionService = new InteractionFileService(interactionRepository, contactRepository);

        ReconnectDomainService reconnectDomainService = new ReconnectDomainService(interactionService, contactService, groupService);
        return listOutOfTouch(reconnectDomainService);
    }

    static void println(String line) {
        System.out.println(line);
    }

    public static void main(String[] args) {
        int rc = new CommandLine(new ShellApplication()).execute(args);
        System.exit(rc);
    }

    private Integer listOutOfTouch(final ReconnectDomainService reconnectDomainService) {
        List<ReconnectModel> outOfTouchList = reconnectDomainService.getOutOfTouchContactList();
        println("Out of touch contacts (" + outOfTouchList.size() + ")\n");
        outOfTouchList.stream().map(ReconnectModel::toHumanReadableString).forEach(contact -> ShellApplication.println(contact + "\n"));
        return 0;
    }
}
