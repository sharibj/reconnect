package spring.service.impl;

import domain.group.Group;
import domain.group.GroupDomainService;
import spring.service.GroupService;
import spring.dto.GroupDTO;
import spring.dto.UpdateGroupDTO;
import spring.mapper.DomainMapper;
import spring.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.IOException;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupDomainService groupDomainService;

    @Autowired
    public GroupServiceImpl(GroupDomainService groupDomainService) {
        this.groupDomainService = groupDomainService;
    }

    @Override
    public Set<GroupDTO> getAllGroups() {
        return groupDomainService.getAll().stream()
                .map(DomainMapper::toDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public void addGroup(GroupDTO groupDTO) {
        try {
            Group group = DomainMapper.toDomain(groupDTO);
            groupDomainService.add(group);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void updateGroup(String name, UpdateGroupDTO updateGroupDTO) {
        try {
            // Get existing group
            Group existingGroup = groupDomainService.get(name);

            // Create updated group only with fields that are provided in the request
            Group.GroupBuilder builder = Group.builder()
                    .name(name); // Always use the name from path variable

            // Only update frequency if provided in request
            if (updateGroupDTO.getFrequencyInDays() != null) {
                builder.frequencyInDays(updateGroupDTO.getFrequencyInDays());
            } else {
                builder.frequencyInDays(existingGroup.getFrequencyInDays());
            }

            groupDomainService.update(builder.build());
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteGroup(String name) {
        try {
            groupDomainService.remove(name);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }
} 