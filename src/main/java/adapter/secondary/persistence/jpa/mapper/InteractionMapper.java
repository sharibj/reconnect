package adapter.secondary.persistence.jpa.mapper;

import org.springframework.stereotype.Component;
import domain.interaction.Interaction;
import domain.interaction.InteractionDetails;
import domain.interaction.InteractionType;
import adapter.secondary.persistence.jpa.entity.InteractionEntity;
import adapter.secondary.persistence.jpa.entity.InteractionDetailsEntity;
import adapter.primary.http.security.TenantContext;

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
            .id(entity.getId() != null ? entity.getId().toString() : null)
            .username(entity.getUsername())
            .contact(entity.getContact())
            .timeStamp(entity.getTimeStamp())
            .notes(entity.getNotes())
            .interactionDetails(details)
            .build();
    }

    public InteractionEntity toEntity(Interaction model) {
        if (model == null) return null;
        
        InteractionEntity entity = new InteractionEntity();
        // Only set ID if it's a valid Long, otherwise let it be auto-generated
        if (model.getId() != null) {
            try {
                entity.setId(Long.parseLong(model.getId()));
            } catch (NumberFormatException e) {
                // Let the ID be auto-generated for new entities
            }
        }
        entity.setUsername(model.getUsername() != null ? model.getUsername() : TenantContext.getCurrentTenant());
        entity.setContact(model.getContact());
        entity.setTimeStamp(model.getTimeStamp());
        entity.setNotes(model.getNotes());
        
        if (model.getInteractionDetails() != null) {
            InteractionDetailsEntity detailsEntity = new InteractionDetailsEntity();
            detailsEntity.setSelfInitiated(model.getInteractionDetails().getSelfInitiated());
            if (model.getInteractionDetails().getType() != null) {
                detailsEntity.setType(model.getInteractionDetails().getType().name());
            }
            entity.setInteractionDetails(detailsEntity);
        }
        
        return entity;
    }
}
