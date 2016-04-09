package com.sgh.swinburne.heartplus.appointment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by VivekShah.
 */
public class AppointmentManager {
    private Context mContext;
    private AlarmManager mAlarmManager;

    public AppointmentManager(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setReminder(Long taskId, Calendar when) {

        Intent i = new Intent(mContext, AlarmReceiver.class);
        i.putExtra(AppointmentDbAdapter.KEY_ROWID, (long) taskId);

        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_ONE_SHOT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
    }
}