// Modified code from http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/

package edu.vu.augmented.reality;

public class Contact {
	 
    // Private variables
    private int _id;
    private String _name;
    private String _phone_number;
    private String _email_address;
    private String _web_address;
 
    // Empty constructor
    public Contact(){
 
    }
    
    // Constructor with id
    public Contact(int id, String name, String _phone_number, String _email_address, String _web_address){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this._email_address = _email_address;
        this._web_address = _web_address;
    }
 
    // Constructor without id
    public Contact(String _name, String _phone_number, String _email_address, String _web_address){
    	//this._id = -1;
        this._name = _name;
        this._phone_number = _phone_number;
        this._email_address = _email_address;
        this._web_address = _web_address;
    }
    
    // Get ID
    public int getID(){
        return this._id;
    }
 
    // Set ID
    public void setID(int id){
        this._id = id;
    }
 
    // Get name
    public String getName(){
        return this._name;
    }
 
    // Set name
    public void setName(String name){
        this._name = name;
    }
 
    // Get phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }
 
    // Set phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
    
    // Get email address
    public String getEmailAddress(){
        return this._email_address;
    }
 
    // Set email address
    public void setEmailAddress(String email_address){
        this._phone_number = email_address;
    }
    
    // Get web address
    public String getWebAddress(){
        return this._web_address;
    }
 
    // Set web address
    public void setWebAddress(String web_address){
        this._web_address = web_address;
    }
}