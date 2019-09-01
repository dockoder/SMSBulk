package pt.peachkoder.masssms.service;

import android.content.Context;
import java.util.List;
import pt.peachkoder.masssms.persistence.dto.ContactDTO;
import pt.peachkoder.masssms.persistence.dao.ContactDaoImpl;

// ContactService implementation.
// Used to handle database access through a level of abstraction
public class ContactServiceImpl implements ContactService {


    private ContactDaoImpl contactDao;

    public ContactServiceImpl(Context ctx) {

        this.contactDao = new ContactDaoImpl(ctx);
    }

    @Override
    public List<ContactDTO> getList() {
        return contactDao.getAll();
    }

    @Override
    public ContactDTO get(int id) {
        return contactDao.get(id);
    }

    @Override
    public ContactDTO get(String id) {
        return null;
    }

    @Override
    public void set(ContactDTO t) {
        contactDao.save(t);
    }
}