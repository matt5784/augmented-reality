package edu.vu.augmented.reality;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		LinearLayout baseLayout = (LinearLayout)findViewById(R.id.activity_history_layout);
		
		// Add text views that show the info from past cards
		TextView tv = new TextView(getApplicationContext());
		tv.setTextColor(Color.BLACK);
		tv.setText("Name: Ryan Testing\nEmail: this.is.test@vanderbilt.edu\nPhone: 615-555-4432");
		baseLayout.addView(tv);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

}
