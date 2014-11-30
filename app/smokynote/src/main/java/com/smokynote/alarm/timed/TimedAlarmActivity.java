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
import com.smokynote.alarm.AlarmScheduler;
import com.smokynote.inject.Injector;
import com.smokynote.note.Note;
import com.smokynote.note.NotesRepository;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

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

    public static final String EXTRA_NOTE_ID = "targetNoteId";

    @Inject
    AlarmScheduler alarmScheduler;

    @Inject
    NotesRepository notesRepository;

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

        ((Injector) getApplication()).inject(this);

        initFragment();
    }

    private boolean validateExtras() {
        return getIntent().hasExtra(EXTRA_NOTE_ID);
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
        arguments.putSerializable(TimedAlarmFragment.EXTRA_NOTE, getTargetNote());
        return arguments;
    }

    private Note getTargetNote() {
        int targetNoteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);
        return notesRepository.getById(targetNoteId);
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

    @Override
    public void onBackPressed() {
        snooze();
    }

    private void dismiss() {
        final Note note = getTargetNote();
        LOG.info("Dismiss note {}", note);

        note.setEnabled(false);
        notesRepository.save(note);
        alarmScheduler.schedule(this);

        finish();
    }

    private void snooze() {
        final Note note = getTargetNote();
        LOG.info("Snooze note {}", note);

        note.setEnabled(true);
        note.setSchedule(DateTime.now().plusMinutes(5));
        notesRepository.save(note);
        alarmScheduler.schedule(this);

        finish();
        // TODO: toast
    }
}
