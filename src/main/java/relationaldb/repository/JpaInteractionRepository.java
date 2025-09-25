package relationaldb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import relationaldb.entity.InteractionEntity;
import java.util.List;

@Repository
public interface JpaInteractionRepository extends JpaRepository<InteractionEntity, String> {
    List<InteractionEntity> findByContact(String contact);
}
