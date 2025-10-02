package adapter.secondary.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import adapter.secondary.persistence.jpa.entity.GroupEntity;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaGroupRepository extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByNameAndUsername(String name, String username);
    List<GroupEntity> findByUsername(String username);
    void deleteByNameAndUsername(String name, String username);
}
