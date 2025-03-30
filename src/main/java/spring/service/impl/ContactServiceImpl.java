package spring.service.impl;

import domain.contact.Contact;
import domain.contact.ContactDomainService;
import spring.service.ContactService;
import spring.dto.ContactDTO;
import spring.dto.CreateContactDTO;
import spring.mapper.DomainMapper;
import spring.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactDomainService contactDomainService;

    @Autowired
    public ContactServiceImpl(ContactDomainService contactDomainService) {
        this.contactDomainService = contactDomainService;
    }

    @Override
    public List<ContactDTO> getAllContacts() {
        return contactDomainService.getAll().stream()
                .map(DomainMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContactDTO getContact(String nickName) {
        return contactDomainService.get(nickName)
                .map(DomainMapper::toDTO)
                .orElse(null);
    }

    @Override
    public ContactDTO addContact(CreateContactDTO createContactDTO) {
        try {
            Contact contact = Contact.builder()
                .nickName(createContactDTO.getNickName())
                .group(createContactDTO.getGroup())
                .details(createContactDTO.getDetails() != null ? DomainMapper.toDomain(createContactDTO.getDetails()) : null)
                .build();
            contactDomainService.add(contact);
            return DomainMapper.toDTO(contact);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ContactDTO updateContact(String nickName, ContactDTO contactDTO) {
        try {
            // Get existing contact
            Contact existingContact = contactDomainService.get(nickName)
                    .orElseThrow(() -> new IOException("Contact not found: " + nickName));

            // Create updated contact only with fields that are provided in the request
            Contact.ContactBuilder builder = Contact.builder()
                    .nickName(nickName); // Always use the nickname from path variable

            // Use provided values or existing values
            builder.group(contactDTO.getGroup() != null ? contactDTO.getGroup() : existingContact.getGroup())
                   .details(contactDTO.getDetails() != null ? DomainMapper.toDomain(contactDTO.getDetails()) : existingContact.getDetails());

            contactDomainService.update(builder.build());
            return DomainMapper.toDTO(builder.build());
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteContact(String nickName) {
        try {
            contactDomainService.remove(nickName);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }
} 