package spring.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class UpdateGroupDTO {
    Integer frequencyInDays;

    @JsonCreator
    public UpdateGroupDTO(
            @JsonProperty("frequencyInDays") Integer frequencyInDays) {
        this.frequencyInDays = frequencyInDays;
    }
} 