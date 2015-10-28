package com.sgh.swinburne.heartplus;

/**
 * Created by Saad on 10/26/2015.
 */


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


import com.sgh.swinburne.heartplus.R;
import com.sgh.swinburne.heartplus.helper.SQLiteHandler;
import com.sgh.swinburne.heartplus.helper.SessionManager;

import java.util.HashMap;

public class ProfileActivity extends Activity {

    private SQLiteHandler db;
    private SessionManager session;
    private TextView txtName;
    private TextView txtEmail;



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        txtName = (TextView) findViewById(R.id.Tname);
        txtEmail = (TextView) findViewById(R.id.Temail);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

    }

}
