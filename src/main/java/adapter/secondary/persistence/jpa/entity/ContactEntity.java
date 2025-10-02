package adapter.secondary.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contacts")
@Data
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nickName;
    
    @Column(name = "`group`")
    private String group;
    
    @Column(nullable = false)
    private String username; // Tenant identifier
    
    @Embedded
    private ContactDetailsEntity details;
}
