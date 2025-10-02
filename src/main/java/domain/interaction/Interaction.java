package domain.interaction;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class Interaction {
    @NonNull
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @NonNull
    private String contact;
    @Builder.Default
    private Long timeStamp = new Date().getTime();
    @Builder.Default
    private String notes = "";
    @Builder.Default
    private InteractionDetails interactionDetails = new InteractionDetails();
    @NonNull
    @Builder.Default
    private String username = "default_user";

    public String toHumanReadableString() {
        return "ID = " + this.id + "\n" +
                "Contact = " + this.contact+ "\n" +
                "Date = " + new Date(timeStamp)+ "\n" +
                "Notes = " + this.notes;
    }
}
