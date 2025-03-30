package spring.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ContactInfoDTO {
    String email;
    String phone;
    String address;

    @JsonCreator
    public ContactInfoDTO(
            @JsonProperty("email") String email,
            @JsonProperty("phone") String phone,
            @JsonProperty("address") String address) {
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public ContactInfoDTO(String email, String phone) {
        this.email = email;
        this.phone = phone;
        this.address = null;
    }

    public ContactInfoDTO() {
        this.email = null;
        this.phone = null;
        this.address = null;
    }
} 