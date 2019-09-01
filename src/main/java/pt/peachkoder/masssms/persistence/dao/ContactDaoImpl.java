package pt.peachkoder.masssms.persistence.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.peachkoder.masssms.persistence.dto.ContactDTO;


// Concrete class used to manipulate directly the database.
public class ContactDaoImpl extends ContactDao{


    public ContactDaoImpl(Context ctx) {
        super(ctx);
    }

    public Map<String, String> getGroups() {

        String selection = ContactsContract.Groups.DELETED + "=? and " +
                ContactsContract.Groups.GROUP_VISIBLE + "=?";

        String[] selectionArgs = { "0", "1" };

        Cursor cursor = ctx.getContentResolver().query(
                ContactsContract.Groups.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );

        Map<String, String> ret = new HashMap<>();

        try {

            cursor.moveToFirst();
            int len = cursor.getCount();


            for (int i = 0; i < len; i++)
            {
                cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
                ret.put(
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE))
                );

            }

        } catch (Exception ignored) {

        } finally {

            cursor.close();

            return ret;

        }
    }

    @Override
    public List<ContactDTO> getAllNumbersFromGroupTitle(String groupTitle){

        List<ContactDTO> ret = new ArrayList<>();

        for (ContactDTO c: contacts)
            if (c.getGroupTitle().equals(groupTitle))
                ret.add(c);

        return ret;
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


public List<ContactDTO> getAllNumbersFromGroupTitle(String groupTitle) {

        List<ContactDTO> ret = new ArrayList<>();

        String selection = ContactsContract.Groups.DELETED + "=? and " +
                ContactsContract.Groups.GROUP_VISIBLE + "=?";

        String[] selectionArgs = { "0", "1" };

        Cursor cursor = contentResolver.query(
                ContactsContract.Groups.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );

        try {

            cursor.moveToFirst();
            int len = cursor.getCount();

            ArrayList<String> numbers = new ArrayList<String>();

            for (int i = 0; i < len; i++)
            {
                String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
                String groupId = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));

                if (title.equals(groupTitle))
                {

                    Cursor groupCursor = contentResolver.query(
                            ContactsContract.Data.CONTENT_URI,
                            null,
                            CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                                    + CommonDataKinds.GroupMembership.MIMETYPE + "='"
                                    + GroupMembership.CONTENT_ITEM_TYPE + "'",
                            new String[] { String.valueOf(groupId) }, null);

                    if (groupCursor != null && groupCursor.moveToFirst())
                    {
                        do
                        {
                            ContactDTO contact = new ContactDTO();

                            String id = groupCursor.getString(groupCursor.getColumnIndex(GroupMembership.CONTACT_ID));

                            contact.setDisplayName(groupCursor.getString(groupCursor.getColumnIndex(Phone.DISPLAY_NAME)));
                            contact.setPhoneList( getMobilePhonesById(id));
                            contact.setGroupTitle(groupTitle);

                            ret.add(contact);


                        } while (groupCursor.moveToNext());
                        groupCursor.close();
                    }
                    break;
                }

                cursor.moveToNext();
            }

        } catch (Exception e) {

        } finally {
            cursor.close();
        }

        return ret;
    }



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


     public ArrayList<ContactDTO> getAllNumbersFromGroupTitle(String navn) {

        List<ContactDTO> ret = new ArrayList<>();

        String selection = ContactsContract.Groups.DELETED + "=? and " +
                ContactsContract.Groups.GROUP_VISIBLE + "=?";

        String[] selectionArgs = { "0", "1" };

        ContentResolver cr = ctx.getContentResolver();

        Cursor cursor = cr.query(
                ContactsContract.Groups.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );

        try {

            cursor.moveToFirst();
            int len = cursor.getCount();

            ArrayList<String> numbers = new ArrayList<String>();

            for (int i = 0; i < len; i++)
            {
                String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));

                if (title.equals(navn))
                {
                    String[] cProjection = {
                            ContactsContract.Contacts.DISPLAY_NAME,
                            GroupMembership.CONTACT_ID
                    };



                    Cursor groupCursor = cr.query(
                            ContactsContract.Data.CONTENT_URI,
                            cProjection,
                            CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                                    + CommonDataKinds.GroupMembership.MIMETYPE + "='"
                                    + GroupMembership.CONTENT_ITEM_TYPE + "'",
                            new String[] { String.valueOf(id) }, null);

                    if (groupCursor != null && groupCursor.moveToFirst())
                    {
                        do
                        {
                            ContactDTO contact = new ContactDTO();

                            int nameCoumnIndex = groupCursor.getColumnIndex(Phone.DISPLAY_NAME);


                            String name = groupCursor.getString(nameCoumnIndex);

                            long contactId = groupCursor.getLong(groupCursor.getColumnIndex(GroupMembership.CONTACT_ID));
                            String _id = groupCursor.getString(groupCursor.getColumnIndex(GroupMembership.CONTACT_ID));

                            contact.setDisplayName(groupCursor.getString(groupCursor.getColumnIndex(Phone.DISPLAY_NAME)));
                            contact.setPhoneList( getMobilePhonesById(_id, cr));

                            Cursor numberCursor = ctx.getContentResolver().query(Phone.CONTENT_URI,
                                    new String[] { Phone.NUMBER }, Phone.CONTACT_ID + "=" + contactId, null, null);

                            if (numberCursor.moveToFirst())
                            {
                                int numberColumnIndex = numberCursor.getColumnIndex(Phone.NUMBER);
                                do
                                {
                                    String phoneNumber = numberCursor.getString(numberColumnIndex);
                                    numbers.add(phoneNumber);
                                } while (numberCursor.moveToNext());
                                numberCursor.close();
                            }
                        } while (groupCursor.moveToNext());
                        groupCursor.close();
                    }
                    break;
                }

                cursor.moveToNext();
            }

        } catch (NullPointerException e) {

        } finally {

            cursor.close();

        }



        return numbers;
    }

 */
