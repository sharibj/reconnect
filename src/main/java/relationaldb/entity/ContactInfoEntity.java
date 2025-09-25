package relationaldb.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ContactInfoEntity {
    private String phone;
    private String email;
}
