package com.sgh.swinburne.heartplus.pillreminder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sgh.swinburne.heartplus.R;

import java.util.Calendar;

/**
 * Created by VivekShah.
 */
public class ConfirmActivity extends Activity
{
    private static final int DATE_PICKER_DIALOG = 0;
    private static final int TIME_PICKER_DIALOG = 1;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    private Button vTakeButton;
    private Button vSkipButton;
    private Button vSnoozeButton;
    private Button vSaveButton;
    private Long vRowId;
    private PillDbAdapter DatabaseHelper;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DatabaseHelper = new PillDbAdapter(this);
        setContentView(R.layout.confirm);

        vTakeButton = (Button)findViewById(R.id.take);
        vSkipButton = (Button)findViewById(R.id.skip);
        vSnoozeButton = (Button)findViewById(R.id.snooze);
        vSaveButton = (Button)findViewById(R.id.save);

        vSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(),
                        "Keep It Going",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.take:
                if (checked)
                    Toast.makeText(getApplicationContext(),
                            "Good Job",
                            Toast.LENGTH_LONG).show();
                break;
            case R.id.skip:
                if (checked)
                    Toast.makeText(getApplicationContext(),
                            "Ok?",
                            Toast.LENGTH_LONG).show();
                break;
            case R.id.snooze:
                if (checked)
                    Toast.makeText(getApplicationContext(),
                            "Maybe Later?",
                            Toast.LENGTH_LONG).show();
                break;
            case R.id.save:
                String take = vTakeButton.getText().toString();
                String skip = vSkipButton.getText().toString();
                String snooze = vSnoozeButton.getText().toString();

                DatabaseHelper.InsertRecord(take,skip,snooze);
        }
    }
}
