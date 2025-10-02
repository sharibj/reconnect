package relationaldb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import relationaldb.entity.InteractionEntity;
import java.util.List;

@Repository
public interface JpaInteractionRepository extends JpaRepository<InteractionEntity, Long> {
    List<InteractionEntity> findByContactAndUsername(String contact, String username);
    List<InteractionEntity> findByUsername(String username);
    void deleteByContactAndUsername(String contact, String username);
}
