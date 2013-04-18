// Modified code from http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/

package edu.vu.augmented.reality;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
     
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_EMAIL = "email_address";
    private static final String KEY_WEB = "web_address";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_WEB + " TEXT"               
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
 
        // Create tables again
        onCreate(db);
    }
    
    // Delete all contacts
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        this.onUpgrade(db, DATABASE_VERSION, DATABASE_VERSION);
    }
    
    // Add a new contact to database
    public void addContact(Contact contact) {
        
        SQLiteDatabase db = this.getWritableDatabase();
         
        ContentValues values = new ContentValues();
        
        // Name
        if (!(contact.getName() == ""))
            values.put(KEY_NAME, contact.getName());
        else
            values.put(KEY_NAME, "-not available-");
        
        // Phone number
        if (!(contact.getPhoneNumber() == ""))
            values.put(KEY_PH_NO, contact.getPhoneNumber());
        else
            values.put(KEY_PH_NO, "-not available-");
        
        // Email
        if (!(contact.getEmailAddress() == ""))
            values.put(KEY_EMAIL, contact.getEmailAddress());
        else
            values.put(KEY_EMAIL, "-not available-");
        
        // Web address
        if (!(contact.getWebAddress() == ""))
            values.put(KEY_WEB, contact.getWebAddress());
        else
            values.put(KEY_WEB, "-not available-");
     
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
     
    // Get a single contact based on ID
    public Contact getContact(int id) {
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_PH_NO, KEY_EMAIL, KEY_WEB }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
     
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

        cursor.close();
        db.close();
        return contact;
    }
     
    // Get all contacts
    public List<Contact> getAllContacts() {
        
        List<Contact> contactList = new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                contact.setEmailAddress(cursor.getString(3));
                contact.setWebAddress(cursor.getString(4));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
     
        // return contact list
        return contactList;
    }
    
    // Get last #n contacts
    public List<Contact> getLastNContacts(int n) {
        
        List<Contact> contactList = new ArrayList<Contact>();
        //String selectQuery = "SELECT TOP " + Integer.toString(n) + " * FROM " + TABLE_CONTACTS;
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS + 
                " ORDER BY " + KEY_ID + " DESC LIMIT 0," + Integer.toString(n);
        
        if (n <= 0)
            return contactList;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                contact.setEmailAddress(cursor.getString(3));
                contact.setWebAddress(cursor.getString(4));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        
        return contactList;
    }
     
    // Get total number of contacts in database
    public int getContactsCount() {
        
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
 
        return count;
    }
    
    // No need to implement
    public int updateContact(Contact contact) {
        
        /*
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());
     
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        */
        return -1;
    }
     
    // Delete a single contact
    public void deleteContact(Contact contact) {
        
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }
}