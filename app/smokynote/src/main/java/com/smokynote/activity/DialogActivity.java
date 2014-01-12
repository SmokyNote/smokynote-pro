package com.smokynote.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.smokynote.R;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public abstract class DialogActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int width = getDialogWidth();
        if (width > 0) {
            // This sets the window size, while working around the IllegalStateException thrown by ActionBarView
            getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    protected int getDialogWidth() {
        return getResources().getDimensionPixelSize(R.dimen.dialog_width);
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

    protected abstract void initActionBar();

    protected abstract void bindButtonBar();

    /**
     * On small screens we prefer to show action buttons as part of actionbar
     * rather then to acquire extra space for additional button bar.
     *
     * @return true if (bottom) button bar should be replaced with actionbar action buttons
     */
    protected boolean shouldUseActionsInsteadOfButtonBar() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return convertPxToDip(metrics.heightPixels) < 350;
    }

    private int convertPxToDip(int pixel){
        float scale = getResources().getDisplayMetrics().density;
        return (int) ((pixel / scale) + 0.5f);
    }

    private void hideButtonBar() {
        findViewById(R.id.buttonPanel).setVisibility(View.GONE);
    }
}
