package edu.vu.augmented.reality.linkedin;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.SearchParameter;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;

public class Interface extends Activity {

	// DO NOT CHANGE
	private final static String APIKEY = "dj4b9ihlwnkv";
	// DO NOT CHANGE
	private final static String APISECRET = "R3aCuzr0NPTmdfkq";

	private String sec, tok, name;
	private static LinkedInApiClient client;
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(APIKEY, APISECRET);

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		Intent intentFromInit = getIntent();

		sec = intentFromInit.getStringExtra("Secret");
		tok = intentFromInit.getStringExtra("Token");
		name = intentFromInit.getStringExtra("Name");
		
		//Map to hold the search parameters
		Map<SearchParameter, String> searchParams = new HashMap<SearchParameter, String>();

		//Define which search parameters we want to use. More can be added.
		searchParams.put(SearchParameter.FIRST_NAME, name);
		//searchParams.put(SearchParameter.COMPANY_NAME, "");
		
		LinkedInAccessToken accessToken = new LinkedInAccessToken(tok, sec);
		client = factory.createLinkedInApiClient(accessToken);

		//test client
		client.postNetworkUpdate("Hello from Augmented Reality");
		
		//search for said user using our name we retrieved from Tesseract
		client.searchPeople(searchParams);
		
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

}
