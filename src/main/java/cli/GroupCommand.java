package cli;

import static cli.ShellApplication.filePath;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.Callable;

import domain.group.Group;
import framework.GroupFileRepository;
import framework.GroupFileService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "group")
public class GroupCommand implements Callable<Integer> {
    GroupFileRepository groupRepository = new GroupFileRepository(filePath, "groups.csv");
    GroupFileService groupService = new GroupFileService(groupRepository);

    @Override
    public Integer call() {
        ShellApplication.println("group command called");
        return 0;
    }

    //region list
    @Command(name = "list")
    public Integer list(
            @Parameters(arity = "0..1", paramLabel = "GROUP_NAME") String name) {
        if (name != null) {
            return listByName(name.trim());
        } else {
            return listAll();
        }
    }


    private Integer listByName(final String name) {
        try {
            Group group = groupService.get(name);
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
        allGroups.stream().sorted(Comparator.comparing(Group::getName)).map(Group::toHumanReadableString).forEach(group -> ShellApplication.println(group + "\n"));
        return 0;
    }
    //endregion

    //region add
    @Command(name = "add")
    public Integer add(@Parameters(arity = "1", paramLabel = "GROUP_NAME") String name,
            @Parameters(arity = "1", paramLabel = "FREQUENCY_IN_DAYS") Integer frequency) {
        try {
            groupService.add(Group.builder().name(name).frequencyInDays(frequency).build());
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }
    //endregion

    //region update
    @Command(name = "update")
    public Integer update(@Parameters(arity = "1", paramLabel = "GROUP_NAME") String name,
            @Parameters(arity = "1", paramLabel = "FREQUENCY_IN_DAYS") Integer frequency) {
        try {
            groupService.update(Group.builder().name(name).frequencyInDays(frequency).build());
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
            groupService.remove(name);
        } catch (IOException e) {
            ShellApplication.println(e.getMessage());
            return 1;
        }
        return 0;
    }
    //endregion

}
