package edu.vu.augmented.reality.linkedin;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
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
	private static LinkedInApiClient client;
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(APIKEY, APISECRET);

	private LinearLayout baseLayout;

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		
		setContentView(R.layout.linkedin_interface);
		baseLayout = (LinearLayout) findViewById(R.id.linkedin_interface_layout);
		
		

		Intent intentFromInit = getIntent();

		//All of these extras come from Init
		sec = intentFromInit.getStringExtra("Secret");
		tok = intentFromInit.getStringExtra("Token");
		//name = intentFromInit.getStringExtra("Name");
		
		LinkedInAccessToken accessToken = new LinkedInAccessToken(tok, sec);
		
		client = factory.createLinkedInApiClient(accessToken);
		
		//Map to hold the search parameters
		Map<SearchParameter, String> searchParams = new HashMap<SearchParameter, String>();

		//Define which search parameters we want to use. More can be added.
		searchParams.put(SearchParameter.FIRST_NAME, "Matthew");
		searchParams.put(SearchParameter.LAST_NAME, "Lavin");
		//searchParams.put(SearchParameter.COMPANY_NAME, "");
		
	
		//test client
		//client.postNetworkUpdate("Hello from Augmented Reality");

		
		//search for said user using our name we retrieved from Tesseract
		//we will use Matt for the time being
		People bus_card_user = client.searchPeople(searchParams);
		List<Person> persons = bus_card_user.getPersonList();
		for (Person p: persons) {
			TextView tv = new TextView(this);
			ImageSpan imgV = null;
			//String picUrl = p.getPictureUrl();
			
			//setPic(picUrl, imgV, tv);
			tv.setTextColor(Color.BLACK);
			tv.setPadding(0, 10, 0, 10);
			tv.setText("Name: " + p.getFirstName() + " " + p.getLastName());
					
			
			baseLayout.addView(tv);
		}
		
		//populate the activity with your information maybe?
		//Person me = client.getProfileForCurrentUser();

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

	private void setPic(String picLoc, ImageSpan imgV, TextView tv)
			throws IOException {
		URL picURL = new URL(picLoc);
		Bitmap picture = BitmapFactory.decodeStream(picURL.openConnection()
				.getInputStream());
		BitmapDrawable pic_draw = new BitmapDrawable(getApplicationContext()
				.getResources(), picture);
		tv.setCompoundDrawablesWithIntrinsicBounds(pic_draw, null, null, null);
	}

}
