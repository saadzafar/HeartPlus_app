package com.sgh.swinburne.heartplus.pillreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by VivekShah.
 */
public class OnBootReceiver extends BroadcastReceiver
{
    private static final String TAG = ComponentInfo.class.getCanonicalName();

    @Override
    public void onReceive(Context ctx, Intent intent) //when receiver receives intent
    {

        PillManager pillMgr = new PillManager(ctx); //new object to schedule alarms

        PillDbAdapter DatabaseHelper = new PillDbAdapter(ctx);
        DatabaseHelper.open();

        Cursor crsr = DatabaseHelper.fetchAllPills();//get cursor from db

        if(crsr != null) {
            crsr.moveToFirst();//goes to first record in cursor

            int r_IdColumnIndex = crsr.getColumnIndex(PillDbAdapter.KEY_RID);
            int Time_ColumnIndex = crsr.getColumnIndex(PillDbAdapter.KEY_TIME);

            while(crsr.isAfterLast() == false) //while loop check if its the last record.
            {

                Log.d(TAG, "Adding alarm from boot.");
                Log.d(TAG, "Row Id Column Index - " + r_IdColumnIndex);
                Log.d(TAG, "Date Time Column Index - " + Time_ColumnIndex);

                Long row_Id = crsr.getLong(r_IdColumnIndex);
                String date_Time = crsr.getString(Time_ColumnIndex);

                Calendar caldr = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat(PillEditActivity.DATE_TIME_FORMAT);

                try {
                    java.util.Date date = format.parse(date_Time); // date parsed from db string
                    caldr.setTime(date);//calender var updated with correct info.

                    pillMgr.set_Reminder(row_Id, caldr);//schedule new reminder using id from db.
                } catch (java.text.ParseException e)
                {
                    Log.e("OnBootReceiver", e.getMessage(), e);
                }

                crsr.moveToNext();
            }
            crsr.close() ;//close cursor
        }

        DatabaseHelper.close();//close db
    }

}
