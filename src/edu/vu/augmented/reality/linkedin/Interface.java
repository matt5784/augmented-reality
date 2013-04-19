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

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.code.linkedinapi.client.AsyncLinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.enumeration.SearchParameter;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.schema.People;
import com.google.code.linkedinapi.schema.Person;

import edu.vu.augmented.reality.R;

public class Interface extends ListActivity {

    // DO NOT CHANGE
    private final static String APIKEY = "dj4b9ihlwnkv";
    // DO NOT CHANGE
    private final static String APISECRET = "R3aCuzr0NPTmdfkq";

    private String sec, tok, name;
    AsyncLinkedInApiClient client;
    LinkedInApiClient client1;
    LinkedInApiClientFactory factory = LinkedInApiClientFactory.newInstance(
            APIKEY, APISECRET);

    private LinearLayout baseLayout;
    private TextView tv_user;
    // private ListView listView;
    private PersonAdapter adapter;
    private Bitmap[] profile_pics;

    private Map<SearchParameter, String> searchParams;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        setContentView(R.layout.linkedin_interface);
        baseLayout = (LinearLayout) findViewById(R.id.linkedin_interface_layout);

        // listView = (ListView)findViewById(R.id.linkedin_listview);

        Intent intentFromInit = getIntent();

        // All of these extras come from Init
        sec = intentFromInit.getStringExtra("Secret");
        tok = intentFromInit.getStringExtra("Token");

        String fName = null, lName = null;

        name = intentFromInit.getStringExtra("Name");
        String[] lineArray = name.split("\\s");
        fName = lineArray[0];
        lName = lineArray[1];

        LinkedInAccessToken accessToken = new LinkedInAccessToken(tok, sec);

        client = factory.createAsyncLinkedInApiClient(accessToken);
        client1 = factory.createLinkedInApiClient(accessToken);

        // allows the API to retrieve all parts of the ProfileField
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
        searchParams = new HashMap<SearchParameter, String>();

        // Define which search parameters we want to use. More can be added.
        searchParams.put(SearchParameter.FIRST_NAME, fName);
        searchParams.put(SearchParameter.LAST_NAME, lName);

        // search for said user using our name we retrieved from Tesseract
        // we will use Matt for the time being

        // Future<People> bus_card_user = client.searchPeople(searchParams);

        new linkedInSearch(this).execute();

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

    private class linkedInSearch extends AsyncTask<Void, Void, List<Person>> {
        Activity context;

        private ProgressDialog dialog;
        
        public linkedInSearch(Activity ctx) {
            this.context = ctx;
            dialog = new ProgressDialog(context);
        }
        
        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected List<Person> doInBackground(Void... arg0) {

            People people = client1.searchPeople(searchParams,
                    EnumSet.allOf(ProfileField.class));
            List<Person> my_people = people.getPersonList();

            profile_pics = new Bitmap[my_people.size()];
            int i = 0;
            for (Person p : my_people) {
                String url = p.getPictureUrl();
                URL picURL;
                Bitmap pic = null;
                try {
                    picURL = new URL(url);
                    pic = BitmapFactory.decodeStream(picURL.openConnection()
                            .getInputStream());
                    profile_pics[i] = pic;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
            }

            return my_people;

        }

        @Override
        protected void onPostExecute(List<Person> pep_list) {
            String[] people = new String[pep_list.size()];
            int i = 0;
            for (Person p : pep_list) {
                people[i] = p.getFirstName() + " " + p.getLastName();
                i++;
            }

            adapter = new PersonAdapter(context, people, profile_pics);
            setListAdapter(adapter);
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

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

    class PersonAdapter extends ArrayAdapter<String> {
        Activity context;
        String[] people;
        Bitmap[] pro_pics;

        PersonAdapter(Activity context, String[] adapterInput,
                Bitmap[] profile_pics) {
            super(context, R.layout.row, adapterInput);
            this.context = context;
            people = adapterInput;
            pro_pics = profile_pics;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.row, null);

            ((TextView) convertView.findViewById(R.id.tvtitle))
                    .setText(people[position]);

            if (pro_pics[position] != null) {
                ((ImageView) convertView.findViewById(R.id.image))
                        .setImageBitmap(pro_pics[position]);
            } else {
                ((ImageView) convertView.findViewById(R.id.image))
                        .setImageResource(R.drawable.icon_no_photo);
            }

            return convertView;
        }
    }

}
