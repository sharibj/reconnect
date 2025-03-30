package spring.service;

import spring.dto.InteractionDTO;
import spring.dto.CreateInteractionDTO;
import java.util.List;

public interface InteractionService {
    List<InteractionDTO> getAllInteractions(String nickName);
    void addInteraction(CreateInteractionDTO createInteractionDTO);
    void updateInteraction(String id, InteractionDTO interactionDTO);
    void deleteInteraction(String id);
} 