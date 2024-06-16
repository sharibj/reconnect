package application;

import static application.ShellApplication.FILE_PATH;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import domain.interaction.Interaction;
import framework.ContactFileRepository;
import framework.InteractionFileRepository;
import framework.InteractionFileService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "interaction")
public class InteractionCommand implements Callable<Integer> {
    ContactFileRepository contactRepository = new ContactFileRepository(FILE_PATH, "contacts.csv");
    InteractionFileRepository interactionRepository = new InteractionFileRepository(FILE_PATH, "interactions.csv");
    InteractionFileService interactionService = new InteractionFileService(interactionRepository, contactRepository);

    @Override
    public Integer call() {
        ShellApplication.println("interaction command called");
        return 0;
    }

    //region list
    @Command(name = "list")
    public Integer list(
            @Parameters(arity = "0..1", paramLabel = "CONTACT_NICKNAME") String contact) {
        if (contact != null) {
            return listByContact(contact.trim());
        } else {
            return listAll();
        }
    }


    private Integer listByContact(final String contact) {
        try {
            List<Interaction> interactions = interactionService.getAll(contact);
            ShellApplication.println("Interactions: (" + interactions.size() + ")\n");
            interactions.forEach(interaction -> ShellApplication.println(interaction.toHumanReadableString() + "\n"));
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return null;
    }

    private Integer listAll() {
        List<Interaction> allInteractions = interactionService.getAll();
        ShellApplication.println("Interactions: (" + allInteractions.size() + ")\n");
        allInteractions.stream().map(Interaction::toHumanReadableString).forEach(interaction -> ShellApplication.println(interaction + "\n"));
        return 0;
    }
    //endregion

    //region add
    @Command(name = "add")
    public Integer add(@Parameters(arity = "1", paramLabel = "CONTACT_NICKNAME") String contact,
            @Parameters(arity = "0..1", paramLabel = "NOTES") String notes) {
        if (null == notes) {
            notes = "";
        }
        try {
            interactionService.add(Interaction.builder().contact(contact).notes(notes).build());
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }
    //endregion

    //region update
    @Command(name = "update")
    public Integer update(@Parameters(arity = "1", paramLabel = "ID") String id,
            @Option(names = { "-c", "--contact" }, arity = "0..1", paramLabel = "CONTACT_NICKNAME") String contactName,
            @Option(names = { "-n", "--notes" }, arity = "0..1", paramLabel = "NOTES") String notes) {

        try {
            Interaction interaction = interactionService.get(id);
            Interaction.InteractionBuilder interactionBuilder = Interaction.builder().id(id);
            interactionBuilder = contactName == null ? interactionBuilder.contact(interaction.getContact()) : interactionBuilder.contact(contactName);
            interactionBuilder = notes == null ? interactionBuilder.notes(interaction.getNotes()) : interactionBuilder.notes(notes);
            interactionService.update(interactionBuilder.build());
        } catch (
                IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }
//endregion

    //region remove
    @Command(name = "remove")
    public Integer remove(@Parameters(arity = "1", paramLabel = "ID") String id) {
        try {
            interactionService.remove(id);
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }
//endregion

}
