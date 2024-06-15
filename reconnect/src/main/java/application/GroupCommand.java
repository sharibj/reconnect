package application;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;

import domain.group.Group;
import domain.group.GroupDomainService;
import domain.group.GroupRepository;
import framework.GroupFileRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "group")
public class GroupCommand implements Callable<Integer> {
    GroupRepository groupRepository = new GroupFileRepository("src/main/resources/", "groups.csv");
    GroupDomainService groupDomainService = new GroupDomainService(groupRepository);

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
            Group group = groupDomainService.getById(id);
            System.out.println(group.toHumanReadableString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return 1;
        }
        return null;
    }

    private Integer listByName(final String name) {
        try {
            Group group = groupDomainService.getByName(name);
            System.out.println(group.toHumanReadableString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return 1;
        }
        return null;
    }

    private Integer listAll() {
        Set<Group> allGroups = groupDomainService.getAll();
        System.out.println("Groups: (" + allGroups.size() + ")\n");
        allGroups.stream().map(Group::toHumanReadableString).forEach(group -> System.out.println(group + "\n"));
        return 0;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("group command called");
        return 0;
    }
}
