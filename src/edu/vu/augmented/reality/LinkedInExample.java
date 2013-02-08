package edu.vu.augmented.reality;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Class that initializes the API for LinkedIn
 * @author Robert Newton
 *
 */

public class LinkedinExample extends Activity {
	
	//DO NOT CHANGE
	final static String APIKEY = "dj4b9ihlwnkv";
	//DO NOT CHANGE
	final static String APISECRET = "R3aCuzr0NPTmdfkq";
	final static String CALLBACK = "oauth://linkedin";
	static Token accessToken = null;
	final LinkedInOAuthService oAuthService             = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(APIKEY, APISECRET);
	final LinkedInApiClientFactory factory              = LinkedInApiClientFactory.newInstance(APIKEY, APISECRET);
	LinkedInApiClient client;
	LinkedInRequestToken liToken;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linkedin);
		
		final TextView textView = (TextView)findViewById(R.id.tv1);
		final WebView webView = (WebView)findViewById(R.id.wv1);
		
		final OAuthService s = new ServiceBuilder().
				provider(LinkedInApi.class)
				.apiKey(APIKEY)
				.apiSecret(APISECRET)
				.callback(CALLBACK)
				.build();
		
		final Token requestToken = s.getRequestToken();
		liToken = new LinkedInRequestToken(requestToken.getToken(),requestToken.getSecret());
		final String authURL = s.getAuthorizationUrl(requestToken);
		
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("oauth")){
					webView.setVisibility(View.GONE);
					
					Uri uri = Uri.parse(url);
					String verifier = uri.getQueryParameter("oauth_verifier");
					Verifier v = new Verifier(verifier);
					
					accessToken = s.getAccessToken(requestToken, v);
					
					if(uri.getHost().equals("linkedin")){
						//OAuthRequest req = new OAuthRequest(Verb.GET, "http://api.linkedin.com/v1/people/~/connections:(id,last-name)");
						//s.signRequest(accessToken, req);
						//Response response = req.send();
						//textView.setText(response.getBody());
						//LinkedInAccessToken accessToken = oAuthService.getOAuthAccessToken(liToken, verifier);
						client = factory.createLinkedInApiClient(accessToken.getToken(),accessToken.getSecret()); 
						
			            client.postNetworkUpdate("LinkedIn Android app test");
			            textView.setText("Status Updated");
					}
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		webView.loadUrl(authURL);
		
	}
	
	
	
}
		