package domain.group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    Optional<Group> find(String name);

    List<Group> findAll();

    Group save(Group group);

    Group delete(String name);
}
