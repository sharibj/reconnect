package adapter.primary.http.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UpdateContactDTO {
    String group;
    ContactDetailsDTO details;

    @JsonCreator
    public UpdateContactDTO(
            @JsonProperty("group") String group,
            @JsonProperty("details") ContactDetailsDTO details) {
        this.group = group;
        this.details = details;
    }
} 