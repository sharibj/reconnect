package spring.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CreateInteractionDTO {
    String contact;
    String timeStamp;
    String notes;
    InteractionDetailsDTO interactionDetails;

    @JsonCreator
    public CreateInteractionDTO(
            @JsonProperty("contact") String contact,
            @JsonProperty("timeStamp") String timeStamp,
            @JsonProperty("notes") String notes,
            @JsonProperty("interactionDetails") InteractionDetailsDTO interactionDetails) {
        this.contact = contact;
        this.timeStamp = timeStamp;
        this.notes = notes;
        this.interactionDetails = interactionDetails;
    }

    public CreateInteractionDTO(String contact) {
        this.contact = contact;
        this.timeStamp = null;
        this.notes = null;
        this.interactionDetails = null;
    }
} 