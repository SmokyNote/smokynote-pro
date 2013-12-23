package com.smokynote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.smokynote.record.RecordActivity;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesListActivity extends SherlockFragmentActivity {

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
        startActivity(intent);
    }
}
