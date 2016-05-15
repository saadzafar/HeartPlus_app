package com.sgh.swinburne.heartplus;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sgh.swinburne.heartplus.helper.SQLiteHandler;
import com.sgh.swinburne.heartplus.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Saad on 11/5/2015.
 */
public class GlucoseActivity extends Activity implements View.OnClickListener {

    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;

    JSONParser jsonParser = new JSONParser();
    EditText inputGlucose;
    EditText inputDate;
    EditText inputMeal;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day, hour, minute;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;

    private static String url_create_inr = "http://188.166.237.51/android_login_api/create_glucose.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glucose_layout);


        inputGlucose = (EditText) findViewById(R.id.inputGlucose);

        inputMeal = (EditText) findViewById(R.id.inputMeal);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        Button btnCreateGlucose = (Button) findViewById(R.id.btnCreateGlucose);
        btnCreateGlucose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateNewGlucose().execute();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, year, month, day);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, hour, minute, false);
            timePickerDialog.show();
        }

        if(inputGlucose.getText().toString().length()==0)
        {
            // Toast.makeText(getApplicationContext(), "Invalid Systolic Value", Toast.LENGTH_LONG).show();
            inputGlucose.setError("Invalid INR Value");
            return;
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Validated Succesfully", Toast.LENGTH_LONG).show();
        }
    }

    class CreateNewGlucose extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GlucoseActivity.this);
            pDialog.setMessage("Posting Glucose...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();

        }

        protected String doInBackground(String... args) {
            db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();
            String email = user.get("email");
            Log.d("email ", email);
            String value = inputGlucose.getText().toString();
           // String date = dateView.getText().toString();
            String meal = inputMeal.getText().toString();
            Log.d("value ", value);
           // Log.d("date ", date);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("value", value));
          //  params.add(new BasicNameValuePair("date", date));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("meal", meal));
            JSONObject json = jsonParser.makeHttpRequest(url_create_inr, "POST", params);
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
