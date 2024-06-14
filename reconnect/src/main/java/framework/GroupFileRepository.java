package framework;

import domain.group.Group;
import domain.group.GroupRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupFileRepository implements GroupRepository {

    private static final String DELIMITER = ",";
    private final List<Group> groups = new ArrayList<>();
    private final String filePath;
    private final String fileName;

    public GroupFileRepository(final String filePath, final String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
        loadGroups(filePath, fileName);
    }

    private void loadGroups(String filePath, String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            lines = FileRepositoryUtils.readLines(filePath, fileName);
        } catch (IOException exception) {
            // Ignore Exceptions
        }
        lines.forEach(this::lineToGroup);
    }

    private void lineToGroup(String line) {
        List<String> tokens = FileRepositoryUtils.readTokens(line, DELIMITER);
        if (tokens.size() != 3) {
            // ignore faulty lines
            return;
        }
        groups.add(Group.builder()
                .id(tokens.get(0))
                .name(tokens.get(1))
                .frequencyInDays(Integer.parseInt(tokens.get(2)))
                .build());
    }

    private String groupToLine(Group group) {
        return group.getId() + "," + group.getName() + "," + group.getFrequencyInDays();
    }

    @Override
    public Optional<Group> findById(final String id) {
        return groups.stream().filter(group -> group.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Group> findByName(final String name) {
        return groups.stream().filter(group -> group.getName().equals(name)).findFirst();
    }

    @Override
    public List<Group> findAll() {
        return new ArrayList<>(groups);
    }

    @Override
    public Group save(final Group group) {
        deleteById(group.getId());
        groups.add(group);
        return group;
    }

    @Override
    public Group deleteById(final String id) {
        Optional<Group> matchingGroup = groups.stream().filter(savedGroup -> id.equals(savedGroup.getId())).findFirst();
        if (matchingGroup.isPresent()) {
            groups.remove(matchingGroup.get());
        }
        return matchingGroup.orElse(null);
    }

    @Override
    public Group deleteByName(final String name) {
        Optional<Group> matchingGroup = groups.stream().filter(savedGroup -> name.equals(savedGroup.getName())).findFirst();
        if (matchingGroup.isPresent()) {
            groups.remove(matchingGroup.get());
        }
        return matchingGroup.orElse(null);
    }

    public void commit() throws IOException {
        List<String> lines = groups.stream().map(this::groupToLine).toList();
        FileRepositoryUtils.appendLines(lines, filePath, fileName);
    }
}
