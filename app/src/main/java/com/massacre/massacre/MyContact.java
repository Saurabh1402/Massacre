package com.massacre.massacre;

/**
 * Created by saurabh on 25/4/16.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MyContact {
    public static final String NAME_OF_CONTACT="nameContact";
    public static final String ID_OF_CONTACT="idContact";
    public static final String PHONE_NUMBER_OF_CONTACT="phoneContact";
    public static final String COUNTRYCODE_OF_CONTACT="countryCode";
    private Cursor getCursorContacts(Context context) {
        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME);
        return cursor;
    }

    public ArrayList<HashMap<String, String>> getArrayListContact(Context context) {
        Cursor cursor = getCursorContacts(context);
        ArrayList<HashMap<String, String>> arrayListContact = new ArrayList<HashMap<String, String>>();
        if (cursor == null) {
        } else if (!cursor.moveToFirst()) {

        } else {
            int nameCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int idCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            int phoneCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            do {
                HashMap<String, String> hashMapContact = new HashMap<String, String>();
                String name = cursor.getString(nameCol);
                String id = cursor.getString(idCol);
                String phone = cursor.getString(phoneCol);
                String array[] = reduceContact(phone,context);
                String countryCode=array[0];
                phone=array[1];
                SaveFile.saveDataToSharedPreference(context,countryCode+phone,name);
                hashMapContact.put(NAME_OF_CONTACT, name);
                hashMapContact.put(ID_OF_CONTACT, id);
                hashMapContact.put(PHONE_NUMBER_OF_CONTACT, phone);
                hashMapContact.put(COUNTRYCODE_OF_CONTACT, countryCode);
                if (!isRedundantContact(arrayListContact, phone)) {
                    arrayListContact.add(hashMapContact);
                }
            } while (cursor.moveToNext());
        }
        return arrayListContact;
    }



    public boolean isRedundantContact(ArrayList<HashMap<String, String>> arrayListContact, String phone) {
        for (int i = 0; i < arrayListContact.size(); i++) {
            HashMap<String, String> hashMapContact = arrayListContact.get(i);
            String tempphone = hashMapContact.get("phoneContact");
            if (phone.equals(tempphone)) return true;
        }

        return false;
    }

    private String[] reduceContact(String phone,Context context) {
        phone=phone.replace("-","");
        phone=phone.replace(" ","");
        int len = phone.length();
        String countrycode="",phoneNumber="";
        if (len >= 10) {
            len -= 10;
            phoneNumber = "" + phone.subSequence(len, phone.length());
            countrycode=""+phone.subSequence(0,len);
            countrycode=countrycode.replace("+","");
            if((!countrycode.equals("")&&!countrycode.equals("0"))){
                if(countrycode.length()>0 && countrycode.charAt(0)=='0'){
                    countrycode=countrycode.replaceFirst("0","");

                }

            }
            else{
                countrycode=SaveFile.getDataFromSharedPreference(context,MyApplication.COUNTRY_CODE,"");
            }

        }

        String array[]=new String[2];
        array[0]=countrycode;
        array[1]=phoneNumber;
        return array;
    }

}
