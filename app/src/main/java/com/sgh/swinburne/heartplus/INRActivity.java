package com.sgh.swinburne.heartplus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
public class INRActivity extends Activity {

    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> INRList;

    private static String url_get_inr = "http://188.166.237.51/android_login_api/get_inr.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INR = "inr";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_HINR = "high_inr";
    private static final String TAG_LINR = "low_inr";

    JSONArray inr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inr_layout);

        /*db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");*/

        //inr_vals = new HashMap<String, String>();
        new LoadINR().execute();
    }

    class LoadINR extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(INRActivity.this);
            pDialog.setMessage("Loading INR values. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            // pDialog.show();
        }

        protected String doInBackground(String... args) {
            db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());
                HashMap<String, String> user = db.getUserDetails();
                String email = user.get("email");
            Log.d("EMAIL: ", email);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", email));
            Log.d("Email PARAMS:", params.toString());
                JSONObject json = jParser.makeHttpRequest(url_get_inr, "GET", params);

                Log.d("INR: ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    inr = json.getJSONArray(TAG_INR);
                    for (int i = 0; i < inr.length(); i++) {
                        JSONObject inrObject = inr.getJSONObject(i);

                        String highINR = inrObject.getString(TAG_HINR);
                        String lowINR = inrObject.getString(TAG_LINR);
                        String txtEmail = inrObject.getString(TAG_EMAIL);

                        Log.d("Email: ", txtEmail);
                        Log.d("High INR: ", highINR);
                        Log.d("Low INR: ", lowINR);


                        if (txtEmail.equals(email)) {

                            break;
                        }
                    }
                    // highINR = (TextView) findViewById(R.id.HINR);
                    // lowINR = (TextView) findViewById(R.id.LINR);

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
