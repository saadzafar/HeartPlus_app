package com.sgh.swinburne.heartplus.pillreminder;

import android.content.Context;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Faizan Elahi
 * Is used to retrieve pills and alarms by other classes.
 * Has access to and can read and write to the database.
 */
public class PillBox {
    private static List<Long> tempIds; // Ids of the alarms to be deleted or edited
    private static String tempName; // Ids of the alarms to be deleted or edited
    private static String tempDose;
    private static String tempInstruction;
    private PillDbAdapter db;

    public List<Long> getTempIds() {
        return Collections.unmodifiableList(tempIds);
    }

    public void setTempIds(List<Long> tempIds) {
        this.tempIds = tempIds;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getTempDose() {
        return tempDose;
    }

    public void setTempDose(String tempDose) {
        this.tempDose = tempDose;
    }

    public String getTempInstruction() {
        return tempInstruction;
    }

    public void setTempInstruction(String tempInstruction) {
        this.tempInstruction = tempInstruction;
    }

    public List<Pill> getPills(Context c) {
        db = new PillDbAdapter(c);
        List<Pill> allPills = db.getAllPills();
        db.close();
        return allPills;
    }

    public long addPill(Context c, Pill pill) {
        db = new PillDbAdapter(c);
        long pillId = db.createPill(pill);
        pill.setPillId(pillId);
        db.close();
        return pillId;
    }

    public Pill getPillByName(Context c, String pillName) {
        db = new PillDbAdapter(c);
        Pill wantedPill = db.getPillByName(pillName);
        db.close();
        return wantedPill;
    }

    public void addAlarm(Context c, Alarm alarm, Pill pill) {
        db = new PillDbAdapter(c);
        db.createAlarm(alarm, pill.getPillId());
        db.close();
    }

    public List<Alarm> getAlarms(Context c, int dayOfWeek) throws URISyntaxException {
        db = new PillDbAdapter(c);
        List<Alarm> daysAlarms = db.getAlarmsByDay(dayOfWeek);
        db.close();
        Collections.sort(daysAlarms);
        return daysAlarms;
    }

    public List<Alarm> getAlarmByPill(Context c, String pillName) throws URISyntaxException {
        db = new PillDbAdapter(c);
        List<Alarm> pillsAlarms = db.getAllAlarmsByPill(pillName);
        db.close();
        return pillsAlarms;
    }

    public boolean pillExist(Context c, String pillName) {
        db = new PillDbAdapter(c);
        for (Pill pill : this.getPills(c)) {
            if (pill.getPillName().equals(pillName))
                return true;
        }
        return false;
    }

    public void deletePill(Context c, String pillName) throws URISyntaxException {
        db = new PillDbAdapter(c);
        db.deletePill(pillName);
        db.close();
    }

    public void deleteAlarm(Context c, long alarmId) {
        db = new PillDbAdapter(c);
        db.deleteAlarm(alarmId);
        db.close();
    }

       /* public void addToHistory(Context c, History h){
            db = new PillDbAdapter(c);
            db.createHistory(h);
            db.close();
        }

        public List<History> getHistory (Context c){
            db = new PillDbAdapter(c);
            List<History> history = db.getHistory();
            db.close();
            return history;
        }*/

    public Alarm getAlarmById(Context c, long alarm_id) throws URISyntaxException {
        db = new PillDbAdapter(c);
        Alarm alarm = db.getAlarmById(alarm_id);
        db.close();
        return alarm;
    }

    public int getDayOfWeek(Context c, long alarm_id) throws URISyntaxException {
        db = new PillDbAdapter(c);
        int getDayOfWeek = db.getDayOfWeek(alarm_id);
        db.close();
        return getDayOfWeek;
    }
}
