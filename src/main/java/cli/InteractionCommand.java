package cli;

import static cli.ShellApplication.filePath;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    ContactFileRepository contactRepository = new ContactFileRepository(filePath, "contacts.csv");
    InteractionFileRepository interactionRepository = new InteractionFileRepository(filePath, "interactions.csv");
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
            interactions.stream().sorted(Comparator.comparing(Interaction::getTimeStamp).reversed()).forEach(interaction -> ShellApplication.println(interaction.toHumanReadableString() + "\n"));
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return null;
    }

    private Integer listAll() {
        List<Interaction> allInteractions = interactionService.getAll();
        ShellApplication.println("Interactions: (" + allInteractions.size() + ")\n");
        allInteractions.stream().sorted(Comparator.comparing(Interaction::getTimeStamp).reversed()).map(Interaction::toHumanReadableString).forEach(interaction -> ShellApplication.println(interaction + "\n"));
        return 0;
    }
    //endregion

    //region add
    @Command(name = "add")
    public Integer add(@Parameters(arity = "1", paramLabel = "CONTACT_NICKNAME") String contact,
            @Parameters(arity = "0..1", paramLabel = "DATE (dd-mm-yyyy)") String dateString,
            @Option(names = { "-n", "--notes" }, arity = "0..1", paramLabel = "NOTES") String notes) {

        try {
            if (null == notes) {
                notes = "";
            }
            Date date = new Date();
            if (null != dateString) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                date = formatter.parse(dateString);
            }
            interactionService.add(Interaction.builder().contact(contact).timeStamp(date.getTime()).notes(notes).build());
        } catch (Exception e) {
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
            @Parameters(arity = "0..1", paramLabel = "DATE (dd-mm-yyyy)") String dateString,
            @Option(names = { "-n", "--notes" }, arity = "0..1", paramLabel = "NOTES") String notes) {

        try {
            Interaction interaction = interactionService.get(id);
            Interaction.InteractionBuilder interactionBuilder = Interaction.builder().id(id);
            interactionBuilder = contactName == null ? interactionBuilder.contact(interaction.getContact()) : interactionBuilder.contact(contactName);
            interactionBuilder = dateString == null ? interactionBuilder.timeStamp(interaction.getTimeStamp()) : interactionBuilder.timeStamp(new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(dateString).getTime());
            interactionBuilder = notes == null ? interactionBuilder.notes(interaction.getNotes()) : interactionBuilder.notes(notes);
            interactionService.update(interactionBuilder.build());
        } catch (
                Exception e) {
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
