package com.sgh.swinburne.heartplus;

/**
 * Created by Saad on 10/7/2015.
 */

import com.sgh.swinburne.heartplus.activity.LoginActivity;
import com.sgh.swinburne.heartplus.helper.SQLiteHandler;
import com.sgh.swinburne.heartplus.helper.SessionManager;


import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


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
        Button btn_bpmonitoring = (Button) findViewById(R.id.btn_bpmonitoring);

        // Dashboard Pill Reminder button
        Button btn_pillreminder = (Button) findViewById(R.id.btn_pillreminder);

        // Dashboard Test button
        Button btn_test = (Button) findViewById(R.id.btn_test);

        // Dashboard Test2 button
        Button btn_test2 = (Button) findViewById(R.id.btn_test2);

        // Dashboard Feedback button
        Button btn_feedback = (Button) findViewById(R.id.btn_feedback);

        // Listening to News Feed button click
        btn_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(i);
            }
        });




        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
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
        txtName.setText(name);
        txtEmail.setText(email);

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
