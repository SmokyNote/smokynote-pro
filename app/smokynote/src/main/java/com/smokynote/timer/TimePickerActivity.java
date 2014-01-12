package com.smokynote.timer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.actionbarsherlock.app.ActionBar;
import com.smokynote.R;
import com.smokynote.activity.DialogActivity;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class TimePickerActivity extends DialogActivity {

    private TimePickerFragment timePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_activity);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        timePickerFragment = (TimePickerFragment) fragmentManager.findFragmentById(R.id.dialog_fragment);
        if (timePickerFragment == null) {
            timePickerFragment = new TimePickerFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.dialog_fragment, timePickerFragment)
                    .commit();
        }
    }

    @Override
    protected void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
    }

    @Override
    protected void bindButtonBar() {

    }

    @Override
    protected int getDialogWidth() {
        return getResources().getDimensionPixelSize(R.dimen.time_picker_dialog_width);
    }
}
