package edu.vu.augmented.reality;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryActivity extends Activity {
	
	private int numCardsToDisplay = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		LinearLayout baseLayout = (LinearLayout)findViewById(R.id.activity_history_layout);
		
		DatabaseHandler databaseHandler = new DatabaseHandler(this);
		List<Contact> cl = databaseHandler.getNContacts(numCardsToDisplay);
		
		// Add text views that show the info from past cards
		for (int i = 0; i < numCardsToDisplay; ++i) {
			
			if (cl.size() >= i)
				break;
			
			TextView tv = new TextView(this);
			tv.setTextColor(Color.BLUE);
			tv.setText("Name: " + cl.get(i).getName() +
					"\nPhone: " + cl.get(i).getPhoneNumber() +
					"\nEmail: " + cl.get(i).getEmailAddress() +
					"\nWeb: " + cl.get(i).getWebAddress());
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
