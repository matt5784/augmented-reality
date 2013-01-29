package edu.vu.augmented.reality;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientException;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;

public class LinkedInExample extends Activity {
    public static final String CONSUMER_KEY             = "dj4b9ihlwnkv";
    public static final String CONSUMER_SECRET          = "R3aCuzr0NPTmdfkq";    
    public static final String APP_NAME                 = "Augmented Reality";
    public static final String OAUTH_CALLBACK_SCHEME    = "linkedin";
    public static final String OAUTH_CALLBACK_HOST      = "callback";
    public static final String OAUTH_CALLBACK_URL       = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
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

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        setContentView(tv);
        final SharedPreferences pref    = getSharedPreferences(OAUTH_PREF, MODE_PRIVATE);
        final String token              = pref.getString(PREF_TOKEN, null);
        final String tokenSecret        = pref.getString(PREF_TOKENSECRET, null);
        if (token == null || tokenSecret == null) {
            startAutheniticate();
        } else {
            LinkedInAccessToken accessToken = new LinkedInAccessToken(token, tokenSecret);
            showCurrentUser(accessToken);
        }
    }//end method

    void startAutheniticate() {
        new Thread(){//added because this will make code work on post API 10 
            @Override
            public void run(){
                final LinkedInRequestToken liToken  = oAuthService.getOAuthRequestToken(OAUTH_CALLBACK_URL ); 
                final String uri                    = liToken.getAuthorizationUrl();
                final SharedPreferences pref        = getSharedPreferences(OAUTH_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor     = pref.edit(); 
                editor.putString(PREF_REQTOKENSECRET, liToken.getTokenSecret());
                editor.commit();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(i);
             }
        }.start();
    }//end method

    void finishAuthenticate(final Uri uri) {
        new Thread(){
            @Override
            public void run(){
                Looper.prepare();
                if (uri != null && uri.getScheme().equals(OAUTH_CALLBACK_SCHEME)) {
                    final String problem = uri.getQueryParameter(OAUTH_QUERY_PROBLEM);
                    if (problem == null) {
                        final SharedPreferences pref                = getSharedPreferences(OAUTH_PREF, MODE_PRIVATE);
                        final String request_token_secret           = pref.getString(PREF_REQTOKENSECRET, null);
                        final String query_token                    = uri.getQueryParameter(OAUTH_QUERY_TOKEN);
                        final LinkedInRequestToken request_token    = new LinkedInRequestToken(query_token, request_token_secret);
                        final LinkedInAccessToken accessToken       = oAuthService.getOAuthAccessToken(request_token, uri.getQueryParameter(OAUTH_QUERY_VERIFIER));
                        SharedPreferences.Editor editor = pref.edit(); 
                        editor.putString(PREF_TOKEN, accessToken.getToken());
                        editor.putString(PREF_TOKENSECRET, accessToken.getTokenSecret());
                        editor.remove(PREF_REQTOKENSECRET);
                        editor.commit();
                        showCurrentUser(accessToken);
                    } else {
                        Toast.makeText(getApplicationContext(), "Application down due OAuth problem: " + problem, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                Looper.loop();
            }
        }.start();
    }//end method

    void clearTokens() {
        getSharedPreferences(OAUTH_PREF, MODE_PRIVATE).edit().remove(PREF_TOKEN).remove(PREF_TOKENSECRET).remove(PREF_REQTOKENSECRET).commit();
    }//end method

    void showCurrentUser(final LinkedInAccessToken accessToken) {
        new Thread(){
            @Override
            public void run(){
                Looper.prepare();
                final LinkedInApiClient client = factory.createLinkedInApiClient(accessToken);
                try {
                    final Person p = client.getProfileForCurrentUser();
                    // /////////////////////////////////////////////////////////
                    // here you can do client API calls ...
                    // client.postComment(arg0, arg1);
                    // client.updateCurrentStatus(arg0);
                    // or any other API call (this sample only check for current user
                    // and shows it in TextView)
                    // /////////////////////////////////////////////////////////             
                    runOnUiThread(new Runnable() {//updating UI thread from different thread not a good idea...
                        public void run() {
                            tv.setText(p.getLastName() + ", " + p.getFirstName());
                        }
                    });
                    //or use Toast
                    Toast.makeText(getApplicationContext(), "Lastname:: "+p.getLastName() + ", First name: " + p.getFirstName(), 1).show();
                } catch (LinkedInApiClientException ex) {
                    clearTokens();
                    Toast.makeText(getApplicationContext(),
                        "Application down due LinkedInApiClientException: "+ ex.getMessage() + " Authokens cleared - try run application again.",
                        Toast.LENGTH_LONG).show();
                    finish();
                }
                Looper.loop();
            }
        }.start();
    }//end method

    @Override
    protected void onNewIntent(Intent intent) {
        finishAuthenticate(intent.getData());
    }//end method
}//end class