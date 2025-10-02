package adapter.secondary.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "contacts")
@Data
@Filter(name = "tenantFilter", condition = "username = :username")
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickName;
    
    @Column(name = "`group`")
    private String group;
    
    
    @Embedded
    private ContactDetailsEntity details;
}
