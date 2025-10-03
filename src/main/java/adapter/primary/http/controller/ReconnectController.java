package adapter.primary.http.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import adapter.primary.http.dto.ContactDTO;
import adapter.primary.http.dto.ContactDetailsDTO;
import adapter.primary.http.dto.GroupDTO;
import adapter.primary.http.dto.InteractionDTO;
import adapter.primary.http.dto.ReconnectModelDTO;
import adapter.primary.http.dto.UpdateContactDTO;
import adapter.primary.http.exception.BusinessException;
import adapter.primary.http.security.TenantContext;
import domain.contact.ContactDomainService;
import domain.group.GroupDomainService;
import domain.interaction.InteractionDomainService;
import domain.ReconnectDomainService;
import adapter.primary.http.mapper.DomainMapper;
import domain.contact.Contact;
import domain.group.Group;
import domain.interaction.Interaction;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/reconnect", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reconnect API", description = "API for managing contacts, interactions, and groups")
public class ReconnectController {

    @Autowired
    private ContactDomainService contactDomainService;

    @Autowired
    private InteractionDomainService interactionDomainService;

    @Autowired
    private GroupDomainService groupDomainService;

    @Autowired
    private ReconnectDomainService reconnectDomainService;

    @Operation(summary = "List all contacts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved contacts")
    })
    @GetMapping("/contacts")
    public ResponseEntity<List<ContactDTO>> listContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ContactDTO> allContacts = contactDomainService.getAll().stream()
                .map(DomainMapper::toDTO)
                .toList();
        int start = page * size;
        int end = Math.min(start + size, allContacts.size());
        return ResponseEntity.ok(allContacts.subList(start, end));
    }

    @Operation(summary = "Add a new contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid contact data")
    })
    @PostMapping("/contacts")
    public ResponseEntity<ContactDTO> addContact(@RequestBody ContactDTO createContactDTO) {
        try {
            Contact contact = Contact.builder()
                .username(TenantContext.getCurrentTenant())
                .nickName(createContactDTO.getNickName())
                .group(createContactDTO.getGroup())
                .details(createContactDTO.getDetails() != null ? DomainMapper.toDomain(createContactDTO.getDetails()) : null)
                .build();
            contactDomainService.add(contact);
            return ResponseEntity.ok(DomainMapper.toDTO(contact));
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Update an existing contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact successfully updated"),
            @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @PutMapping("/contacts/{nickName}")
    public ResponseEntity<ContactDTO> updateContact(
            @Schema(description = "Nickname of the contact to update", required = true)
            @PathVariable(name = "nickName") String nickName,
            @RequestBody UpdateContactDTO updateContactDTO) {
        try {
            // Get existing contact
            Contact existingContact = contactDomainService.get(nickName)
                    .orElseThrow(() -> new BusinessException("Contact not found: " + nickName));

            // Create updated contact only with fields that are provided in the request
            Contact updatedContact = Contact.builder()
                    .username(existingContact.getUsername())
                    .nickName(nickName)
                    .group(updateContactDTO.getGroup() != null ? updateContactDTO.getGroup() : existingContact.getGroup())
                    .details(updateContactDTO.getDetails() != null ? DomainMapper.toDomain(updateContactDTO.getDetails()) : existingContact.getDetails())
                    .build();

            contactDomainService.update(updatedContact);
            return ResponseEntity.ok(DomainMapper.toDTO(updatedContact));
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Delete a contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contact successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @DeleteMapping("/contacts/{nickName}")
    public ResponseEntity<Void> deleteContact(
            @Schema(description = "Nickname of the contact to delete", required = true)
            @PathVariable(name = "nickName") String nickName) {
        try {
            contactDomainService.remove(nickName);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "List interactions for a contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved interactions"),
            @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @GetMapping("/contacts/{nickName}/interactions")
    public ResponseEntity<List<InteractionDTO>> listInteractions(
            @Schema(description = "Nickname of the contact to get interactions for", required = true)
            @PathVariable(name = "nickName") String nickName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<InteractionDTO> allInteractions = interactionDomainService.getAll(nickName).stream()
                    .map(DomainMapper::toDTO)
                    .toList();
            int start = page * size;
            int end = Math.min(start + size, allInteractions.size());
            return ResponseEntity.ok(allInteractions.subList(start, end));
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "List all interactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved interactions")
    })
    @GetMapping("/interactions")
    public ResponseEntity<List<InteractionDTO>> listAllInteractions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<InteractionDTO> allInteractions = interactionDomainService.getAll().stream()
                .map(DomainMapper::toDTO)
                .toList();
        int start = page * size;
        int end = Math.min(start + size, allInteractions.size());
        return ResponseEntity.ok(allInteractions.subList(start, end));
    }

    @Operation(summary = "Add a new interaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interaction successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid interaction data"),
            @ApiResponse(responseCode = "404", description = "Contact not found"),
            @ApiResponse(responseCode = "409", description = "Interaction already exists")
    })
    @PostMapping(value = "/interactions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addInteraction(@RequestBody InteractionDTO createInteractionDTO) {
        try {
            Interaction interaction = Interaction.builder()
                    .username(TenantContext.getCurrentTenant())
                    .contact(createInteractionDTO.getContact())
                    .timeStamp(createInteractionDTO.getTimeStamp() != null ? Long.parseLong(createInteractionDTO.getTimeStamp()) : null)
                    .notes(createInteractionDTO.getNotes())
                    .interactionDetails(createInteractionDTO.getInteractionDetails() != null ?
                        DomainMapper.toDomain(createInteractionDTO.getInteractionDetails()) : null)
                    .build();
            interactionDomainService.add(interaction);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Update an existing interaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interaction successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid interaction data"),
            @ApiResponse(responseCode = "404", description = "Interaction not found")
    })
    @PutMapping(value = "/interactions/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateInteraction(
            @Schema(description = "ID of the interaction to update", required = true)
            @PathVariable(name = "id") String id,
            @RequestBody InteractionDTO updateInteractionDTO) {
        try {
            Interaction interaction = Interaction.builder()
                    .id(id)
                    .username(TenantContext.getCurrentTenant())
                    .contact(updateInteractionDTO.getContact())
                    .timeStamp(updateInteractionDTO.getTimeStamp() != null ? Long.parseLong(updateInteractionDTO.getTimeStamp()) : null)
                    .notes(updateInteractionDTO.getNotes())
                    .interactionDetails(updateInteractionDTO.getInteractionDetails() != null ?
                        DomainMapper.toDomain(updateInteractionDTO.getInteractionDetails()) : null)
                    .build();
            interactionDomainService.update(interaction);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Delete an interaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Interaction successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Interaction not found")
    })
    @DeleteMapping("/interactions/{id}")
    public ResponseEntity<Void> deleteInteraction(
            @Schema(description = "ID of the interaction to delete", required = true)
            @PathVariable(name = "id") String id) {
        try {
            interactionDomainService.remove(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "List all groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved groups")
    })
    @GetMapping("/groups")
    public ResponseEntity<List<GroupDTO>> listGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<GroupDTO> allGroups = groupDomainService.getAll().stream()
                .map(DomainMapper::toDTO)
                .toList();
        int start = page * size;
        int end = Math.min(start + size, allGroups.size());
        return ResponseEntity.ok(allGroups.subList(start, end));
    }

    @Operation(summary = "Add a new group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid group data"),
            @ApiResponse(responseCode = "409", description = "Group already exists")
    })
    @PostMapping("/groups")
    public ResponseEntity<Void> addGroup(@RequestBody GroupDTO groupDTO) {
        try {
            Group group = Group.builder()
                    .username(TenantContext.getCurrentTenant())
                    .name(groupDTO.getName())
                    .frequencyInDays(groupDTO.getFrequencyInDays())
                    .build();
            groupDomainService.add(group);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Update an existing group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid group data"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @PutMapping("/groups/{name}")
    public ResponseEntity<Void> updateGroup(
            @Schema(description = "Name of the group to update", required = true)
            @PathVariable(name = "name") String name,
            @RequestBody GroupDTO updateGroupDTO) {
        try {
            Group existingGroup = groupDomainService.get(name);

            Group updatedGroup = Group.builder()
                    .username(existingGroup.getUsername())
                    .name(name)
                    .frequencyInDays(updateGroupDTO.getFrequencyInDays() != null ?
                        updateGroupDTO.getFrequencyInDays() : existingGroup.getFrequencyInDays())
                    .build();

            groupDomainService.update(updatedGroup);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Delete a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Group successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @DeleteMapping("/groups/{name}")
    public ResponseEntity<Void> deleteGroup(
            @Schema(description = "Name of the group to delete", required = true)
            @PathVariable(name = "name") String name) {
        try {
            groupDomainService.remove(name);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Get out of touch contacts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved out of touch contacts")
    })
    @GetMapping("/out-of-touch")
    public ResponseEntity<List<ReconnectModelDTO>> getOutOfTouchContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ReconnectModelDTO> allContacts = reconnectDomainService.getOutOfTouchContactList().stream()
                .map(DomainMapper::toDTO)
                .toList();
        int start = page * size;
        int end = Math.min(start + size, allContacts.size());
        return ResponseEntity.ok(allContacts.subList(start, end));
    }
}