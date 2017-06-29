package com.smokynote.timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.smokynote.R;
import com.smokynote.activity.DialogActivity;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class TimePickerActivity extends DialogActivity {

    public static final int RESULT_TIME_SELECTED = 10;

    public static final String EXTRA_TIME = "time";
    public static final String EXTRA_TRANSFER = "transfer";

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
        final Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setText(R.string.time_picker_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        final Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setText(R.string.time_picker_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (shouldUseActionsInsteadOfButtonBar()) {
            getSupportMenuInflater().inflate(R.menu.time_picker_activity_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit:
                submit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void submit() {
        final Intent data = new Intent();
        data.putExtra(EXTRA_TIME, timePickerFragment.getSelectedTime());
        if (getIntent().hasExtra(EXTRA_TRANSFER)) {
            data.putExtra(EXTRA_TRANSFER, getIntent().getBundleExtra(EXTRA_TRANSFER));
        }
        setResult(RESULT_TIME_SELECTED, data);
        finish();
    }

    @Override
    protected int getDialogWidth() {
        return getResources().getDimensionPixelSize(R.dimen.time_picker_dialog_width);
    }
}
