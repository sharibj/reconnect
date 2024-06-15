package application;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;

import domain.group.Group;
import framework.GroupFileRepository;
import framework.GroupFileService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "group")
public class GroupCommand implements Callable<Integer> {
    GroupFileRepository groupRepository = new GroupFileRepository("src/main/resources/", "groups.csv");
    GroupFileService groupService = new GroupFileService(groupRepository);

    //region list
    @Command(name = "list")
    public Integer list(
            @Option(names = { "-i" }, arity = "0..1", paramLabel = "GROUP_ID") String id,
            @Option(names = { "-n" }, arity = "0..1", paramLabel = "GROUP_NAME") String name) {
        if (id != null) {
            return listById(id.trim());
        } else if (name != null) {
            return listByName(name.trim());
        } else {
            return listAll();
        }
    }

    private Integer listById(final String id) {
        try {
            Group group = groupService.getById(id);
            ShellApplication.println(group.toHumanReadableString());
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    private Integer listByName(final String name) {
        try {
            Group group = groupService.getByName(name);
            ShellApplication.println(group.toHumanReadableString());
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return null;
    }

    private Integer listAll() {
        Set<Group> allGroups = groupService.getAll();
        ShellApplication.println("Groups: (" + allGroups.size() + ")\n");
        allGroups.stream().map(Group::toHumanReadableString).forEach(group -> ShellApplication.println(group + "\n"));
        return 0;
    }
    //endregion

    //region add
    @Command(name = "add")
    public Integer add(@Parameters(arity = "1", paramLabel = "GROUP_NAME") String name,
            @Parameters(arity = "1", paramLabel = "FREQUENCY_IN_DAYS") Integer frequency) {
        try {
            groupService.addGroup(name, frequency);
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    //endregion

    //region remove
    @Command(name = "remove")
    public Integer remove(@Parameters(arity = "1", paramLabel = "GROUP_NAME") String name) {
        try {
            groupService.removeGroupByName(name);
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    //endregion
    @Override
    public Integer call() throws Exception {
        ShellApplication.println("group command called");
        return 0;
    }
}
