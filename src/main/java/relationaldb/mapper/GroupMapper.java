package relationaldb.mapper;

import org.springframework.stereotype.Component;
import domain.group.Group;
import relationaldb.entity.GroupEntity;

@Component
public class GroupMapper {
    public Group toModel(GroupEntity entity) {
        if (entity == null) return null;
        return Group.builder()
            .name(entity.getName())
            .frequencyInDays(entity.getFrequencyInDays())
            .build();
    }

    public GroupEntity toEntity(Group model) {
        if (model == null) return null;
        GroupEntity entity = new GroupEntity();
        entity.setName(model.getName());
        entity.setFrequencyInDays(model.getFrequencyInDays());
        return entity;
    }
}
