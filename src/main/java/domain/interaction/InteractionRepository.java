package domain.interaction;

import java.util.List;
import java.util.Optional;

public interface InteractionRepository {
    List<Interaction> findAll();

    List<Interaction> findAll(String contact);

    Optional<Interaction> find(String id);

    Interaction save(Interaction interaction);

    Interaction delete(String name);
}
