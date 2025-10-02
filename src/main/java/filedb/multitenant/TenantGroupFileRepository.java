package filedb.multitenant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import domain.group.Group;
import domain.group.GroupRepository;
import filedb.FileRepositoryUtils;
import lombok.SneakyThrows;

public class TenantGroupFileRepository implements GroupRepository {
    private static final String DELIMITER = "±";
    private static final String FILE_NAME = "groups.txt";
    private final List<Group> groups = new ArrayList<>();
    private boolean loaded = false;

    public TenantGroupFileRepository() {
        // Don't load groups during construction - load them lazily when needed
    }

    @SneakyThrows
    private void ensureLoaded() {
        if (!loaded) {
            loadGroups();
            loaded = true;
        }
    }

    @SneakyThrows
    private void loadGroups() {
        groups.clear();
        try {
            FileRepositoryUtils.readLines(TenantFileManager.getTenantDirectory(), FILE_NAME).stream()
                    .map(this::lineToGroup)
                    .filter(Objects::nonNull)
                    .forEach(groups::add);
        } catch (IOException e) {
            // File doesn't exist yet for this tenant - that's OK
        }
    }

    private Group lineToGroup(String line) {
        List<String> tokens = FileRepositoryUtils.readTokens(line, DELIMITER);
        if (tokens.isEmpty()) {
            return null;
        }
        return Group.builder()
                .name(tokens.get(0))
                .build();
    }

    private String groupToLine(Group group) {
        return group.getName();
    }

    @Override
    public List<Group> findAll() {
        ensureLoaded();
        return new ArrayList<>(groups);
    }

    @Override
    public Optional<Group> find(String name) {
        ensureLoaded();
        return groups.stream()
                .filter(group -> group.getName().equals(name))
                .findFirst();
    }

    @Override
    public Group save(Group group) {
        ensureLoaded();
        groups.removeIf(g -> g.getName().equals(group.getName()));
        groups.add(group);
        saveGroups();
        return group;
    }

    @Override
    public Group delete(String name) {
        ensureLoaded();
        Optional<Group> group = groups.stream()
                .filter(g -> g.getName().equals(name))
                .findFirst();
        
        if (group.isPresent()) {
            groups.removeIf(g -> g.getName().equals(name));
            saveGroups();
            return group.get();
        }
        return null;
    }

    @SneakyThrows
    private void saveGroups() {
        List<String> lines = groups.stream()
                .map(this::groupToLine)
                .toList();
        FileRepositoryUtils.writeLines(lines, TenantFileManager.getTenantDirectory(), FILE_NAME);
    }
}
