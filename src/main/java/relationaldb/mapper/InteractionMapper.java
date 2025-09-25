package relationaldb.mapper;

import org.springframework.stereotype.Component;
import domain.interaction.Interaction;
import domain.interaction.InteractionDetails;
import domain.interaction.InteractionType;
import relationaldb.entity.InteractionEntity;
import relationaldb.entity.InteractionDetailsEntity;

@Component
public class InteractionMapper {
    public Interaction toModel(InteractionEntity entity) {
        if (entity == null) return null;
        
        InteractionDetails details = new InteractionDetails();
        if (entity.getInteractionDetails() != null) {
            details.setSelfInitiated(entity.getInteractionDetails().getSelfInitiated());
            if (entity.getInteractionDetails().getType() != null) {
                details.setType(InteractionType.valueOf(entity.getInteractionDetails().getType()));
            }
        }

        return Interaction.builder()
            .id(entity.getId())
            .contact(entity.getContact())
            .timeStamp(entity.getTimeStamp())
            .notes(entity.getNotes())
            .interactionDetails(details)
            .build();
    }

    public InteractionEntity toEntity(Interaction model) {
        if (model == null) return null;
        
        InteractionEntity entity = new InteractionEntity();
        entity.setId(model.getId());
        entity.setContact(model.getContact());
        entity.setTimeStamp(model.getTimeStamp());
        entity.setNotes(model.getNotes());
        
        if (model.getInteractionDetails() != null) {
            InteractionDetailsEntity details = new InteractionDetailsEntity();
            details.setSelfInitiated(model.getInteractionDetails().getSelfInitiated());
            if (model.getInteractionDetails().getType() != null) {
                details.setType(model.getInteractionDetails().getType().name());
            }
            entity.setInteractionDetails(details);
        }
        
        return entity;
    }
}
