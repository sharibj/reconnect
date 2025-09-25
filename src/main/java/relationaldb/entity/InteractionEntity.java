package relationaldb.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "interactions")
@Data
public class InteractionEntity {
    @Id
    private String id;
    private String contact;
    private Long timeStamp;
    private String notes;
    
    @Embedded
    private InteractionDetailsEntity interactionDetails;
}
