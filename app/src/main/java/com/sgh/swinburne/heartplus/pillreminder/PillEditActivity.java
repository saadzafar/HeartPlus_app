package com.sgh.swinburne.heartplus.pillreminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sgh.swinburne.heartplus.R;

import java.sql.RowId;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by VivekShah.
 */

/*Creating Tasks*/

public class PillEditActivity extends Activity {

    private static final int DATE_PICK_DIALOG = 0;
    private static final int TIME_PICK_DIALOG = 1;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";


    //Variable Declaration
    private EditText vNameText;
    private EditText vDoseText;
    private EditText vInstructionsText;
    private Button vDateButton;
    private Button vTimeButton;
    private Button vConfirmButton;
    private Long vRowId;
    private PillDbAdapter DatabaseHelper;
    private Calendar vCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper = new PillDbAdapter(this); //PillDbAdapter in instantiated here
        setContentView(R.layout.pill_edit); //layout setting

        //instantiate
        vCalendar = Calendar.getInstance();
        vNameText = (EditText) findViewById(R.id.name);
        vDoseText = (EditText) findViewById(R.id.dose);
        vInstructionsText = (EditText) findViewById(R.id.instructions);
        vDateButton = (Button) findViewById(R.id.pill_date);
        vTimeButton = (Button) findViewById(R.id.pill_time);

        vConfirmButton = (Button) findViewById(R.id.confirm);

        vRowId = savedInstanceState != null ? savedInstanceState.getLong(PillDbAdapter.KEY_RID) //22
                : null;

        registerButtonListeners_SetDefaultText();
    }

    private void setR_IdFromIntent() {
        if (vRowId == null) {
            Bundle extras = getIntent().getExtras();
            vRowId = extras != null ?
                    extras.getLong(PillDbAdapter.KEY_RID)
                    : null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseHelper.close();//close db
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHelper.open();//open db
        setR_IdFromIntent();//sets id object from intent
        populatetheFields();//method called
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICK_DIALOG:
                return showDate_Picker();
            case TIME_PICK_DIALOG:
                return showTime_Picker();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog showDate_Picker() {

        DatePickerDialog date_Picker = new DatePickerDialog(PillEditActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                vCalendar.set(Calendar.YEAR, year);
                vCalendar.set(Calendar.MONTH, monthOfYear);
                vCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate_ButtonText();
            }
        }, vCalendar.get(Calendar.YEAR), vCalendar.get(Calendar.MONTH), vCalendar.get(Calendar.DAY_OF_MONTH));
        return date_Picker;
    }

    private TimePickerDialog showTime_Picker() {

        TimePickerDialog time_Picker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                vCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                vCalendar.set(Calendar.MINUTE, minute);
                updateTime_ButtonText();
            }
        }, vCalendar.get(Calendar.HOUR_OF_DAY), vCalendar.get(Calendar.MINUTE), true);

        return time_Picker;
    }

    private void registerButtonListeners_SetDefaultText() {

        vDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_PICK_DIALOG);
            }
        });
        vTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_PICK_DIALOG);
            }
        });

        vConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                savetheState(); // call savestate() method
                setResult(RESULT_OK); // informs PillListActivity that everything went as planned in PillEditActivity...Part of Activity parent
                Toast.makeText(PillEditActivity.this, getString(R.string.task_saved_message), Toast.LENGTH_SHORT).show(); //Toast message letting user know task is saved.
                finish(); //Closes remindereditactivity

            }

        });

        updateDate_ButtonText();
        updateTime_ButtonText();
    }

    private void populatetheFields() {

        if (vRowId != null)  //populate form if id is not null
        {
            Cursor reminder = DatabaseHelper.fetchPills(vRowId);//get cursor from db depending on id
            startManagingCursor(reminder);//58
            vNameText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(PillDbAdapter.KEY_NAME)));//set text using cursor where the getcolumnindexorthrow() method gives the column index. get column value by calling getstring(). set text
            vDoseText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(PillDbAdapter.KEY_DOSE)));
            vInstructionsText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(PillDbAdapter.KEY_INSTRUCTIONS)));


            // Get the date from the database and format it for our use.
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date date = null; //new date object
            try {
                String dateString = reminder.getString(reminder.getColumnIndexOrThrow(PillDbAdapter.KEY_TIME));//67
                date = dateTimeFormat.parse(dateString);//68
                vCalendar.setTime(date);//69
            } catch (ParseException e) //70
            {
                Log.e("PillEditActivity", e.getMessage(), e);//error log printed
            }
        } else {
            // This is a new task - add defaults from preferences if set.
            SharedPreferences prefers = PreferenceManager.getDefaultSharedPreferences(this);
            String default_TitleKey = getString(R.string.pref_title_key);
            String default_TimeKey = getString(R.string.pref_time_from_now_key);

            String default_Title = prefers.getString(default_TitleKey, null);
            String default_Time = prefers.getString(default_TimeKey, null);

            if (default_Title != null)
                vNameText.setText(default_Title);

            if (default_Time != null)
                vCalendar.add(Calendar.MINUTE, Integer.parseInt(default_Time));

        }

        updateDate_ButtonText();
        updateTime_ButtonText();

    }

    private void updateTime_ButtonText() {
        // Set the time button text based upon the value from the database
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        String timeForButton = timeFormat.format(vCalendar.getTime());
        vTimeButton.setText(timeForButton);
    }

    private void updateDate_ButtonText() {
        // Set the date button text based upon the value from the database
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(vCalendar.getTime());
        vDateButton.setText(dateForButton);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PillDbAdapter.KEY_RID, vRowId); //save id using onsaveinstancestate() method
    }

    private void savetheState() //determine whether to create new reminder or update existing reminder.
    {
        String Name = vNameText.getText().toString();
        String Dose = vDoseText.getText().toString();
        String Instructions = vInstructionsText.getText().toString();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT); //define simple date format used constants are declared above i.e yyyy-mm-dd goood format to store in db
        String reminderDateTime = dateTimeFormat.format(vCalendar.getTime()); //gets the date and time and stores it in local variable

        if (vRowId == null) //if no rowid could be found create new pill reminder
        {

            long r_id = DatabaseHelper.createPill(Name, Dose, Instructions, reminderDateTime);
            if (r_id > 0) //check to make sure id is greater than 0
            {
                vRowId = r_id; //setting local mRowid to new created id.
            }
        } else {
            DatabaseHelper.updatePill(vRowId, Name, Dose, Instructions, reminderDateTime); // update task
        }

        new PillManager(this).set_Reminder(vRowId, vCalendar); // tell PillManager to setReminder for a row of ID.. at the specficic date and time.
    }


}
