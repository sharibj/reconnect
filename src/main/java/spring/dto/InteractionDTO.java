package spring.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class InteractionDTO {
    String id;
    String contact;
    String timeStamp;
    String notes;
    InteractionDetailsDTO interactionDetails;

    @JsonCreator
    public InteractionDTO(
            @JsonProperty("id") String id,
            @JsonProperty("contact") String contact,
            @JsonProperty("timeStamp") String timeStamp,
            @JsonProperty("notes") String notes,
            @JsonProperty("interactionDetails") InteractionDetailsDTO interactionDetails) {
        this.id = id;
        this.contact = contact;
        this.timeStamp = timeStamp;
        this.notes = notes;
        this.interactionDetails = interactionDetails;
    }

    public InteractionDTO(String id, String contact, String timeStamp) {
        this.id = id;
        this.contact = contact;
        this.timeStamp = timeStamp;
        this.notes = null;
        this.interactionDetails = null;
    }
} 