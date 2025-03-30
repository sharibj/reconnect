package spring.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CreateContactDTO {
    String nickName;
    String group;
    ContactDetailsDTO details;

    @JsonCreator
    public CreateContactDTO(
            @JsonProperty("nickName") String nickName,
            @JsonProperty("group") String group,
            @JsonProperty("details") ContactDetailsDTO details) {
        this.nickName = nickName;
        this.group = group;
        this.details = details;
    }
} 