package com.sgh.swinburne.heartplus.PillEducation;

/**
 * Created by Saad on 11/12/2015.
 */
public class Pill {
    int id;
    String pill_name;
    String pill_description;

    public Pill() {

    }

    public Pill(String pill_name, String pill_description) {
        this.pill_name = pill_name;
        this.pill_description = pill_description;
    }

    public Pill(int id, String pill_name, String pill_description) {
        this.id = id;
        this.pill_name = pill_name;
        this.pill_description = pill_description;
    }

    public void setPill_id(int pill_id) {
        this.id = pill_id;
    }

    public void setPill_name(String pill_name) {
        this.pill_name = pill_name;
    }

    public void setPill_description(String pill_description) {
        this.pill_description = pill_description;
    }

    public int getPill_id() {
        return this.id;
    }

    public String getPill_name() {
        return this.pill_name;
    }

    public String getPill_description() {
        return this.pill_description;
    }

}
