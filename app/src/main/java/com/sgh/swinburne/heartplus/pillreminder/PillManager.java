package com.sgh.swinburne.heartplus.pillreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by VivekShah.
 */
public class PillManager {
    private Context vContext;
    private AlarmManager vAlarmManager;

    public PillManager(Context ctx) {
        vContext = ctx;// instantiated with context object
        vAlarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE); //Alarmmanager got from getSystemService call.
    }

    public void set_Reminder(Long R_Id, Calendar _when) //use taskId, date and alarm to tell when the alarm should fire
    {

        Intent _intent = new Intent(vContext, OnAlarmReceiver.class); //new intent object tells what happens when alarm goes off i.e. call OnAlarmreceiver
        _intent.putExtra(PillDbAdapter.KEY_RID, (long) R_Id); //provide extra info to intent object

        //pendingIntent created to tell all apps that an action needs to be performed
        PendingIntent pintent = PendingIntent.getBroadcast(vContext, 0, _intent, PendingIntent.FLAG_ONE_SHOT); //can only be used once. //uses intent object

        //set() method to schedule alarm.
        //RTC_Wakeup wakes up device at trigger time
        // triggerattime:gettimeinmillis() specify the time the alarm goes off...converts time to milliseconds
        //pi..the pending intent used to act upon when alarm goes off.
        vAlarmManager.set(AlarmManager.RTC_WAKEUP, _when.getTimeInMillis(), pintent);
    }
}
