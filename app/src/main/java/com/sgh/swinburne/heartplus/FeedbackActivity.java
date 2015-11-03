package com.sgh.swinburne.heartplus;

/**
 * Created by Saad on 11/3/2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FeedbackActivity extends Activity {
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputEmail;
    EditText inputMessage;
    private static String url_create_feedback = "http://188.166.237.51/android_login_api/create_feedback.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);


        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputMessage = (EditText) findViewById(R.id.inputMessage);


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
            String email = inputEmail.getText().toString();
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
