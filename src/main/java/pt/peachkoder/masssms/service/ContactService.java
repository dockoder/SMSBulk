package pt.peachkoder.masssms.service;

import java.util.List;

import pt.peachkoder.masssms.persistence.dto.ContactDTO;

// ContactService interface
public interface ContactService extends Service<ContactDTO> {


    @Override
    List<ContactDTO> getList();

    @Override
    ContactDTO get(int id);

    @Override
    ContactDTO get(String id);

    @Override
    void set(ContactDTO t);
}
