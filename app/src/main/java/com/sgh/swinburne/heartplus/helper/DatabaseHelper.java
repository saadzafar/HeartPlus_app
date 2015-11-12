package com.sgh.swinburne.heartplus.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sgh.swinburne.heartplus.PillEducation.Interaction;
import com.sgh.swinburne.heartplus.PillEducation.Pill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saad on 11/12/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PillEducation";

    private static final String TABLE_PILL = "pill";
    private static final String TABLE_INTERACTION = "interaction";
    private static final String TABLE_PILL_INTERACTION = "pill_interaction";

    private static final String KEY_ID = "id";
    private static final String KEY_PILLNAME = "pill_name";
    private static final String KEY_PILLDESCRIPTION = "pill_description";
    private static final String KEY_INTERACTIONNAME = "interaction_name";
    private static final String KEY_INTERACTIONDESCRIPTION = "interaction_description";

    private static final String KEY_PILL_ID = "pill_id";
    private static final String KEY_INTERACTION_ID = "interaction_id";

    private static final String CREATE_TABLE_PILL = "CREATE TABLE " + TABLE_PILL + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PILLNAME + " TEXT," + KEY_PILLDESCRIPTION + " TEXT" + ")";

    private static final String CREATE_TABLE_INTERACTION = "CREATE TABLE " + TABLE_INTERACTION + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_INTERACTIONNAME + " TEXT," + KEY_INTERACTIONDESCRIPTION + " TEXT" + ")";

    private static final String CREATE_TABLE_PILL_INTERACTION = "CREATE TABLE " + TABLE_PILL_INTERACTION + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PILL_ID + " INTEGER," + KEY_INTERACTION_ID + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PILL);
        db.execSQL(CREATE_TABLE_INTERACTION);
        db.execSQL(CREATE_TABLE_PILL_INTERACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PILL_INTERACTION);

        onCreate(db);
    }

    //CREATING AN INTERACTION
    public long createInteraction(Interaction interaction, long[] pill_ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INTERACTIONNAME, interaction.getInteraction_name());
        values.put(KEY_INTERACTIONDESCRIPTION, interaction.getInteraction_description());

        long interaction_id = db.insert(TABLE_INTERACTION, null, values);

        for (long pill_id : pill_ids) {
            createPillInteraction(interaction_id, pill_id);
        }

        return interaction_id;
    }

    //FETCHING AN INTERACTION
    public Interaction getInteraction(long interaction_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PILL + " WHERE "
                + KEY_ID + " = " + interaction_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        Interaction interaction = new Interaction();
        interaction.setInteration_id(c.getInt(c.getColumnIndex(KEY_ID)));
        interaction.setInteraction_name(c.getString(c.getColumnIndex(KEY_INTERACTIONNAME)));
        interaction.setInteraction_description(c.getString(c.getColumnIndex(KEY_INTERACTIONDESCRIPTION)));

        return interaction;
    }

    //LIST ALL INTERACTIONS
    public List<Interaction> getAllInteractions() {
        List<Interaction> interactions = new ArrayList<Interaction>();
        String selectQuery = "SELECT * FROM " + TABLE_INTERACTION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Interaction interaction = new Interaction();
                interaction.setInteration_id(c.getInt((c.getColumnIndex(KEY_ID))));
                interaction.setInteraction_name((c.getString(c.getColumnIndex(KEY_INTERACTIONNAME))));
                interaction.setInteraction_description(c.getString(c.getColumnIndex(KEY_INTERACTIONDESCRIPTION)));

                interactions.add(interaction);
            } while (c.moveToNext());
        }

        return interactions;

    }

    //LISTING INTERACTIONS BY PILL
    public List<Interaction> getAllInteractionsByPill(String pill_name) {
        List<Interaction> interactions = new ArrayList<Interaction>();

        String selectQuery = "SELECT * FROM " + TABLE_INTERACTION + " i, " + TABLE_PILL + " p, "
                + TABLE_PILL_INTERACTION + " tpi WHERE p." + KEY_PILLNAME + " = '" + pill_name
                + "'" + "AND p." + KEY_ID + " = " + "tpi." + KEY_PILL_ID
                + " AND i." + KEY_ID + " = " + "tpi." + KEY_INTERACTION_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Interaction interaction = new Interaction();
                interaction.setInteration_id(c.getInt(c.getColumnIndex(KEY_ID)));
                interaction.setInteraction_name(c.getString(c.getColumnIndex(KEY_INTERACTIONNAME)));
                interaction.setInteraction_description(c.getString(c.getColumnIndex(KEY_INTERACTIONDESCRIPTION)));

                interactions.add(interaction);
            } while (c.moveToNext());
        }

        return interactions;
    }


    //UPDATING AN INTERACTION
    public int updateInteraction(Interaction interaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INTERACTIONNAME, interaction.getInteraction_name());
        values.put(KEY_INTERACTIONDESCRIPTION, interaction.getInteraction_description());

        return db.update(TABLE_INTERACTION, values, KEY_ID + " = ?", new String[]{String.valueOf(interaction.getInteration_id())});
    }

    //DELETING AN INTERACTION
    public void deleteInteraction(long interaction_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INTERACTION, KEY_ID + " = ?", new String[]{String.valueOf(interaction_id)});
    }

    //CREATE A PILL
    public long createPill(Pill pill) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PILLNAME, pill.getPill_name());
        values.put(KEY_PILLDESCRIPTION, pill.getPill_description());

        long pill_id = db.insert(TABLE_PILL, null, values);

        return pill_id;
    }

    //FETCH ALL PILLS
    public List<Pill> getAllPills() {
        List<Pill> pills = new ArrayList<Pill>();
        String selectQuery = "SELECT * FROM " + TABLE_PILL;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Pill p = new Pill();
                p.setPill_id(c.getInt(c.getColumnIndex(KEY_ID)));
                p.setPill_name(c.getString(c.getColumnIndex(KEY_INTERACTIONNAME)));
                p.setPill_description(c.getString(c.getColumnIndex(KEY_INTERACTIONDESCRIPTION)));

                pills.add(p);
            } while (c.moveToNext());
        }
        return pills;
    }


    //UPDATING PILLS
    public int updatePills(Pill pill) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PILLNAME, pill.getPill_name());

        return db.update(TABLE_PILL, values, KEY_ID + " = ?", new String[]{String.valueOf(pill.getPill_id())});
    }

    //DELETING PILLS AND INTERACTIONS BASED ON THEM
    public void deletePill(Pill pill) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PILL, KEY_ID + " = ?", new String[]{String.valueOf(pill.getPill_id())});
    }


    //ASSIGN AN INTERACTION TO A PILL
    public long createPillInteraction(long pill_id, long interaction_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PILL_ID, pill_id);
        values.put(KEY_INTERACTION_ID, interaction_id);

        long id = db.insert(TABLE_PILL_INTERACTION, null, values);

        return id;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public int getPillCount() {
        String countQuery = "SELECT * FROM " + TABLE_PILL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }


}



