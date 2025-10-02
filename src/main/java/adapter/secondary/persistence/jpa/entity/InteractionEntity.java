package adapter.secondary.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "interactions")
@Data
public class InteractionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String contact;
    
    @Column(nullable = false)
    private String username; // Tenant identifier
    
    private Long timeStamp;
    private String notes;
    
    @Embedded
    private InteractionDetailsEntity interactionDetails;
}
