package adapter.primary.http.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ContactDetailsDTO {
    String firstName;
    String lastName;
    String notes;
    ContactInfoDTO contactInfo;

    @JsonCreator
    public ContactDetailsDTO(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("notes") String notes,
            @JsonProperty("contactInfo") ContactInfoDTO contactInfo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.notes = notes;
        this.contactInfo = contactInfo;
    }

    public ContactDetailsDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.notes = null;
        this.contactInfo = null;
    }
} 