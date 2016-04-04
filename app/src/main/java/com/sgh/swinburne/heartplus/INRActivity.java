package com.sgh.swinburne.heartplus;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sgh.swinburne.heartplus.helper.SQLiteHandler;
import com.sgh.swinburne.heartplus.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Saad on 11/5/2015.
 */
public class INRActivity extends ListActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> INRList;

    private static String url_all_inr = "http://188.166.237.51/android_login_api/get_inr_all.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_INR = "inr_monitoring";
    private static final String TAG_DATE = "date";
    private static final String TAG_VALUE = "value";
    private SQLiteHandler db;
    private SessionManager session;

    JSONArray inr = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_inr);

        INRList = new ArrayList<HashMap<String, String>>();
        new LoadAllINR().execute();

        ListView lv = getListView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    class LoadAllINR extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(INRActivity.this);
            pDialog.setMessage("Loading INR History. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // SqLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // session manager
            session = new SessionManager(getApplicationContext());

            // Fetching user details from sqlite
            HashMap<String, String> user = db.getUserDetails();

            String email1 = user.get("email");

            params.add(new BasicNameValuePair("email", email1));
            Log.d("email", email1);

            JSONObject json = jParser.makeHttpRequest(url_all_inr, "GET", params);
            Log.d("All INR", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    inr = json.getJSONArray(TAG_INR);

                    for (int i = 0; i < inr.length(); i++) {
                        JSONObject c = inr.getJSONObject(i);

                        String email = c.getString(TAG_EMAIL);
                        String value = c.getString(TAG_VALUE);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_EMAIL, email);
                        map.put(TAG_VALUE, value);

                        INRList.add(map);
                    }
                } else {
                    Intent i = new Intent(getApplicationContext(), INRActivity1.class);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }
}
