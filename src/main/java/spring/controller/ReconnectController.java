package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import spring.service.ContactService;
import spring.service.GroupService;
import spring.service.InteractionService;
import spring.service.ReconnectService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import spring.dto.*;
import io.swagger.v3.oas.annotations.media.Schema;
import spring.exception.BusinessException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/reconnect", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reconnect API", description = "API for managing contacts, interactions, and groups")
public class ReconnectController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ReconnectService reconnectService;

    @Operation(summary = "List all contacts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved contacts")
    })
    @GetMapping("/contacts")
    public ResponseEntity<List<ContactDTO>> listContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ContactDTO> allContacts = contactService.getAllContacts();
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
    public ResponseEntity<ContactDTO> addContact(@RequestBody CreateContactDTO createContactDTO) {
        return ResponseEntity.ok(contactService.addContact(createContactDTO));
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
        // Get existing contact
        ContactDTO existingContact = contactService.getContact(nickName);
        if (existingContact == null) {
            throw new BusinessException("Contact not found: " + nickName);
        }

        // Create updated DTO only with fields that are provided in the request
        String updatedGroup = updateContactDTO.getGroup() != null ? updateContactDTO.getGroup() : existingContact.getGroup();
        ContactDetailsDTO updatedDetails = updateContactDTO.getDetails() != null ? updateContactDTO.getDetails() : existingContact.getDetails();
        
        ContactDTO updatedDTO = new ContactDTO(nickName, updatedGroup, updatedDetails);
        return ResponseEntity.ok(contactService.updateContact(nickName, updatedDTO));
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
        contactService.deleteContact(nickName);
        return ResponseEntity.noContent().build();
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
        List<InteractionDTO> allInteractions = interactionService.getAllInteractions(nickName);
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
    public ResponseEntity<Void> addInteraction(@RequestBody CreateInteractionDTO createInteractionDTO) {
        interactionService.addInteraction(createInteractionDTO);
        return ResponseEntity.ok().build();
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
            @RequestBody UpdateInteractionDTO updateInteractionDTO) {
        // Create a new DTO with the ID from path variable
        InteractionDTO interactionDTO = new InteractionDTO(
            id,
            updateInteractionDTO.getContact(),
            updateInteractionDTO.getTimeStamp(),
            updateInteractionDTO.getNotes(),
            updateInteractionDTO.getInteractionDetails()
        );
        interactionService.updateInteraction(id, interactionDTO);
        return ResponseEntity.ok().build();
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
        interactionService.deleteInteraction(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all groups")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved groups")
    })
    @GetMapping("/groups")
    public ResponseEntity<List<GroupDTO>> listGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Set<GroupDTO> allGroups = groupService.getAllGroups();
        List<GroupDTO> groupsList = allGroups.stream().toList();
        int start = page * size;
        int end = Math.min(start + size, groupsList.size());
        return ResponseEntity.ok(groupsList.subList(start, end));
    }

    @Operation(summary = "Add a new group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Group successfully added"),
        @ApiResponse(responseCode = "400", description = "Invalid group data"),
        @ApiResponse(responseCode = "409", description = "Group already exists")
    })
    @PostMapping("/groups")
    public ResponseEntity<Void> addGroup(@RequestBody GroupDTO groupDTO) {
        groupService.addGroup(groupDTO);
        return ResponseEntity.ok().build();
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
            @RequestBody UpdateGroupDTO updateGroupDTO) {
        groupService.updateGroup(name, updateGroupDTO);
        return ResponseEntity.ok().build();
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
        groupService.deleteGroup(name);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get out of touch contacts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved out of touch contacts")
    })
    @GetMapping("/out-of-touch")
    public ResponseEntity<List<ReconnectModelDTO>> getOutOfTouchContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ReconnectModelDTO> allContacts = reconnectService.getOutOfTouchContacts();
        int start = page * size;
        int end = Math.min(start + size, allContacts.size());
        return ResponseEntity.ok(allContacts.subList(start, end));
    }
}