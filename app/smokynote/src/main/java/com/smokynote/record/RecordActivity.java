package com.smokynote.record;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class RecordActivity extends DialogActivity implements RecordListener {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.RECORD");

    public static final int RESULT_RECORDED = 10;
    public static final int RESULT_STORAGE_UNAVAILABLE = 100;
    public static final int RESULT_RECORDER_PREPARE_FAILED = 101;
    public static final int RESULT_SAVE_ERROR = 102;

    public static final String EXTRA_FILENAME = "fileName";

    private RecordFragment recordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.record_activity);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        recordFragment = (RecordFragment) fragmentManager.findFragmentById(R.id.record_fragment);
        if (recordFragment == null) {
            recordFragment = new RecordFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.record_fragment, recordFragment)
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
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        final Button submitButton = (Button) findViewById(R.id.submit);
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
            getSupportMenuInflater().inflate(R.menu.record_activity_menu, menu);
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

        super.onBackPressed();
    }

    private void cancel() {
        finish();
    }

    private void submit() {
        final String fileName;
        try {
            fileName = recordFragment.finishRecording();
        } catch (StorageUnavailableException e) {
            setResult(RESULT_STORAGE_UNAVAILABLE);
            finish();
            return;
        } catch (RecordSaveException e) {
            setResult(RESULT_SAVE_ERROR);
            finish();
            return;
        }

        final Intent intent = new Intent();
        intent.putExtra(EXTRA_FILENAME, fileName);
        setResult(RESULT_RECORDED, intent);

        finish();
    }

    @Override
    public void onStorageUnavailable() {
        LOG.warn("Storage unavailable");
        setResult(RESULT_STORAGE_UNAVAILABLE);
        finish();
    }

    @Override
    public void onRecorderPrepareFailed() {
        LOG.warn("Recorder prepare exception");
        setResult(RESULT_RECORDER_PREPARE_FAILED);
        finish();
    }
}
