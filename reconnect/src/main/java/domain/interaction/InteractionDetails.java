package domain.interaction;

import lombok.Data;

@Data
public class InteractionDetails {
    Boolean selfInitiated = true;
    InteractionType type = InteractionType.AUDIO_CALL;
}
