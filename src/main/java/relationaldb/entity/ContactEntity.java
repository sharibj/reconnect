package relationaldb.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contacts")
@Data
public class ContactEntity {
    @Id
    private String nickName;
    
    @Column(name = "`group`")
    private String group;
    
    @Embedded
    private ContactDetailsEntity details;
}
