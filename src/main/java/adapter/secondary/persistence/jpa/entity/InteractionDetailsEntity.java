package adapter.secondary.persistence.jpa.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class InteractionDetailsEntity {
    private Boolean selfInitiated;
    private String type;  // Will store InteractionType enum as String
}
