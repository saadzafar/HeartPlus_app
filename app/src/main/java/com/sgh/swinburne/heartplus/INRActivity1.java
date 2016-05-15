package com.sgh.swinburne.heartplus;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sgh.swinburne.heartplus.helper.SQLiteHandler;
import com.sgh.swinburne.heartplus.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Saad on 11/6/2015.
 */
public class INRActivity1 extends Activity implements View.OnClickListener {
    private static final String TAG_SUCCESS = "success";
    private static String url_create_inr = "http://188.166.237.51/android_login_api/create_inr.php";
    JSONParser jsonParser = new JSONParser();
    EditText inputINR;
    EditText inputDate;
    Button btnDatePicker;
    EditText txtDate;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private String inr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inr_layout);


        inputINR = (EditText) findViewById(R.id.inputINR);
      //  dateView = (TextView) findViewById(R.id.inputDate);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        txtDate=(EditText)findViewById(R.id.inputDate);
        btnDatePicker.setOnClickListener(this);

      /*  calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day); */


        Button btnCreateINR = (Button) findViewById(R.id.btnCreateINR);
        btnCreateINR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (inputINR.getText().toString().length() == 0) {
                    // Toast.makeText(getApplicationContext(), "Invalid Systolic Value", Toast.LENGTH_LONG).show();
                    inputINR.setError("Invalid INR Value");
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Validated Succesfully", Toast.LENGTH_LONG).show();
                }
                new CreateNewINR().execute();
                inr = inputINR.getText().toString();
               // Intent myIntent = new Intent(view.getContext(), Graph_View.class);
               // myIntent.putExtra("inr", inr);
               // startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            if (year == 0 || month == 0 || day == 0) {
                Calendar c=Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
            }

            DatePickerDialog mDatePicker=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                {
                    year = selectedyear;
                    month = selectedmonth;
                    day = selectedday;
                    txtDate.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));
                }
            },year, month, day);
            mDatePicker.setTitle("Please select date");
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.show();
        }

    }

  /*  @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
        Log.d("date: ", dateView.toString());
    } */

    class CreateNewINR extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(INRActivity1.this);
            pDialog.setMessage("Posting INR...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected String doInBackground(String... args) {
            db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();
            String email = user.get("email");
            Log.d("email ", email);
            String value = inputINR.getText().toString();
            String date = txtDate.getText().toString();
            Log.d("value ", value);
            Log.d("date ", date);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("value", value));
            params.add(new BasicNameValuePair("date", date));
            params.add(new BasicNameValuePair("email", email));
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
