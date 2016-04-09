package com.sgh.swinburne.heartplus.appointment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;


public class AppointmentDbAdapter {

    //
    // Databsae Related Constants
    //
    private static final String DATABASE_NAME = "app";
    private static final String DATABASE_TABLE = "appointments";
    private static final int DATABASE_VERSION = 5;

    public static final String KEY_TITLE = "title";
    public static final String KEY_INFO = "body";
    public static final String KEY_DATE_TIME = "reminder_date_time";
    public static final String KEY_ROWID = "_id";


    private static final String TAG = "AppointmentDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation SQL statement
     */
    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TITLE + " text not null, "
                    + KEY_INFO + " text not null, "
                    + KEY_DATE_TIME + " text not null);";


    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }


    public AppointmentDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }


    public AppointmentDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public long createReminder(String title, String body, String reminderDateTime) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_INFO, body);
        initialValues.put(KEY_DATE_TIME, reminderDateTime);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    public boolean deleteReminder(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }


    public Cursor fetchAllReminders() {

        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE,
                KEY_INFO, KEY_DATE_TIME}, null, null, null, null, null);
    }

    public Cursor fetchReminder(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
                                KEY_TITLE, KEY_INFO, KEY_DATE_TIME}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean updateReminder(long rowId, String title, String body, String reminderDateTime) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_INFO, body);
        args.put(KEY_DATE_TIME, reminderDateTime);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
