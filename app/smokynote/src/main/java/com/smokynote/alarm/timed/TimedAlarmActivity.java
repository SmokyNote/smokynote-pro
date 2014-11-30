package com.smokynote.alarm.timed;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.smokynote.R;
import com.smokynote.activity.DialogActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class TimedAlarmActivity extends DialogActivity {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.ALARM");

    private static final int WINDOW_FLAGS = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

    public static final String EXTRA_NOTE = "targetNote";

    private TimedAlarmFragment timedAlarmFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!validateExtras()) {
            LOG.error("No Note passed to start alarm. Force finish.");
            finish();
            return;
        }

        LOG.info("Initializing Alarm activity");

        getWindow().addFlags(WINDOW_FLAGS);

        setContentView(R.layout.dialog_activity);

        initFragment();
    }

    private boolean validateExtras() {
        return getIntent().hasExtra(EXTRA_NOTE);
    }

    private void initFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        timedAlarmFragment = (TimedAlarmFragment) fragmentManager.findFragmentById(R.id.dialog_fragment);
        if (timedAlarmFragment == null) {
            doInitFragment();
        }
    }

    private void doInitFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        timedAlarmFragment = new TimedAlarmFragment();
        timedAlarmFragment.setArguments(constructFragmentArguments());
        fragmentManager.beginTransaction()
                .replace(R.id.dialog_fragment, timedAlarmFragment)
                .commit();

        timedAlarmFragment.startAlarm();
    }

    private Bundle constructFragmentArguments() {
        final Bundle arguments = new Bundle();
        // Constant values may change some day, so we repackage arguments.
        arguments.putSerializable(TimedAlarmFragment.EXTRA_NOTE, getIntent().getSerializableExtra(EXTRA_NOTE));
        return arguments;
    }

    @Override
    protected void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
    }

    @Override
    protected void bindButtonBar() {
        final Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setText(R.string.timed_alarm_dismiss);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setText(R.string.timed_alarm_snooze);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snooze();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (shouldUseActionsInsteadOfButtonBar()) {
            getSupportMenuInflater().inflate(R.menu.timed_alarm_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dismiss:
                dismiss();
                return true;
            case R.id.snooze:
                snooze();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dismiss() {

    }

    private void snooze() {

    }
}
