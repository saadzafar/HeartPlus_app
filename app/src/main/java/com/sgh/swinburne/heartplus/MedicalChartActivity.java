package com.sgh.swinburne.heartplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Saad on 11/5/2015.
 */
public class MedicalChartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicalchart_layout);


        Button btnINR = (Button) findViewById(R.id.btnINR);
        btnINR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching INR Screen
                Intent i = new Intent(getApplicationContext(), INRActivity1.class);
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

        Button btnHR = (Button) findViewById(R.id.btnHR);
        btnHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching Heart Rate Screen
                Intent i = new Intent(getApplicationContext(), HeartRateActivity.class);
                startActivity(i);
            }
        });

        Button btn_emergency = (Button) findViewById(R.id.btn_test2);
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Glucose_Graph.class);
                startActivity(i);
            }
        });

     /*   Button btnVINR = (Button) findViewById(R.id.btn_test2);
        btnVINR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching INR HISTORY Screen
                Intent i = new Intent(getApplicationContext(), INRActivity.class);
                startActivity(i);
            }
        });*/

        Button btnGraph = (Button) findViewById(R.id.btnGraph);
        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching Graph Screen
                Intent i = new Intent(getApplicationContext(), Graph_View.class);
                startActivity(i);
            }
        });
    }
}
