package com.smokynote.activity;

import android.os.Bundle;
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

        final int width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        if (width > 0) {
            // This sets the window size, while working around the IllegalStateException thrown by ActionBarView
            getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
