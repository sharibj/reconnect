package adapter.secondary.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import adapter.secondary.persistence.jpa.entity.GroupEntity;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaGroupRepository extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByName(String name);
    void deleteByName(String name);
    List<GroupEntity> findAllByName(String name);
}
