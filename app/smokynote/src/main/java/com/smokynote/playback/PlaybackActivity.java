package com.smokynote.playback;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.smokynote.R;
import com.smokynote.activity.DialogActivity;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class PlaybackActivity extends DialogActivity {

    private PlaybackFragment playbackFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_activity);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        playbackFragment = (PlaybackFragment) fragmentManager.findFragmentById(R.id.dialog_fragment);
        if (playbackFragment == null) {
            playbackFragment = new PlaybackFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.dialog_fragment, playbackFragment)
                    .commit();
            playbackFragment.startPlayback();
        }
    }

    @Override
    protected void bindButtonBar() {
        findViewById(R.id.submit).setVisibility(View.GONE);

        Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setText(R.string.playback_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (shouldUseActionsInsteadOfButtonBar()) {
            getSupportMenuInflater().inflate(R.menu.playback_activity_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                cancel();
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
        playbackFragment.stopPlayback();
        finish();
    }
}
