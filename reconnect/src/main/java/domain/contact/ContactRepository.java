package domain.contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    Optional<Contact> findById(String id);

    Optional<Contact> findByName(String nickName);

    List<Contact> findAll();

    Contact save(Contact contact);

    Contact deleteById(String id);

    Contact deleteByName(String nickName);
}
