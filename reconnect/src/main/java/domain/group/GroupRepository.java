package domain.group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    Optional<Group> findById(String id);

    Optional<Group> findByName(String name);

    List<Group> findAll();

    Group save(Group group);

    Group deleteById(String id);

    Group deleteByName(String name);
}
