package domain.interaction;

import java.util.Date;
import java.util.UUID;

import domain.contact.Contact;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class Interaction {

    @Builder.Default
    String id = UUID.randomUUID().toString();
    @NonNull
    Contact contact;
    InteractionDetails interactionDetails;
    @Builder.Default
    Long timeStamp = new Date().getTime();
    String notes;
}
