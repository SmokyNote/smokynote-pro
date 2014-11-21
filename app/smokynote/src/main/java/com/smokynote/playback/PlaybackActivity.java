package com.smokynote.playback;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

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
public class PlaybackActivity extends DialogActivity {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.PLAY");

    public static final String EXTRA_NOTE = "targetNote";

    private PlaybackFragment playbackFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!validateExtras()) {
            LOG.error("No Note passed to play. Force finish.");
            finish();
            return;
        }

        setContentView(R.layout.dialog_activity);

        initPlaybackFragment();
    }

    private boolean validateExtras() {
        return getIntent().hasExtra(EXTRA_NOTE);
    }

    private void initPlaybackFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        playbackFragment = (PlaybackFragment) fragmentManager.findFragmentById(R.id.dialog_fragment);
        if (playbackFragment == null) {
            doInitPlaybackFragment();
        }
    }

    private void doInitPlaybackFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        playbackFragment = new PlaybackFragment();
        playbackFragment.setArguments(constructFragmentArguments());
        fragmentManager.beginTransaction()
                .replace(R.id.dialog_fragment, playbackFragment)
                .commit();
        playbackFragment.startPlayback();
    }

    private Bundle constructFragmentArguments() {
        final Bundle arguments = new Bundle();
        // Constant values may change some day, so we repackage arguments.
        arguments.putSerializable(PlaybackFragment.EXTRA_NOTE, getIntent().getSerializableExtra(EXTRA_NOTE));
        return arguments;
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
