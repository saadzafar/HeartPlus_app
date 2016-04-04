package com.sgh.swinburne.heartplus.pillreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sgh.swinburne.heartplus.R;

/**
 * Created by VivekShah.
 */
public class PillService extends WakeService //reminderservice inherits wakereminderintentservice
{
    public PillService() {
        super("PillService");
    }

    @Override
    void doWork(Intent intent) {
        Log.d("PillService", "Work.");
        Long r_Id = intent.getExtras().getLong(PillDbAdapter.KEY_RID); //get id

        NotificationManager Notifymgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(this, ConfirmActivity.class);
        notifyIntent.putExtra(PillDbAdapter.KEY_RID, r_Id);

        PendingIntent pint = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_ONE_SHOT);

        //Notification note  = new Notification.Builder(this)
        NotificationCompat.Builder notify = new NotificationCompat.Builder(this);
        notify.setContentTitle("Time to take your pills")
                .setContentText("Pill Reminder")
                .setSmallIcon(R.mipmap.ic_launcher).build();
        notify.setAutoCancel(true);
        notify.setContentIntent(pint);
        //   note.defaults |= Notification.DEFAULT_SOUND;
        //   note.flags |= Notification.FLAG_AUTO_CANCEL;

      /*  Notification note=new Notification(android.R.drawable.stat_sys_warning, getString(R.string.notify_new_task_message), System.currentTimeMillis());
		note.setLatestInfo(this, getString(R.string.notify_new_task_title), getString(R.string.notify_new_task_message), pi);
		note.defaults |= Notification.DEFAULT_SOUND;
		note.flags |= Notification.FLAG_AUTO_CANCEL;*/

        // An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value).
        // I highly doubt this will ever happen. But is good to note.
        int _id = (int)((long)r_Id);
        Notifymgr.notify(_id, notify.build());


    }

}
