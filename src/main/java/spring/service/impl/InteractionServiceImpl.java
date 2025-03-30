package spring.service.impl;

import domain.interaction.Interaction;
import domain.interaction.InteractionDomainService;
import spring.service.InteractionService;
import spring.dto.InteractionDTO;
import spring.dto.CreateInteractionDTO;
import spring.mapper.DomainMapper;
import spring.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

@Service
public class InteractionServiceImpl implements InteractionService {
    private final InteractionDomainService interactionDomainService;

    @Autowired
    public InteractionServiceImpl(InteractionDomainService interactionDomainService) {
        this.interactionDomainService = interactionDomainService;
    }

    @Override
    public List<InteractionDTO> getAllInteractions(String nickName) {
        try {
            return interactionDomainService.getAll(nickName).stream()
                    .map(DomainMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void addInteraction(CreateInteractionDTO createInteractionDTO) {
        try {
            if (createInteractionDTO.getContact() == null) {
                throw new BusinessException("Contact is required");
            }

            Interaction interaction = Interaction.builder()
                    .contact(createInteractionDTO.getContact())
                    .notes(createInteractionDTO.getNotes())
                    .timeStamp(createInteractionDTO.getTimeStamp() != null ? Long.parseLong(createInteractionDTO.getTimeStamp()) : new java.util.Date().getTime())
                    .interactionDetails(createInteractionDTO.getInteractionDetails() != null ? DomainMapper.toDomain(createInteractionDTO.getInteractionDetails()) : null)
                    .build();

            interactionDomainService.add(interaction);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void updateInteraction(String id, InteractionDTO interactionDTO) {
        try {
            // Get existing interaction
            Interaction existingInteraction = interactionDomainService.get(id);

            // Create updated interaction only with fields that are provided in the request
            Interaction.InteractionBuilder builder = Interaction.builder()
                    .id(id); // Always use the id from path variable

            // Use provided values or existing values
            builder.contact(interactionDTO.getContact() != null ? interactionDTO.getContact() : existingInteraction.getContact())
                   .timeStamp(interactionDTO.getTimeStamp() != null ? Long.parseLong(interactionDTO.getTimeStamp()) : 
                            (existingInteraction.getTimeStamp() != null ? existingInteraction.getTimeStamp() : new java.util.Date().getTime()))
                   .notes(interactionDTO.getNotes() != null ? interactionDTO.getNotes() : existingInteraction.getNotes())
                   .interactionDetails(interactionDTO.getInteractionDetails() != null ? 
                           DomainMapper.toDomain(interactionDTO.getInteractionDetails()) : 
                           existingInteraction.getInteractionDetails());

            interactionDomainService.update(builder.build());
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteInteraction(String id) {
        try {
            interactionDomainService.remove(id);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }
} 