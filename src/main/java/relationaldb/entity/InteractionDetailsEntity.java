package relationaldb.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class InteractionDetailsEntity {
    private Boolean selfInitiated;
    private String type;  // Will store InteractionType enum as String
}
