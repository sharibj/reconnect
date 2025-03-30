package spring.service;

import spring.dto.ContactDTO;
import spring.dto.CreateContactDTO;
import java.util.List;

public interface ContactService {
    List<ContactDTO> getAllContacts();
    ContactDTO getContact(String nickName);
    ContactDTO addContact(CreateContactDTO createContactDTO);
    ContactDTO updateContact(String nickName, ContactDTO contactDTO);
    void deleteContact(String nickName);
} 