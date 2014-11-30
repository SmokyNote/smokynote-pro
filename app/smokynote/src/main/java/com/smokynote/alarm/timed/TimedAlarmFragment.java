package com.smokynote.alarm.timed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.smokynote.R;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class TimedAlarmFragment extends SherlockFragment {

    public static final String EXTRA_NOTE = "targetNote";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playback_fragment, container, false);
    }

    public void startAlarm() {

    }
}
