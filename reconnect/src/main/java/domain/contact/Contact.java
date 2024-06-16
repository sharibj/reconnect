package domain.contact;

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
