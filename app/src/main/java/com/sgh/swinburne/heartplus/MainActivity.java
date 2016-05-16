package com.sgh.swinburne.heartplus;


/**
 * Created by Saad on 10/7/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sgh.swinburne.heartplus.activity.LoginActivity;
import com.sgh.swinburne.heartplus.appointment.AppointmentListActivity;
import com.sgh.swinburne.heartplus.helper.SQLiteHandler;
import com.sgh.swinburne.heartplus.helper.SessionManager;
import com.sgh.swinburne.heartplus.pillreminder.PillListActivity;

import java.util.HashMap;


public class MainActivity extends Activity {
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Creating all buttons instances
         * */
        // Dashboard Profile button
        Button btn_profile = (Button) findViewById(R.id.btn_profile);

        // Dashboard BP Monitoring button
        Button btn_MC = (Button) findViewById(R.id.btn_MedicalCharts);

        // Dashboard Pill Reminder button
        Button btn_pillreminder = (Button) findViewById(R.id.btn_pillreminder);

        // Dashboard Test button
        Button btn_test = (Button) findViewById(R.id.btn_test);

        // Dashboard Emergency button
        Button btn_emergency = (Button) findViewById(R.id.btn_test2);

        // Dashboard Feedback button
        Button btn_feedback = (Button) findViewById(R.id.btn_feedback);


        // Listening to Profile button click
        btn_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching profile Screen
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(i);
            }
        });

        //listening to Feedback button click
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launching feedback screen
                Intent i = new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(i);
            }
        });

        //listening to Emergency button click
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String emergency_number = "999";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel: " + emergency_number));
                startActivity(intent);
            }
        });

        //listening to Medical Charts button click
        btn_MC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MedicalChartActivity.class);
                startActivity(i);
            }
        });

        //listening to Appointment button click
        btn_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AppointmentListActivity.class);
                startActivity(i);
            }
        });

        //listening to Pill Reminder button click
        btn_pillreminder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PillListActivity.class);
                startActivity(i);
            }
        });

        // txtName = (TextView) findViewById(R.id.name);
        // txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        // txtName.setText(name);
        // txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
