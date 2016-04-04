package com.sgh.swinburne.heartplus.pillreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.util.Log;

/**
 * Created by VivekShah.
 */
public class OnAlarmReceiver extends BroadcastReceiver
{
    private static final String TAG = ComponentInfo.class.getCanonicalName();


    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.d(TAG, "Received wake up from alarm manager."); //Debug

        long r_id = intent.getExtras().getLong(PillDbAdapter.KEY_RID); //get db id from intent after handling it

        WakeService.acquireStaticLock(ctx); // inform Wakereminderervice to get static lock on cpu to keep it from sleeping while using app

        Intent itent = new Intent(ctx, PillService.class); // new intent object which starts PillService
        itent.putExtra(PillDbAdapter.KEY_RID, r_id);//put id into intent to start work in PillService
        ctx.startService(itent);//starts PillService
    }

}