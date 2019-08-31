package pt.peachkoder.masssms.persistence.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import pt.peachkoder.masssms.persistence.dto.ContactDTO;
import pt.peachkoder.masssms.persistence.dto.DataDTO;


// Concrete class used to manipulate directly the database.
public class ContactDao implements Dao<ContactDTO> {

    private List<ContactDTO> contacts = new ArrayList<>();

    private Context ctx;

    public ContactDao(Context ctx) {

        this.ctx = ctx;

        init();
    }

    private void init() {


        ContentResolver cr = ctx.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {

            while (cur != null && cur.moveToNext()) {

                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    ContactDTO ct = new ContactDTO();

                    ct.setContactId(Integer.parseInt(id));

                    ct.setDisplayName(name);

                    while (pCur.moveToNext()) {

                        String phone = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        int typePhone = pCur.getInt(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.TYPE));

                        if(typePhone==ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE ||
                                typePhone==ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE){

                            DataDTO data = new DataDTO();

                            data.setDataType(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

                            data.setDataValue(phone);

                            ct.getPhoneList().add(data);
                        }
                    }

                    contacts.add(ct);

                    pCur.close();
                }
            }
        }

        if(cur!=null){
            cur.close();
        }

    }

    @Override
    public ContactDTO get(int id) {
        return contacts.get(id);
    }

    @Override
    public ContactDTO get(String type) {
        return null;
    }

    @Override
    public List<ContactDTO> getAll() {
        return contacts;
    }


    @Override
    public void save(ContactDTO contactDTO) {
        //TODO
    }

    @Override
    public void update(ContactDTO contactDTO, String[] params) {
        //TODO
    }

    @Override
    public void delete(ContactDTO contactDTO) {
        //TODO
    }
}



/*



    void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(ctx, i, null);
       // startActivity(Intent.createChooser(i, ""));
    }

    public void getWazzupContactList(){

        //This class provides applications access to the content model.
        ContentResolver cr = ctx.getContentResolver();

        //RowContacts for filter Account Types
        Cursor contactCursor = cr.query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID,
                        ContactsContract.RawContacts.CONTACT_ID},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"},
                null);



        if (contactCursor != null) {
            if (contactCursor.getCount() > 0) {
                if (contactCursor.moveToFirst()) {
                    do {
                        //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                        String whatsappContactId = contactCursor.getString(
                                contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID)
                        );

                        if (whatsappContactId != null) {
                            //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                            Cursor whatsAppContactCursor = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{whatsappContactId}, null);

                            if (whatsAppContactCursor != null) {
                                whatsAppContactCursor.moveToFirst();
                                String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                whatsAppContactCursor.close();

                                //Add Number to ArrayList
                                myWhatsappContacts.add(number);
                            }
                        }
                    } while (contactCursor.moveToNext());
                    contactCursor.close();
                }
            }
        }



    }

 */
