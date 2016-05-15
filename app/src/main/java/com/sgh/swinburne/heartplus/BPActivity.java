package com.sgh.swinburne.heartplus;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

import java.util.Calendar;
import java.util.HashMap;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Saad on 11/5/2015.
 */
public class BPActivity extends Activity implements View.OnClickListener {
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;

    JSONParser jsonParser = new JSONParser();
    EditText inputS;
    EditText inputDate;
    EditText inputTime;
    EditText inputD;
    EditText remark;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day, hour, minute;
    Button btnDatePicker, btnTimePicker;
    TextView txtDate, txtTime;

    private static String url_create_bp = "http://188.166.237.51/android_login_api/create_bp.php";
    private static final String TAG_SUCCESS = "success";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_layout);
        inputS = (EditText) findViewById(R.id.inputS);
        inputD = (EditText) findViewById(R.id.inputD);
        remark = (EditText) findViewById(R.id.remark);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

       // dateView = (TextView) findViewById(R.id.inputDate);
       // timeView = (TextView) findViewById(R.id.inputTime);

       /* calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        showDate(year, month + 1, day);*/
       // showTime(hour, minute);

        Button btnCreateGlucose = (Button) findViewById(R.id.btnCreateGlucose);
        btnCreateGlucose.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    new CreateNewBP().execute();
                                                }
                                            }
        );
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

                           txtTime.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));

                        }
                    }, hour, minute, false);
            timePickerDialog.show();
        }

        if(inputS.getText().toString().length()==0)
        {
           // Toast.makeText(getApplicationContext(), "Invalid Systolic Value", Toast.LENGTH_LONG).show();
            inputS.setError("Invalid Systolic Value");
            return;
        }

        if(inputD.getText().toString().length()==0)
        {
            //Toast.makeText(getApplicationContext(), "Invalid Dystolic Value", Toast.LENGTH_LONG).show();
            inputD.setError("Invalid Dystolic Value");
            return;
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Validated Succesfully", Toast.LENGTH_LONG).show();
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


        private void showTime(int hour, int minute) {
            timeView.setText(new StringBuilder().append(hour).append(":").append(minute));
            Log.d("time: ", timeView.toString());

        }

        ;

        private void showDate(int year, int month, int day) {
            dateView.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
            Log.d("date: ", dateView.toString());
        } */

        class CreateNewBP extends AsyncTask<String, String, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(BPActivity.this);
                pDialog.setMessage("Posting Blood Pressure...");
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
                String time = txtTime.getText().toString();
                String systolic = inputS.getText().toString();
                String diastolic = inputD.getText().toString();
                String inputremark = remark.getText().toString();
                String date = txtDate.getText().toString();
                Log.d("BPS ", systolic);
                Log.d("BPD ", diastolic);
                Log.d("date ", date);
                Log.d("time ", time);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("systolic", systolic));
                params.add(new BasicNameValuePair("diastolic", diastolic));
                params.add(new BasicNameValuePair("date", date));
                params.add(new BasicNameValuePair("time", time));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("remark", inputremark));
                Log.d("params ", params.toString());
                JSONObject json = jsonParser.makeHttpRequest(url_create_bp, "POST", params);
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
