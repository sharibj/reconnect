package cli;

import domain.ReconnectDomainService;
import domain.ReconnectModel;
import adapter.secondary.persistence.file.ContactFileRepository;
import adapter.secondary.persistence.file.ContactFileService;
import adapter.secondary.persistence.file.GroupFileRepository;
import adapter.secondary.persistence.file.GroupFileService;
import adapter.secondary.persistence.file.InteractionFileRepository;
import adapter.secondary.persistence.file.InteractionFileService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class DesktopApplication extends Application {

    private static final String FILE_PATH = "./";
    private static final String GROUPS_FILE = "groups.csv";
    private static final String CONTACTS_FILE = "contacts.csv";
    private static final String INTERACTIONS_FILE = "interactions.csv";

    @Override
    public void start(Stage primaryStage) {
        GroupFileRepository groupRepository = new GroupFileRepository(FILE_PATH, GROUPS_FILE);
        ContactFileRepository contactRepository = new ContactFileRepository(FILE_PATH, CONTACTS_FILE);
        InteractionFileRepository interactionRepository = new InteractionFileRepository(FILE_PATH, INTERACTIONS_FILE);

        GroupFileService groupService = new GroupFileService(groupRepository, contactRepository);
        ContactFileService contactService = new ContactFileService(contactRepository, groupRepository);
        InteractionFileService interactionService = new InteractionFileService(interactionRepository, contactRepository);

        ReconnectDomainService reconnectDomainService = new ReconnectDomainService(interactionService, contactService, groupService);

        List<ReconnectModel> outOfTouchList = reconnectDomainService.getOutOfTouchContactList();

        ListView<String> listView = new ListView<>();
        outOfTouchList.stream()
                .map(ReconnectModel::toHumanReadableString)
                .forEach(contact -> listView.getItems().add(contact));

        VBox vbox = new VBox(listView);
        Scene scene = new Scene(vbox, 300, 400);

        primaryStage.setTitle("Out of Touch Contacts");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}