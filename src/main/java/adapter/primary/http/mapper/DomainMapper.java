package adapter.primary.http.mapper;

import domain.contact.Contact;
import domain.contact.ContactDetails;
import domain.contact.ContactInfo;
import domain.group.Group;
import domain.interaction.Interaction;
import domain.interaction.InteractionDetails;
import domain.interaction.InteractionType;
import domain.ReconnectModel;
import adapter.primary.http.dto.*;

public class DomainMapper {
    public static Group toDomain(GroupDTO dto) {
        return Group.builder()
                .name(dto.getName())
                .frequencyInDays(dto.getFrequencyInDays())
                .build();
    }

    public static GroupDTO toDTO(Group domain) {
        return new GroupDTO(domain.getName(), domain.getFrequencyInDays());
    }

    public static Contact toDomain(ContactDTO dto) {
        return Contact.builder()
                .nickName(dto.getNickName())
                .group(dto.getGroup())
                .details(toDomain(dto.getDetails()))
                .build();
    }

    public static ContactDTO toDTO(Contact domain) {
        return new ContactDTO(
                domain.getNickName(),
                domain.getGroup(),
                domain.getDetails() != null ? toDTO(domain.getDetails()) : null
        );
    }

    public static ContactDetails toDomain(ContactDetailsDTO dto) {
        ContactDetails details = new ContactDetails();
        details.setFirstName(dto.getFirstName());
        details.setLastName(dto.getLastName());
        details.setNotes(dto.getNotes());
        details.setContactInfo(toDomain(dto.getContactInfo()));
        return details;
    }

    public static ContactDetailsDTO toDTO(ContactDetails domain) {
        return new ContactDetailsDTO(
                domain.getFirstName(),
                domain.getLastName(),
                domain.getNotes(),
                domain.getContactInfo() != null ? toDTO(domain.getContactInfo()) : null
        );
    }

    public static ContactInfo toDomain(ContactInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        ContactInfo info = new ContactInfo();
        info.setPhone(dto.getPhone());
        info.setEmail(dto.getEmail());
        return info;
    }

    public static ContactInfoDTO toDTO(ContactInfo domain) {
        if (domain == null) {
            return new ContactInfoDTO();
        }
        return new ContactInfoDTO(domain.getPhone(), domain.getEmail());
    }

    public static Interaction toDomain(InteractionDTO dto) {
        return Interaction.builder()
                .id(dto.getId())
                .contact(dto.getContact())
                .timeStamp(dto.getTimeStamp() != null ? Long.parseLong(dto.getTimeStamp()) : null)
                .notes(dto.getNotes())
                .interactionDetails(toDomain(dto.getInteractionDetails()))
                .build();
    }

    public static InteractionDTO toDTO(Interaction domain) {
        return new InteractionDTO(
                domain.getId(),
                domain.getContact(),
                String.valueOf(domain.getTimeStamp()),
                domain.getNotes(),
                toDTO(domain.getInteractionDetails()),
                domain.toHumanReadableString()
        );
    }

    public static InteractionDetails toDomain(InteractionDetailsDTO dto) {
        InteractionDetails details = new InteractionDetails();
        details.setType(dto.getType() != null ? InteractionType.valueOf(dto.getType()) : null);
        details.setSelfInitiated(dto.getSelfInitiated());
        return details;
    }

    public static InteractionDetailsDTO toDTO(InteractionDetails domain) {
        return new InteractionDetailsDTO(
                domain.getSelfInitiated(),
                domain.getType() != null ? domain.getType().name() : null
        );
    }

    public static ReconnectModelDTO toDTO(ReconnectModel domain) {
        return new ReconnectModelDTO(
                domain.getContact(),
                domain.getGroup(),
                domain.getFrequencyInDays(),
                domain.getLastContactedTimestamp()
        );
    }
} 