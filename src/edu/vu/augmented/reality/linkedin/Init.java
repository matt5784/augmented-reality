package edu.vu.augmented.reality.linkedin;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;

import edu.vu.augmented.reality.R;

/**
 * Class that initializes the API for LinkedIn How this class works is by
 * leveraging two libraries, scribe and linkedin-j Scribe is used to perform the
 * authentication and retrieval of the access token LinkedIn-j is used to make
 * the calls: update status, pull profile information, etc.
 * 
 * A note on this class, there was a change made to the scribe class
 * LinkedInApi.java. We have to grant this activity certain permissions that are
 * needed to make client calls. If something changes and more/less permissions
 * are needed, edit LinkedInApi.java found in org.scribe.builder.api.
 * 
 * @author Robert Newton
 * 
 */

public class Init extends Activity {

	// DO NOT CHANGE
	private final static String APIKEY = "dj4b9ihlwnkv";
	// DO NOT CHANGE
	private final static String APISECRET = "R3aCuzr0NPTmdfkq";

	final static String CALLBACK = "oauth://linkedin";
	static Token accessToken = null;
	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
			.getInstance().createLinkedInOAuthService(APIKEY, APISECRET);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(APIKEY, APISECRET);
	LinkedInRequestToken liToken;

	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linkedin);

		// This is a fix to allow network code to run on the main thread
		// The code to authenticate should be placed inside an AsyncTask
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		final WebView webView = (WebView) findViewById(R.id.wv1);

		// We should check to see if the user is already logged in, so
		// that we don't always have to load the webview
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean prefToken = mPrefs.getBoolean("AccessToken", false);
		
		//Grab the name from the intent passed in by HistoryActivity
		Intent fromHistoryActivity = getIntent();
		final String name = fromHistoryActivity.getStringExtra("Name");

		if (!prefToken) {

			// used to build our OAuthService
			final OAuthService s = new ServiceBuilder()
					.provider(LinkedInApi.class).apiKey(APIKEY)
					.apiSecret(APISECRET).callback(CALLBACK).build();

			// create the scribe and linkedin-j request token
			final Token requestToken = s.getRequestToken();
			liToken = new LinkedInRequestToken(requestToken.getToken(),
					requestToken.getSecret());
			final String authURL = s.getAuthorizationUrl(requestToken);

			// Since call backs don't seem to work, we can create a webview
			// where the
			// user can authenticate. After they have done so the webview will
			// close itself.
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if (url.startsWith("oauth")) {

						webView.setVisibility(View.GONE);

						// verifier is retrieved from the Url after the user has
						// authenticated
						Uri uri = Uri.parse(url);
						String verifier = uri
								.getQueryParameter("oauth_verifier");
						Verifier v = new Verifier(verifier);

						// use scribe to create our accessToken
						accessToken = s.getAccessToken(requestToken, v);

						// call the sharedpreferences editor to update the
						// access
						// token
						SharedPreferences.Editor editor = mPrefs.edit();
						editor.putBoolean("AccessToken", true);
						editor.putString("Token", accessToken.getToken());
						editor.putString("Secret", accessToken.getSecret());
						editor.commit();

						Intent i;

						try {
							i = new Intent(
									Init.this,
									Class.forName("edu.vu.augmented.reality.linkedin.Interface"));
							String tok = mPrefs.getString("Token", "null");
							String sec = mPrefs.getString("Secret", "null");
							if (tok != null && sec != null) {
								i.putExtra("Token", tok);
								i.putExtra("Secret", sec);
								i.putExtra("Name", name);
								startActivity(i);
							}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						return true;
					}
					return super.shouldOverrideUrlLoading(view, url);
				}
			});
			webView.loadUrl(authURL);

		} else {
			Intent i;

			try {
				i = new Intent(
						Init.this,
						Class.forName("edu.vu.augmented.reality.linkedin.Interface"));
				String tok = mPrefs.getString("Token", "null");
				String sec = mPrefs.getString("Secret", "null");
				if (tok != null && sec != null) {
					i.putExtra("Token", tok);
					i.putExtra("Secret", sec);
					i.putExtra("Name", name);
					startActivity(i);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
