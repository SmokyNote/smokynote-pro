package com.smokynote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.smokynote.record.RecordActivity;
import com.smokynote.timer.TimePickerActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesListActivity extends SherlockFragmentActivity {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.NOTES");

    private static final int ACTIVITY_RECORD = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_list_activity);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initActionBar();
        initRecordButton();
    }

    private void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);
    }

    private void initRecordButton() {
        final Button recordButton = (Button) findViewById(R.id.add_note);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRecordActivity();
            }
        });
    }

    private void launchRecordActivity() {
        final Intent intent = new Intent(this, RecordActivity.class);
        startActivityForResult(intent, ACTIVITY_RECORD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_RECORD:
                handleRecordResult(resultCode, data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleRecordResult(int resultCode, Intent data) {
        switch (resultCode) {
            case RecordActivity.RESULT_STORAGE_UNAVAILABLE:
                handleStorageUnavailable();
                break;
            case RecordActivity.RESULT_RECORDER_PREPARE_FAILED:
                handleRecorderPrepareFailed();
                break;
            case RecordActivity.RESULT_SAVE_ERROR:
                handleRecordSaveError();
                break;
            case RecordActivity.RESULT_RECORDED:
                handleRecordSuccess(data);
                break;
        }
    }

    private void handleStorageUnavailable() {
        Toast.makeText(this, R.string.record_error_storage_unavailable, Toast.LENGTH_SHORT).show();
    }

    private void handleRecorderPrepareFailed() {
        Toast.makeText(this, R.string.record_error_recorder_prepare_failed, Toast.LENGTH_SHORT).show();
    }

    private void handleRecordSaveError() {
        Toast.makeText(this, R.string.record_error_save_failed, Toast.LENGTH_SHORT).show();
    }

    private void handleRecordSuccess(Intent data) {
        final String fileName = data.getStringExtra(RecordActivity.EXTRA_FILENAME);
        LOG.info("Recorded file {}", fileName);

        final Intent intent = new Intent(this, TimePickerActivity.class);
        startActivity(intent);
    }
}
