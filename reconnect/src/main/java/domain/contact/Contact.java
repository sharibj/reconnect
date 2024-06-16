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
    @NonNull
    String group = "";
    ContactDetails details;


    public String toHumanReadableString() {
        return "Name = " + this.nickName + "\n" +
                "Group = " + this.group;
    }
}
