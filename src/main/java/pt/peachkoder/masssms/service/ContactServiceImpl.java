package pt.peachkoder.masssms.service;

import android.content.Context;
import java.util.List;
import pt.peachkoder.masssms.persistence.dto.ContactDTO;
import pt.peachkoder.masssms.persistence.dao.ContactDao;

// ContactService implementation.
// Used to handle database access through a level of abstraction
public class ContactServiceImpl implements ContactService {

    private Context ctx;

    private ContactDao contactDao;

    public ContactServiceImpl(Context ctx) {

        this.ctx = ctx;

        this.contactDao = new ContactDao(ctx);
    }

    @Override
    public List<ContactDTO> getContactList() {
        return contactDao.getAll();

    }

    @Override
    public ContactDTO getContact(int id) {
        return contactDao.get(id);
    }

    @Override
    public void setContact(ContactDTO contact) {
        contactDao.save(contact);

    }
}