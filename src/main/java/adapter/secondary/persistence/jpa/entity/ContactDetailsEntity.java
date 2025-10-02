package adapter.secondary.persistence.jpa.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Embeddable
@Data
public class ContactDetailsEntity {
    private String firstName;
    private String lastName;
    private String notes;
    
    @Embedded
    private ContactInfoEntity contactInfo;
}
