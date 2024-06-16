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
    String id = UUID.randomUUID().toString();
    @NonNull
    String contact;
    @Builder.Default
    Long timeStamp = new Date().getTime();
    @Builder.Default
    String notes = "";
    @Builder.Default
    InteractionDetails interactionDetails = new InteractionDetails();

    public String toHumanReadableString() {
        return "ID = " + this.id + "\n" +
                "Contact = " + this.contact+ "\n" +
                "Date = " + new Date(timeStamp)+ "\n" +
                "Notes = " + this.notes;
    }
}
