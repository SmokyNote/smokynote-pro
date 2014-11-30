package com.smokynote.alarm.timed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Transparent full-screen Activity used only to dismiss lock screen.
 * Once this job is done, simply launches target Activity.
 *
 * Why use this Activity when you can set needed flags from target Activity?
 * Well, in some cases Android will only turn screen on but show lock screen.
 * For me it happens with dialog-like (non-fullscreen) activities.
 *
 * @author Maksim Zakharov
 * @since 1.0
 */
public class ScreenUnlockActivity extends Activity {

    public static final String EXTRA_TARGET_CLASS = "targetClass";
    public static final String EXTRA_TARGET_CLASS_EXTRAS = "targetClassExtras";

    private static final int WINDOW_FLAGS = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WINDOW_FLAGS);

        super.onCreate(savedInstanceState);

        // Hack to start target Activity /later/.
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                startTargetActivity();
                finish();
            }
        });
    }

    private void startTargetActivity() {
        Class<?> targetClass = (Class<?>) getIntent().getSerializableExtra(EXTRA_TARGET_CLASS);
        Bundle extras = getIntent().getBundleExtra(EXTRA_TARGET_CLASS_EXTRAS);

        Intent intent = new Intent(this, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
