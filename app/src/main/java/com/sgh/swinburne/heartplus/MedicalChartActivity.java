package com.sgh.swinburne.heartplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Saad on 11/5/2015.
 */
public class MedicalChartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);


        Button btnINR = (Button) findViewById(R.id.btnINR);
        btnINR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching INR Screen
                Intent i = new Intent(getApplicationContext(), INRActivity.class);
                startActivity(i);
            }
        });

        Button btnBP = (Button) findViewById(R.id.btnBP);
        btnBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching Blood Pressure Screen
                Intent i = new Intent(getApplicationContext(), BPActivity.class);
                startActivity(i);
            }
        });

        Button btnGlucose = (Button) findViewById(R.id.btnGlucose);
        btnGlucose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching Glucose Screen
                Intent i = new Intent(getApplicationContext(), GlucoseActivity.class);
                startActivity(i);
            }
        });
    }
}
