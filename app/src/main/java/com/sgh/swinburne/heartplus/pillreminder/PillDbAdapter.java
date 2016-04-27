package com.sgh.swinburne.heartplus.pillreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by VivekShah.
 */
public class PillDbAdapter
{
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "reminders";
    private static final int DATABASE_VERSION = 7;

    public static final String KEY_NAME = "name";
    public static final String KEY_DOSE = "dose";
    public static final String KEY_TIME = "DateTime";
    public static final String KEY_INSTRUCTIONS = "instructions";
    public static final String KEY_RID = "_id";
    public static final String KEY_BTN = "button";


    private static final String TAG = "PillDbAdapter";
    private DatabaseHelper DatabaseHelper;
    private SQLiteDatabase Db;

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_RID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, "
                    + KEY_DOSE + " text not null, "
                    + KEY_INSTRUCTIONS + " text not null, "
                    + KEY_BTN + " text, "
                    + KEY_TIME + " text not null);";

    private final Context vCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
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

    public PillDbAdapter(Context ctx)
    {
        this.vCtx=ctx;
    }

    public PillDbAdapter open() throws SQLException //create database
    {
        DatabaseHelper = new DatabaseHelper(vCtx);
        Db = DatabaseHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DatabaseHelper.close();
    } // Closing the Database

    public long createPill(String name, String dose, String instructions, String DateTime)
    {
        ContentValues initValues = new ContentValues(); //ContentValues used to define values for the columns in the db row that you insert
        initValues.put(KEY_NAME, name);
        initValues.put(KEY_DOSE, dose);
        initValues.put(KEY_INSTRUCTIONS, instructions);
        initValues.put(KEY_TIME, DateTime);

        return Db.insert(DATABASE_TABLE, null, initValues);
    }

    public boolean deletePill(long r_Id) //accepts rowID parameter to delete a pill reminder
    {
        return Db.delete(DATABASE_TABLE, KEY_RID + "=" + r_Id, null) > 0;
    }

    public Cursor fetchAllPills() // uses query() to find all pill reminder in the system //uses cursor object to retrieve values from the result set sent by query()

    {
        return Db.query(DATABASE_TABLE, new String[] {KEY_RID, KEY_NAME, KEY_DOSE, KEY_INSTRUCTIONS, KEY_TIME}, null, null, null, null, null);
    }

    public Cursor fetchPills(long r_Id) throws SQLException //gets rowID to fetch pill
    {
        Cursor vcursr = Db.query(true, DATABASE_TABLE, new String[] {KEY_RID,
                        KEY_NAME, KEY_DOSE, KEY_INSTRUCTIONS, KEY_TIME}, KEY_RID + "=" + r_Id, null,
                null, null, null, null);
        if (vcursr != null) {
            vcursr.moveToFirst(); // move to first method on cursor tell the curosr to go to the first record in the result because the cursor object is not always in the initial part.
            //this method is called only if cursor is not null.
        }
        return vcursr;
    }

    public boolean updatePill(long r_Id, String name, String dose, String instructions, String DateTime)
    {
        ContentValues arg = new ContentValues(); // ContentValues() used to store updated values in db
        arg.put(KEY_NAME, name);
        arg.put(KEY_DOSE, dose);
        arg.put(KEY_INSTRUCTIONS, instructions);
        arg.put(KEY_TIME, DateTime);

        return Db.update(DATABASE_TABLE, arg, KEY_RID + "=" + r_Id, null) > 0; //updating with new information
    }

    public long InsertRecord(String take, String snooze, String skip) {

        long d = 0;
        ContentValues values = new ContentValues();

        values.put(KEY_BTN, take);
        values.put(KEY_BTN, snooze);
        values.put(KEY_BTN, skip);

        try {
            d = Db.insert(DATABASE_TABLE, null, values);
        } catch (Exception e) {

        }
        return d;

    }

}
