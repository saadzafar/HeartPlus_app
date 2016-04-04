package com.sgh.swinburne.heartplus.pillreminder;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by VivekShah.
 */
public abstract class WakeService extends IntentService
{
    abstract void doWork(Intent intent);

    public static final String LOCK_NAME_STATIC="com.vivekshah.pillreminder.Static";
    private static PowerManager.WakeLock lockStatic=null;

    public static void acquireStaticLock(Context context) {
        getLock(context).acquire();
    }//calls getlock()..aquire() method called to ensure device is in same state as requested

    synchronized private static PowerManager.WakeLock getLock(Context context) //getlock() returns power-manager.wakelock which tells os to keep device on.
    {
        if (lockStatic==null) {
            PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);//retrieves powermanager from getsysservice. used to create the lock
            lockStatic=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);//create new wakelock ...partial wakelock informs os to leave cpu on but not screen...lock name static for debugging
            lockStatic.setReferenceCounted(true);//tells powermanager that this has been counted.`
        }
        return(lockStatic); //returns WakeLock to caller
    }

    //constructor with child, name
    public WakeService(String name) {
        super(name);
    } //18

    @Override
    final protected void onHandleIntent(Intent intent) //call of intent service as soon as service is started
    {
        try {
            doWork(intent);//call doReminderWork to perform neccessary work
        }
        finally {
            getLock(this).release(); //release wakelock or else device will be on until reboot.
        }
    }
}
