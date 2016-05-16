package com.sgh.swinburne.heartplus.pillreminder;

import java.util.Comparator;

/**
 * Created by faizan on 4/24/2016.
 */
public class PillComparator implements Comparator<Pill> {

    @Override
    public int compare(Pill pill1, Pill pill2) {

        String firstName = pill1.getPillName();
        String secondName = pill2.getPillName();
        return firstName.compareTo(secondName);
    }
}
