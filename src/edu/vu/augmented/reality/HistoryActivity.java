package edu.vu.augmented.reality;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends Activity {

	private int numCardsToDisplay = 5;

	// Get assigned in the onCreate() method
	private DatabaseHandler databaseHandler;
	private LinearLayout baseLayout;
	private List<View> listOfCards = new LinkedList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		baseLayout = (LinearLayout) findViewById(R.id.activity_history_layout);

		databaseHandler = new DatabaseHandler(this);
		final List<Contact> cl = databaseHandler
				.getLastNContacts(numCardsToDisplay);

		Toast.makeText(
				this,
				"Entries: "
						+ Integer.toString(databaseHandler.getContactsCount()),
				Toast.LENGTH_SHORT).show();

		// Add text views that show the info from past cards
		for (int i = 0; i < numCardsToDisplay; ++i) {

			// needed for the inner class on View.OnClickListener()
			final int temp = i;

			if (cl.size() <= i)
				break;

			TextView tv = new TextView(this);
			if (i % 2 == 0)
				tv.setBackgroundColor(Color.parseColor("#8BD1FF"));
			else
				tv.setBackgroundColor(Color.parseColor("#B9F9CE"));
			tv.setTextColor(Color.BLACK);
			tv.setPadding(0, 10, 0, 10);
			tv.setText("Name: " + cl.get(i).getName() + "\nPhone: "
					+ cl.get(i).getPhoneNumber() + "\nEmail: "
					+ cl.get(i).getEmailAddress() + "\nWeb: "
					+ cl.get(i).getWebAddress() + "\nID(testing): "
					+ cl.get(i).getID());

			// allow each contact to be clicked to prompt the user if they want
			// to search for
			// a certain user on Linkedin
			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					new AlertDialog.Builder(HistoryActivity.this)
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
														HistoryActivity.this,
														Class.forName("edu.vu.augmented.reality.linkedin.Init"));

												myIntent.putExtra("Name",
														cl.get(temp).getName());

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

			baseLayout.addView(tv);

			listOfCards.add(tv);
		}

		// Add button that deletes all past entries
		Button b = new Button(this);
		b.setText("Clear all history");
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				databaseHandler.deleteAll();
				for (int i = 0; i < listOfCards.size(); ++i) {
					baseLayout.removeView(listOfCards.get(i));
				}

			}
		});
		baseLayout.addView(b);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

}
