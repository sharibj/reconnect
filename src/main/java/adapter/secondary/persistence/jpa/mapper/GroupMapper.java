package adapter.secondary.persistence.jpa.mapper;

import org.springframework.stereotype.Component;
import domain.group.Group;
import adapter.secondary.persistence.jpa.entity.GroupEntity;
import adapter.primary.http.security.TenantContext;

@Component
public class GroupMapper {
    public Group toModel(GroupEntity entity) {
        if (entity == null) return null;
        return Group.builder()
            .username(entity.getUsername())
            .name(entity.getName())
            .frequencyInDays(entity.getFrequencyInDays())
            .build();
    }

    public GroupEntity toEntity(Group model) {
        if (model == null) return null;
        GroupEntity entity = new GroupEntity();
        entity.setUsername(model.getUsername() != null ? model.getUsername() : TenantContext.getCurrentTenant());
        entity.setName(model.getName());
        entity.setFrequencyInDays(model.getFrequencyInDays());
        return entity;
    }
}
