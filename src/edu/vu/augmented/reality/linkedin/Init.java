package edu.vu.augmented.reality.linkedin;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
 * authentication and retrieval of the access token. LinkedIn-j is used to make
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
    static LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
            .getInstance().createLinkedInOAuthService(APIKEY, APISECRET);
    static LinkedInApiClientFactory factory = LinkedInApiClientFactory
            .newInstance(APIKEY, APISECRET);
    static LinkedInRequestToken liToken;

    private SharedPreferences mPrefs;

    private static OAuthService s;
    private static Token requestToken;
    private static String authURL;
    private static WebView webView;
    private static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.linkedin);
        webView = (WebView) findViewById(R.id.wv1);

        // We should check to see if the user is already logged in, so
        // that we don't always have to load the webview
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean prefToken = mPrefs.getBoolean("AT", false);

        // Grab the name from the intent passed in by HistoryActivity
        Intent fromHistoryActivity = getIntent();
        name = fromHistoryActivity.getStringExtra("Name");

        if (!prefToken) {

            // used to build our OAuthService
            s = new ServiceBuilder().provider(LinkedInApi.class).apiKey(APIKEY)
                    .apiSecret(APISECRET).callback(CALLBACK).build();

            //Must run the network code as an AsyncTask (network code can't be run on UI thread)
            new runNetworkCode().execute();

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
                e.printStackTrace();
            }

        }

    }

    /**
     * runNetworkCode We cannot run network code on the UI stack so we create a
     * separate thread
     * 
     * @author Robert Newton
     * 
     */
    private class runNetworkCode extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog;

        @Override
        protected String doInBackground(String... arg0) {

            requestToken = s.getRequestToken();
            authURL = s.getAuthorizationUrl(requestToken);

            return authURL;

        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(Init.this, "Loading...",
                    "Verifying...");
        }

        @Override
        protected void onPostExecute(String authURL) {
            mProgressDialog.dismiss();
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url.startsWith("oauth")) {

                        webView.setVisibility(View.GONE);
                        final String url1 = url;
                        Thread t1 = new Thread() {
                            public void run() {
                                Uri uri = Uri.parse(url1);
                                String verifier = uri
                                        .getQueryParameter("oauth_verifier");
                                Verifier v = new Verifier(verifier);

                                // use scribe to create our accessToken
                                Token accessToken = s.getAccessToken(
                                        requestToken, v);

                                // Store the tokens in preferences for further
                                // use
                                SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putBoolean("AT", true);
                                editor.putString("Token",
                                        accessToken.getToken());
                                editor.putString("Secret",
                                        accessToken.getSecret());
                                editor.commit();

                                // Start activity
                                Intent intent;
                                try {
                                    intent = new Intent(
                                            Init.this,
                                            Class.forName("edu.vu.augmented.reality.linkedin.Interface"));
                                    intent.putExtra("Token",
                                            accessToken.getToken());
                                    intent.putExtra("Secret",
                                            accessToken.getSecret());
                                    intent.putExtra("Name", name);
                                    startActivity(intent);
                                } catch (ClassNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                finish();

                            }
                        };
                        t1.start();

                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, url);

                }
            });
            webView.loadUrl(authURL);
        }

    }

}
