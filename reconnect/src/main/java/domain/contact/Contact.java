package domain.contact;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class Contact {

    @Builder.Default
    String id = UUID.randomUUID().toString();
    @NonNull
    String nickName;
    String groupId;
    ContactDetails details;
}
