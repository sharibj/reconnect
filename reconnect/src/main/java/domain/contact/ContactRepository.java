package domain.contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {

    Optional<Contact> find(String nickName);

    List<Contact> findAll();

    Contact save(Contact contact);

    Contact delete(String nickName);
}
