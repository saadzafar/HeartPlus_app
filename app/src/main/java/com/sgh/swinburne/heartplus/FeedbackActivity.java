package com.sgh.swinburne.heartplus;

/**
 * Created by Faizan Elahi on 11/3/2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.sgh.swinburne.heartplus.helper.SQLiteHandler;
import com.sgh.swinburne.heartplus.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FeedbackActivity extends Activity {
    private static final String TAG_SUCCESS = "success";
    private static String url_create_feedback = "http://188.166.237.51/android_login_api/create_feedback.php";
    JSONParser jsonParser = new JSONParser();
    EditText inputEmail;
    EditText inputMessage;
    RatingBar rate;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);


        //inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputMessage = (EditText) findViewById(R.id.inputMessage);

        rate = (RatingBar) findViewById(R.id.ratingBar1);

        LayerDrawable stars = (LayerDrawable) rate.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);


        Button btnCreateFeedback = (Button) findViewById(R.id.btnCreateFeedback);
        btnCreateFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateNewFeedback().execute();
            }
        });
    }

    class CreateNewFeedback extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FeedbackActivity.this);
            pDialog.setMessage("Posting feedback...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected String doInBackground(String... args) {
            db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();
            String email = user.get("email");
            //String email = inputEmail.getText().toString();
            String message = inputMessage.getText().toString();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("message", message));
            JSONObject json = jsonParser.makeHttpRequest(url_create_feedback, "POST", params);
            Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    finish();
                } else {

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
