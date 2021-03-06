package edu.vu.augmented.reality;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings({ "deprecation" })
public class DetailActivity extends Activity {

    private String thisName;
    private String thisEmail;
    private String thisWeb;
    private String thisPhone;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        Intent inputData = this.getIntent();
        
        thisName = inputData.getStringExtra("name");
        thisEmail = inputData.getStringExtra("email");
        thisWeb = inputData.getStringExtra("web");
        thisPhone = inputData.getStringExtra("phone");
        
        ((TextView)findViewById(R.id.detailname)).setText(thisName);
        ((TextView)findViewById(R.id.detailemail)).setText(thisEmail);
        ((TextView)findViewById(R.id.detailphone)).setText(thisPhone);
        ((TextView)findViewById(R.id.detailweb)).setText(thisWeb);
        
        Button linkedinButton = (Button) findViewById(R.id.linkedinbutton);
        
        Button webButton = (Button) findViewById(R.id.webbutton);
        
        Button contactButton = (Button) findViewById(R.id.contactbutton);
        
        contactButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent i_contact = new Intent(Contacts.Intents.Insert.ACTION, Contacts.People.CONTENT_URI);
                i_contact.putExtra(Contacts.Intents.Insert.NAME, thisName);
                i_contact.putExtra(Contacts.Intents.Insert.EMAIL, thisEmail);
                i_contact.putExtra(Contacts.Intents.Insert.PHONE, thisPhone);
                
                startActivity(i_contact);
            }
        });
        
        
        webButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (thisWeb.equals("-not available-")){
                    Toast.makeText(getApplicationContext(), "Sorry, the web address is not available", Toast.LENGTH_SHORT).show();
                } else {
                    String currentUrl = thisWeb;
                    if (!currentUrl.startsWith("http://") && !currentUrl.startsWith("https://")) {
                        currentUrl = "http://" + currentUrl;
                    }
                    
                    Intent i_web = new Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl));
                    try {
                        startActivity(i_web);
                    } catch (ActivityNotFoundException e){
                        Toast.makeText(getApplicationContext(), "Sorry, the web address appears to be invalid.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        
        // allow each contact to be clicked to prompt the user if they want
        // to search for that contact on Linkedin
        
        linkedinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new AlertDialog.Builder(DetailActivity.this)
                        .setMessage("Search on Linkedin?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {

                                    // flow into our Linkedin class
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        try {
                                            // we must also send the name of
                                            // the user we are trying to
                                            // search for
                                            Intent myIntent = new Intent(
                                                    DetailActivity.this,
                                                    Class.forName("edu.vu.augmented.reality.linkedin.Init"));

                                            myIntent.putExtra("Name", thisName);

                                            startActivity(myIntent);
                                        } catch (ClassNotFoundException e) {
                                            // TODO Auto-generated catch
                                            // block
                                            e.printStackTrace();
                                        }

                                    }

                                }).setNegativeButton("No", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail, menu);
        return true;
    }
}
