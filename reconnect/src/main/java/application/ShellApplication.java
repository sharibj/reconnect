package application;

import java.util.Set;
import domain.group.Group;
import domain.group.GroupDomainService;
import domain.group.GroupRepository;
import framework.GroupFileRepository;

public class ShellApplication {

    GroupRepository groupRepository = new GroupFileRepository("src/main/resources/", "groups.csv");
    GroupDomainService groupDomainService = new GroupDomainService(groupRepository);

    public void listGroups() {
        Set<Group> allGroups = groupDomainService.getAll();
        System.out.println("Groups: (" + allGroups.size() + ")\n");
        allGroups.stream().map(Group::toHumanReadableString).forEach(group -> System.out.println(group + "\n"));
    }
}
