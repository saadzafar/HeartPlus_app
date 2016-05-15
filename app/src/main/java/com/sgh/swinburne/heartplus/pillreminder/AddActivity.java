package com.sgh.swinburne.heartplus.pillreminder;

/**
 * Created by faizan on 4/20/2016.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sgh.swinburne.heartplus.MainActivity;
import com.sgh.swinburne.heartplus.R;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class AddActivity extends Activity {
    int hour, minute;
    TextView timeLabel;
    EditText editText;
    EditText editText1;
    EditText editText2;
    PillBox pillBox = new PillBox();
    // Time picker dialog that pops up when the user presses the time string
    // This method specifies the hour and minute of the time picker before the user
    // does anything
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minuteOfHour) {
            hour = hourOfDay;
            minute = minuteOfHour;
            timeLabel.setText(setTime(hour, minute));
        }
    };
    private AlarmManager alarmManager;
    private PendingIntent operation;
    private boolean dayOfWeekList[] = new boolean[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pill_add);

        // Set up the time string on the page
        timeLabel=(TextView)findViewById(R.id.reminder_time);
        Typeface lightFont = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf");
        timeLabel.setTypeface(lightFont);

        // Get the time right now and set it to be the time string
        Calendar rightNow = Calendar.getInstance();
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        minute = rightNow.get(Calendar.MINUTE);
        timeLabel.setText(setTime(hour, minute));

        timeLabel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(AddActivity.this,
                        //R.style.Theme_AppCompat_Dialog,
                        t,
                        hour,
                        minute,
                        false).show();
            }
        });
        timeLabel.setText(setTime(hour, minute));


        Button btnSetAlarm = (Button) findViewById(R.id.btn_set_alarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                int checkBoxCounter = 0;

                editText = (EditText) findViewById(R.id.name);
                /*Add dose*/
                String pill_name = editText.getText().toString();

                editText1 = (EditText) findViewById(R.id.dose);
                String dose_name = editText1.getText().toString();

                editText2 = (EditText) findViewById(R.id.instructions);
                String instruction_name = editText2.getText().toString();
                /** Updating model */

                if (editText.getText().toString().length() == 0) {
                    // Toast.makeText(getApplicationContext(), "Invalid Systolic Value", Toast.LENGTH_LONG).show();
                    editText.setError("Please insert Pill name");
                    return;
                }

                if (editText1.getText().toString().length() == 0) {
                    // Toast.makeText(getApplicationContext(), "Invalid Systolic Value", Toast.LENGTH_LONG).show();
                    editText1.setError("Please insert dosage of medication");
                    return;
                }

                if (editText2.getText().toString().length() == 0) {
                    // Toast.makeText(getApplicationContext(), "Invalid Systolic Value", Toast.LENGTH_LONG).show();
                    editText2.setError("Please insert how to take medication");
                    return;
                } else {
                    Toast.makeText(getBaseContext(), "Alarm for " + pill_name + " is set successfully", Toast.LENGTH_LONG).show();
                    Intent returnHome = new Intent(getBaseContext(), PillListActivity.class);
                    startActivity(returnHome);
                    finish();
                }


                /** Updating model */
                Alarm alarm = new Alarm();

                /** If Pill does not already exist */
                if (!pillBox.pillExist(getApplicationContext(), pill_name)) {
                    Pill pill = new Pill();
                    pill.setPillName(pill_name);
                    pill.setDose(dose_name);
                    pill.setInstruction(instruction_name);
                    alarm.setHour(hour);
                    alarm.setMinute(minute);
                    alarm.setPillName(pill_name);
                    alarm.setdose(dose_name);
                    alarm.setinstruction(instruction_name);
                    alarm.setDayOfWeek(dayOfWeekList);
                    pill.addAlarm(alarm);
                    long pillId = pillBox.addPill(getApplicationContext(), pill);
                    pill.setPillId(pillId);
                    pillBox.addAlarm(getApplicationContext(), alarm, pill);
                } else { // If Pill already exists
                    Pill pill = pillBox.getPillByName(getApplicationContext(), pill_name);
                    alarm.setHour(hour);
                    alarm.setMinute(minute);
                    alarm.setPillName(pill_name);
                    alarm.setdose(dose_name);
                    alarm.setinstruction(instruction_name);
                    alarm.setDayOfWeek(dayOfWeekList);
                    pill.addAlarm(alarm);
                    pillBox.addAlarm(getApplicationContext(), alarm, pill);
                }
                List<Long> ids = new LinkedList<Long>();
                try {
                    List<Alarm> alarms = pillBox.getAlarmByPill(getApplicationContext(), pill_name);
                    for (Alarm tempAlarm : alarms) {
                        if (tempAlarm.getHour() == hour && tempAlarm.getMinute() == minute) {
                            ids = tempAlarm.getIds();
                            break;
                        }
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < 7; i++) {
                    if (dayOfWeekList[i] && pill_name.length() != 0) {

                        int dayOfWeek = i + 1;

                        long _id = ids.get(checkBoxCounter);
                        int id = (int) _id;
                        checkBoxCounter++;

                        /** This intent invokes the activity AlertActivity, which in turn opens the AlertAlarm window */
                        Intent intent = new Intent(getBaseContext(), AlertActivity.class);
                        intent.putExtra("pill_name", pill_name);
                        intent.putExtra("dose", dose_name);
                        intent.putExtra("instruction", instruction_name);

                        operation = PendingIntent.getActivity(getBaseContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        /** Getting a reference to the System Service ALARM_SERVICE */
                        alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

                        /** Creating a calendar object corresponding to the date and time set by the user */
                        Calendar calendar = Calendar.getInstance();

                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                        /** Converting the date and time in to milliseconds elapsed since epoch */
                        long alarm_time = calendar.getTimeInMillis();

                        if (calendar.before(Calendar.getInstance()))
                            alarm_time += AlarmManager.INTERVAL_DAY * 7;

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm_time,
                                AlarmManager.INTERVAL_DAY * 7, operation);
                    }

                }

            }


        });

        Button btnQuitAlarm = (Button) findViewById(R.id.btn_cancel_alarm);
        btnQuitAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnHome = new Intent(getBaseContext(), PillListActivity.class);
                startActivity(returnHome);
                finish();
            }
        });
    }

    @Override
    /** Inflate the menu; this adds items to the action bar if it is present */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        /** Checking which checkbox was clicked */
        switch(view.getId()) {
            case R.id.checkbox_monday:
                dayOfWeekList[1] = checked;
                break;
            case R.id.checkbox_tuesday:
                dayOfWeekList[2] = checked;
                break;
            case R.id.checkbox_wednesday:
                dayOfWeekList[3] = checked;
                break;
            case R.id.checkbox_thursday:
                dayOfWeekList[4] = checked;
                break;
            case R.id.checkbox_friday:
                dayOfWeekList[5] = checked;
                break;
            case R.id.checkbox_saturday:
                dayOfWeekList[6] = checked;
                break;
            case R.id.checkbox_sunday:
                dayOfWeekList[0] = checked;
                break;
            case R.id.every_day:
                LinearLayout ll = (LinearLayout) findViewById(R.id.checkbox_layout);
                for (int i = 0; i < ll.getChildCount(); i++) {
                    View v = ll.getChildAt(i);
                    ((CheckBox) v).setChecked(checked);
                    onCheckboxClicked(v);
                }
                break;
        }
    }

    @Override
    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        Intent returnHome = new Intent(getBaseContext(), MainActivity.class);
        startActivity(returnHome);
        finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method takes hours and minute as input and returns
     * a string that is like "12:01pm"
     */
    public String setTime(int hour, int minute) {
        String am_pm = (hour < 12) ? "am" : "pm";
        int nonMilitaryHour = hour % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;
        String minuteWithZero;
        if (minute < 10)
            minuteWithZero = "0" + minute;
        else
            minuteWithZero = "" + minute;
        return nonMilitaryHour + ":" + minuteWithZero + am_pm;
    }

    @Override
    public void onBackPressed() {
        Intent returnHome = new Intent(getBaseContext(), PillListActivity.class);
        startActivity(returnHome);
        finish();

    }
}