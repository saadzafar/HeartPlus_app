package com.sgh.swinburne.heartplus.pillreminder;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.text.method.DigitsKeyListener;

import com.sgh.swinburne.heartplus.R;

/**
 * Created by VivekShah.
 */
public class TaskPref extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.task_pref);

        // Set the time default to a numeric number only
        EditTextPreference Default_time = (EditTextPreference) findPreference(getString(R.string.pref_time_from_now_key));
        Default_time.getEditText().setKeyListener(DigitsKeyListener.getInstance());
    }
}
