package spring.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ReconnectModelDTO {
    String nickName;
    String group;
    Integer frequencyInDays;
    Long lastInteractionTimeStamp;

    @JsonCreator
    public ReconnectModelDTO(
            @JsonProperty("nickName") String nickName,
            @JsonProperty("group") String group,
            @JsonProperty("frequencyInDays") Integer frequencyInDays,
            @JsonProperty("lastInteractionTimeStamp") Long lastInteractionTimeStamp) {
        this.nickName = nickName;
        this.group = group;
        this.frequencyInDays = frequencyInDays;
        this.lastInteractionTimeStamp = lastInteractionTimeStamp;
    }
} 