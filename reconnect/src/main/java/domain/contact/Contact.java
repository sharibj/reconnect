package domain.contact;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class Contact {
    @NonNull
    String nickName;
    @Builder.Default
    String group = "";
    ContactDetails details;
}
