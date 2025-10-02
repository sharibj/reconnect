package adapter.primary.http.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class InteractionDetailsDTO {
    Boolean selfInitiated;
    String type;

    @JsonCreator
    public InteractionDetailsDTO(
            @JsonProperty("selfInitiated") Boolean selfInitiated,
            @JsonProperty("type") String type) {
        this.selfInitiated = selfInitiated;
        this.type = type;
    }

    public InteractionDetailsDTO(String type) {
        this.selfInitiated = null;
        this.type = type;
    }
} 