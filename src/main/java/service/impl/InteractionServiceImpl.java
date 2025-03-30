package spring.service.impl;

import domain.interaction.Interaction;
import domain.interaction.InteractionDomainService;
import spring.service.InteractionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.io.IOException;

@Service
public class InteractionServiceImpl implements InteractionService {
    private final InteractionDomainService interactionDomainService;

    @Autowired
    public InteractionServiceImpl(InteractionDomainService interactionDomainService) {
        this.interactionDomainService = interactionDomainService;
    }

    @Override
    public List<Interaction> getAllInteractions(String nickName) {
        try {
            return interactionDomainService.getAll(nickName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get interactions", e);
        }
    }

    @Override
    public void addInteraction(Interaction interaction) {
        try {
            interactionDomainService.add(interaction);
        } catch (IOException e) {
            throw new RuntimeException("Failed to add interaction", e);
        }
    }
} 