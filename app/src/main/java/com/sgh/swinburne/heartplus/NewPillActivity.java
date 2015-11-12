package com.sgh.swinburne.heartplus;

/**
 * Created by Saad on 11/12/2015.
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewPillActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputSideEffect;
    EditText inputDescription;
    EditText inputIndication;

    // url to create new product
    private static String url_create_pill = "http://188.166.237.51/android_login_api/create_pill.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_pill_layout);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputSideEffect = (EditText) findViewById(R.id.inputSideEffect);
        inputDescription = (EditText) findViewById(R.id.inputDescription);
        inputIndication = (EditText) findViewById(R.id.inputIndication);

        // Create button
        Button btnCreatePill = (Button) findViewById(R.id.btnCreatePill);

        // button click event
        btnCreatePill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewPill().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     */
    class CreateNewPill extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewPillActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String sideeffect = inputSideEffect.getText().toString();
            String description = inputDescription.getText().toString();
            String indication = inputIndication.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("sideeffect", sideeffect));
            params.add(new BasicNameValuePair("description", description));
            params.add(new BasicNameValuePair("indication", indication));

            Log.e("params ", params.toString());

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_pill,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
