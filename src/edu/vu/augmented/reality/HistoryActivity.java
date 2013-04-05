package edu.vu.augmented.reality;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends Activity {
	
	private int numCardsToDisplay = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		LinearLayout baseLayout = (LinearLayout)findViewById(R.id.activity_history_layout);
		
		DatabaseHandler databaseHandler = new DatabaseHandler(this);
		List<Contact> cl = databaseHandler.getNContacts(numCardsToDisplay);
		//List<Contact> cl = databaseHandler.getAllContacts();
		
		Toast.makeText(this, "Entries: " + Integer.toString(databaseHandler.getContactsCount()), Toast.LENGTH_SHORT).show();
		
		// Add text views that show the info from past cards
		for (int i = 0; i < numCardsToDisplay; ++i) {
			
			if (cl.size() <= i)
				break;
			
			TextView tv = new TextView(this);
			if (i % 2 == 0)
				tv.setBackgroundColor(Color.parseColor("#8BD1FF"));
			else
				tv.setBackgroundColor(Color.parseColor("#B9F9CE"));
			tv.setTextColor(Color.BLACK);
			tv.setPadding(0, 10, 0, 10);
			tv.setText("Name: " + cl.get(i).getName() +
					"\nPhone: " + cl.get(i).getPhoneNumber() +
					"\nEmail: " + cl.get(i).getEmailAddress() +
					"\nWeb: " + cl.get(i).getWebAddress() +
					"\nID(testing): " + cl.get(i).getID());
			baseLayout.addView(tv);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

}
