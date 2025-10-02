package domain.contact;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode
public class Contact {
    @NonNull
    private String username;
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
