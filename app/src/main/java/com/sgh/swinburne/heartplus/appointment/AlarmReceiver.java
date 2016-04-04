package com.sgh.swinburne.heartplus.appointment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.util.Log;

/**
 * Created by VivekShah.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = ComponentInfo.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received wake up from alarm manager.");

        long rowid = intent.getExtras().getLong(AppointmentDbAdapter.KEY_ROWID);

        WakeReminderIntentService.acquireStaticLock(context);

        Intent i = new Intent(context, AppointmentServices.class);
        i.putExtra(AppointmentDbAdapter.KEY_ROWID, rowid);
        context.startService(i);

    }
}
