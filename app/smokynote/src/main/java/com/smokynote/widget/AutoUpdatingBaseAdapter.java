package com.smokynote.widget;

import android.os.Handler;
import android.text.format.DateUtils;
import android.widget.BaseAdapter;

/**
 * {@link android.widget.BaseAdapter} extension that invalidates it's content every second.
 * Useful for ListViews that contains frequently changing (text) content, e.g., countdowns.
 *
 * @author Maksim Zakharov
 * @since 1.0
 */
public abstract class AutoUpdatingBaseAdapter extends BaseAdapter {

    private final Handler handler = new Handler();
    private final Runnable updateTimerTask = new UpdateTimerTask();

    public void start() {
        handler.post(updateTimerTask);
    }

    public void stop() {
        handler.removeCallbacks(updateTimerTask);
    }

    private class UpdateTimerTask implements Runnable {

        @Override
        public void run() {
            // Invalidating data set causes whole view to redraw and aborts long taps.
            // Have no idea how to "fix" it.
            notifyDataSetChanged();
            handler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
        }
    }
}
