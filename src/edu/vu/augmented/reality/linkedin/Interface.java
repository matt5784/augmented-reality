package edu.vu.augmented.reality.linkedin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.code.linkedinapi.client.AsyncLinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.enumeration.SearchParameter;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.schema.People;
import com.google.code.linkedinapi.schema.Person;

import edu.vu.augmented.reality.R;

public class Interface extends Activity {

	// DO NOT CHANGE
	private final static String APIKEY = "dj4b9ihlwnkv";
	// DO NOT CHANGE
	private final static String APISECRET = "R3aCuzr0NPTmdfkq";

	private String sec, tok, name;
	AsyncLinkedInApiClient client;
	LinkedInApiClientFactory factory = LinkedInApiClientFactory.newInstance(
			APIKEY, APISECRET);

	private LinearLayout baseLayout;
	private TextView tv_user;

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.linkedin_interface);
		baseLayout = (LinearLayout) findViewById(R.id.linkedin_interface_layout);

		Intent intentFromInit = getIntent();

		// All of these extras come from Init
		sec = intentFromInit.getStringExtra("Secret");
		tok = intentFromInit.getStringExtra("Token");

		String fName, lName = null;

		if (checkDemoPrefs()) {
			name = intentFromInit.getStringExtra("Name");
			String[] lineArray = name.split("\\s");
			fName = lineArray[0];
			lName = lineArray[1];
		} else {
			fName = "Matthew";
			lName = "Lavin";
		}

		LinkedInAccessToken accessToken = new LinkedInAccessToken(tok, sec);

		client = factory.createAsyncLinkedInApiClient(accessToken);

		//allows the API to retrieve all parts of the ProfileField
		Future<Person> me_future = client.getProfileForCurrentUser(EnumSet
				.allOf(ProfileField.class));
		try {
			Person me = me_future.get();
			tv_user = new TextView(this);

			String picUrl = me.getPictureUrl();

			tv_user.setTextColor(Color.BLACK);
			tv_user.setTextSize(20);
			tv_user.setPadding(0, 10, 0, 10);
			tv_user.setGravity(Gravity.CENTER);
			tv_user.setText("Name: " + me.getFirstName() + " "
					+ me.getLastName() + "\nHeadline: " + me.getHeadline());

			tv_user.setPadding(0, 0, 0, 15);
			new setPic().execute(picUrl);

			baseLayout.addView(tv_user);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		} catch (ExecutionException e1) {

			e1.printStackTrace();
		}

		// Map to hold the search parameters
		Map<SearchParameter, String> searchParams = new HashMap<SearchParameter, String>();

		// Define which search parameters we want to use. More can be added.
		searchParams.put(SearchParameter.FIRST_NAME, fName);
		searchParams.put(SearchParameter.LAST_NAME, lName);

		// search for said user using our name we retrieved from Tesseract
		// we will use Matt for the time being
		Future<People> bus_card_user = client.searchPeople(searchParams);
		People persons = null;
		try {

			persons = bus_card_user.get(); // this is a blocking call
			List<Person> pep_list = persons.getPersonList();
			for (Person p : pep_list) {
				TextView tv = new TextView(this);
				// ImageSpan imgV = null;
				// String picUrl = p.getPictureUrl();

				// setPic(picUrl, imgV, tv);
				tv.setTextColor(Color.BLACK);
				tv.setPadding(0, 15, 0, 15);
				tv.setText("Name: " + p.getFirstName() + " " + p.getLastName());

				baseLayout.addView(tv);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private class setPic extends AsyncTask<String, ImageSpan, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... urls) {

			URL picURL;
			Bitmap pic = null;
			try {
				picURL = new URL(urls[0]);
				pic = BitmapFactory.decodeStream(picURL.openConnection()
						.getInputStream());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pic;

		}

		@Override
		protected void onPostExecute(Bitmap img) {
			BitmapDrawable pic_draw = new BitmapDrawable(
					getApplicationContext().getResources(), img);

			tv_user.setCompoundDrawablesWithIntrinsicBounds(pic_draw, null,
					null, null);
		}

	}

	private boolean checkDemoPrefs() {
		SharedPreferences mPrefs = getApplicationContext()
				.getSharedPreferences("Options", MODE_PRIVATE);
		return mPrefs.getBoolean("Demo", false);

	}

}
