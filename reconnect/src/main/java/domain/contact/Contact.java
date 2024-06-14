package domain.contact;

import java.util.UUID;

import domain.group.Group;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class Contact {

    @Builder.Default
    String id = UUID.randomUUID().toString();
    @NonNull
    String nickName;
    ContactDetails details;
    Group group;
}
