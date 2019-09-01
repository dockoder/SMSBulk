package pt.peachkoder.masssms.persistence.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.peachkoder.masssms.persistence.dto.ContactDTO;
import pt.peachkoder.masssms.persistence.dto.DataDTO;


// Concrete class used to manipulate directly the database.
public abstract class ContactDao implements Dao<ContactDTO> {

    protected List<ContactDTO> contacts = new ArrayList<>();

    protected Context ctx;

    protected ContentResolver contentResolver;

    public ContactDao(Context ctx) {

        this.ctx = ctx;

        ContentResolver cr = this.ctx.getContentResolver();

        getAllContacts();
    }

    public void reload() { getAllContacts(); }

    public abstract  Map<String, String> getGroups();

    public abstract List<ContactDTO> getAllNumbersFromGroupTitle(String groupTitle);

    protected List<DataDTO> getMobilePhonesById(String id){

        List<DataDTO> list = new ArrayList<>();

        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{id}, null);

        while (cursor.moveToNext()) {

            String phone = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));

            int typePhone = cursor.getInt(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.TYPE));

            if(typePhone== ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE ||
                    typePhone== ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE){

                DataDTO data = new DataDTO();

                data.setDataType(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

                data.setDataValue(phone);

                list.add(data);
            }
        }

        cursor.close();

        return list;
    }

    private void getAllContacts() {

        Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {

            contacts.clear();

            while (cur.moveToNext()) {

                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                String group = cur.getString(cur.getColumnIndex(
                        ContactsContract.Groups.TITLE));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    ContactDTO ct = new ContactDTO();

                    ct.setContactId(Integer.parseInt(id));

                    ct.setGroupTitle(group);

                    ct.setDisplayName(name);

                    ct.setPhoneList( getMobilePhonesById(id) );

                    contacts.add(ct);
                }
            }
        }

        if(cur!=null){
            cur.close();
        }

    }






}

