package pt.peachkoder.masssms.service;

import java.util.List;

import pt.peachkoder.masssms.persistence.dto.ContactDTO;

// ContactService interface
public interface ContactService {

    List<ContactDTO> getContactList();

    ContactDTO getContact(int id);

    void setContact(ContactDTO contact);


 }
