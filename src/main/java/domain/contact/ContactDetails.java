package domain.contact;

import lombok.Data;

@Data
public class ContactDetails {
    String firstName;
    String lastName;
    String notes;
    ContactInfo contactInfo;
}
