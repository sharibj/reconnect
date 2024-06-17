package domain.contact;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class Contact {
    @NonNull
    private String nickName;
    @Builder.Default
    @NonNull
    private String group = "";
    private ContactDetails details;


    public String toHumanReadableString() {
        return "Name = " + this.nickName + "\n" +
                "Group = " + this.group;
    }
}
