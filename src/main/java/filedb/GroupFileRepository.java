package filedb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import domain.group.Group;
import domain.group.GroupRepository;

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
        try {
            List<String> lines = FileRepositoryUtils.readLines(filePath, fileName);
            lines.stream()
                .map(this::lineToGroup)
                .filter(Objects::nonNull)
                .forEach(groups::add);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load groups from file: " + filePath + "/" + fileName, e);
        }
    }

    private Group lineToGroup(String line) {
        try {
            List<String> tokens = FileRepositoryUtils.readTokens(line, DELIMITER);
            if (tokens.size() != 2) {
                return null;
            }
            return Group.builder()
                    .name(tokens.get(0))
                    .frequencyInDays(Integer.parseInt(tokens.get(1)))
                    .build();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String groupToLine(Group group) {
        return group.getName() + DELIMITER + group.getFrequencyInDays();
    }

    @Override
    public Optional<Group> find(final String name) {
        return groups.stream().filter(group -> group.getName().equals(name)).findFirst();
    }

    @Override
    public List<Group> findAll() {
        return new ArrayList<>(groups);
    }

    @Override
    public Group save(final Group group) {
        delete(group.getName());
        groups.add(group);
        return group;
    }

    @Override
    public Group delete(final String name) {
        Optional<Group> matchingGroup = groups.stream().filter(savedGroup -> name.equals(savedGroup.getName())).findFirst();
        matchingGroup.ifPresent(groups::remove);
        return matchingGroup.orElse(null);
    }

    public void commit() throws IOException {
        try {
            List<String> lines = groups.stream().map(this::groupToLine).toList();
            FileRepositoryUtils.appendLines(lines, filePath, fileName);
        } catch (IOException e) {
            throw new IOException("Failed to commit groups to file: " + filePath + "/" + fileName, e);
        }
    }
}
