package adapter.secondary.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "interactions")
@Data
@Filter(name = "tenantFilter", condition = "username = :username")
public class InteractionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    private String contact;
    
    
    private Long timeStamp;
    private String notes;
    
    @Embedded
    private InteractionDetailsEntity interactionDetails;
}
