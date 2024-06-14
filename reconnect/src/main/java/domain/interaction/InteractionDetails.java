package domain.interaction;

import lombok.Data;

@Data
public class InteractionDetails {
    Boolean selfInitiated;
    InteractionType type;
}
