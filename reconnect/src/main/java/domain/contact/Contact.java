package domain.contact;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class Contact {
    @NonNull
    String nickName;
    String group;
    ContactDetails details;
}
