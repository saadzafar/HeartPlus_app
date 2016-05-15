package com.sgh.swinburne.heartplus.pillreminder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by CharlesPK3 on 4/3/15.
 * This class represents each pill created by the user and contains the medication's name in
 * a string and a list of all of the pill's alarm objects. The pill's id is used to access the pill in the
 * database.
 */
public class Pill {
    private String pillName;
    private String dose;
    private String instruction;
    private long pillId;
    private List<Alarm> alarms = new LinkedList<Alarm>();

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }


    /**
     * @param alarm allows a new alarm sto be added to a preexisting alarm
     */
    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
        Collections.sort(alarms);
    }

    public long getPillId() {
        return pillId;
    }

    public void setPillId(long pillID) {
        this.pillId = pillID;
    }
}
