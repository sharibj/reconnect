package framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import domain.group.Group;
import domain.group.GroupRepository;

public class GroupFileRepository implements GroupRepository {

    private static final String DELIMITER = ",";
    private final List<Group> groups = new ArrayList<>();

    public GroupFileRepository(final String filePath, final String fileName) {
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
        return null;
    }

    @Override
    public Group update(final Group group) {
        return null;
    }

    @Override
    public Group deleteById(final String id) {
        return null;
    }

    @Override
    public Group deleteByName(final String name) {
        return null;
    }
}
