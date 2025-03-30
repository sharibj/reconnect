package spring.service;

import spring.dto.GroupDTO;
import spring.dto.UpdateGroupDTO;
import java.util.Set;

public interface GroupService {
    Set<GroupDTO> getAllGroups();
    void addGroup(GroupDTO groupDTO);
    void updateGroup(String name, UpdateGroupDTO updateGroupDTO);
    void deleteGroup(String name);
} 