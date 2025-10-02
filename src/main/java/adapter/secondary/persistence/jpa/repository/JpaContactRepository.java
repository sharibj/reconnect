package adapter.secondary.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import adapter.secondary.persistence.jpa.entity.ContactEntity;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaContactRepository extends JpaRepository<ContactEntity, Long> {
    Optional<ContactEntity> findByNickNameAndUsername(String nickName, String username);
    List<ContactEntity> findByUsername(String username);
    void deleteByNickNameAndUsername(String nickName, String username);
}
