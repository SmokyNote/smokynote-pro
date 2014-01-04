package com.smokynote.record;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
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

    public static final int RESULT_STORAGE_UNAVAILABLE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.record_activity);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.record_fragment) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.record_fragment, new RecordFragment())
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initActionBar();

        if (shouldUseActionsInsteadOfButtonBar()) {
            hideButtonBar();
        } else {
            bindButtonBar();
        }
    }

    private void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
    }

    private void hideButtonBar() {
        findViewById(R.id.buttonPanel).setVisibility(View.GONE);
    }

    private void bindButtonBar() {
        final Button cancelButton = (Button) findViewById(R.id.cancel);
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
            getSupportMenuInflater().inflate(R.menu.record_activity_menu, menu);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        cancel();

        super.onBackPressed();
    }

    private void cancel() {
        finish();
    }

    /**
     * On small screens we prefer to show action buttons as part of actionbar
     * rather then to acquire extra space for additional button bar.
     *
     * @return true if (bottom) button bar should be replaced with actionbar action buttons
     */
    private boolean shouldUseActionsInsteadOfButtonBar() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return convertPxToDip(metrics.heightPixels) < 350;
    }

    private int convertPxToDip(int pixel){
        float scale = getResources().getDisplayMetrics().density;
        return (int) ((pixel / scale) + 0.5f);
    }

    @Override
    public void onStorageUnavailable() {
        LOG.warn("Storage unavailable");
        setResult(RESULT_STORAGE_UNAVAILABLE);
        finish();
    }
}
