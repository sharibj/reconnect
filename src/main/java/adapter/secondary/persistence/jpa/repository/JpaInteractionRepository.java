package adapter.secondary.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import adapter.secondary.persistence.jpa.entity.InteractionEntity;
import java.util.List;

@Repository
public interface JpaInteractionRepository extends JpaRepository<InteractionEntity, Long> {
    List<InteractionEntity> findByContact(String contact);
    void deleteByContact(String contact);
}
