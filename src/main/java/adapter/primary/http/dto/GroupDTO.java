package adapter.primary.http.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class GroupDTO {
    String name;
    Integer frequencyInDays;

    @JsonCreator
    public GroupDTO(
            @JsonProperty("name") String name,
            @JsonProperty("frequencyInDays") Integer frequencyInDays) {
        this.name = name;
        this.frequencyInDays = frequencyInDays;
    }
} 