package relationaldb.mapper;

import org.springframework.stereotype.Component;
import domain.contact.Contact;
import domain.contact.ContactDetails;
import domain.contact.ContactInfo;
import relationaldb.entity.ContactEntity;
import relationaldb.entity.ContactDetailsEntity;
import relationaldb.entity.ContactInfoEntity;

@Component
public class ContactMapper {
    public Contact toModel(ContactEntity entity) {
        if (entity == null) return null;
        
        ContactDetails details = null;
        if (entity.getDetails() != null) {
            details = new ContactDetails();
            details.setFirstName(entity.getDetails().getFirstName());
            details.setLastName(entity.getDetails().getLastName());
            details.setNotes(entity.getDetails().getNotes());
            
            if (entity.getDetails().getContactInfo() != null) {
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setPhone(entity.getDetails().getContactInfo().getPhone());
                contactInfo.setEmail(entity.getDetails().getContactInfo().getEmail());
                details.setContactInfo(contactInfo);
            }
        }

        return Contact.builder()
            .nickName(entity.getNickName())
            .group(entity.getGroup())
            .details(details)
            .build();
    }

    public ContactEntity toEntity(Contact model) {
        if (model == null) return null;
        
        ContactEntity entity = new ContactEntity();
        entity.setNickName(model.getNickName());
        entity.setGroup(model.getGroup());
        
        if (model.getDetails() != null) {
            ContactDetailsEntity details = new ContactDetailsEntity();
            details.setFirstName(model.getDetails().getFirstName());
            details.setLastName(model.getDetails().getLastName());
            details.setNotes(model.getDetails().getNotes());
            
            if (model.getDetails().getContactInfo() != null) {
                ContactInfoEntity contactInfo = new ContactInfoEntity();
                contactInfo.setPhone(model.getDetails().getContactInfo().getPhone());
                contactInfo.setEmail(model.getDetails().getContactInfo().getEmail());
                details.setContactInfo(contactInfo);
            }
            
            entity.setDetails(details);
        }
        
        return entity;
    }
}
