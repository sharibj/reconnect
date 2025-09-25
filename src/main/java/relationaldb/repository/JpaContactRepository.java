package relationaldb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import relationaldb.entity.ContactEntity;
import java.util.Optional;

@Repository
public interface JpaContactRepository extends JpaRepository<ContactEntity, String> {
    Optional<ContactEntity> findByNickName(String nickName);
}
