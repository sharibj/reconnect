package domain.interaction;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
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
}
