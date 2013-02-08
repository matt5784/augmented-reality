package edu.vu.augmented.reality;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;


public class BadExample extends Activity {
	//DO NOT CHANGE 
    public static final String CONSUMER_KEY             = "dj4b9ihlwnkv";
    //DO NOT CHANGE
    public static final String CONSUMER_SECRET          = "R3aCuzr0NPTmdfkq";  
    
    public static final String APP_NAME                 = "Augmented Reality";
    public static final String OAUTH_CALLBACK_SCHEME    = "host";
    public static final String OAUTH_CALLBACK_HOST      = "callback";
    public static final String OAUTH_CALLBACK_URL       = "oauth-testing:///";
    static final String OAUTH_QUERY_TOKEN               = "oauth_token";
    static final String OAUTH_QUERY_VERIFIER            = "oauth_verifier";
    static final String OAUTH_QUERY_PROBLEM             = "oauth_problem";
    static final String OAUTH_PREF                      = "AppPreferences";
    static final String PREF_TOKEN                      = "linkedin_token";
    static final String PREF_TOKENSECRET                = "linkedin_token_secret";
    static final String PREF_REQTOKENSECRET             = "linkedin_request_token_secret";

    final LinkedInOAuthService oAuthService             = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(CONSUMER_KEY, CONSUMER_SECRET);
    final LinkedInApiClientFactory factory              = LinkedInApiClientFactory.newInstance(CONSUMER_KEY, CONSUMER_SECRET);
    LinkedInRequestToken liToken;
    LinkedInApiClient client;

    TextView tv = null;
    WebView wv = null;
    
    @Override
    protected void onCreate(Bundle intent){
    	super.onCreate(intent);
    	setContentView(R.layout.linkedin);
    	
    	liToken = oAuthService.getOAuthRequestToken(OAUTH_CALLBACK_URL);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(liToken.getAuthorizationUrl()));
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        WebView webView = new WebView(this);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (!v.hasFocus()) {
                        v.requestFocus();
                    }
                    break;
                }
                return false;
            }
        });
        
        webView.loadUrl(liToken.getAuthorizationUrl());
        //mainLayout.removeAllViews();
        //mainLayout.addView(webView);
    	
    }
    
    @Override
    protected void onNewIntent(Intent intent){
    	super.onNewIntent(intent);
        dealWithLinkedInResponse(intent);
    }
    
    private void dealWithLinkedInResponse(Intent intent) {
        Uri uri = intent.getData();
        System.out.println("URI=" + uri);
        if (uri != null && uri.toString().startsWith(OAUTH_CALLBACK_URL)) {
            String verifier = uri.getQueryParameter("oauth_verifier");
            LinkedInAccessToken accessToken = oAuthService.getOAuthAccessToken(liToken, verifier);
            client = factory.createLinkedInApiClient(accessToken); 
            client.postNetworkUpdate("LinkedIn Android app test");
        }
    }
    
}

    