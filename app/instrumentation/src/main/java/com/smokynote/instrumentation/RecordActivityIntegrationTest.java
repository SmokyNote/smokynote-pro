package com.smokynote.instrumentation;

import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.smokynote.Application;
import com.smokynote.R;
import com.smokynote.record.RecordActivity;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class RecordActivityIntegrationTest extends ActivityInstrumentationTestCase2<RecordActivity> {

    Application application;

    @SuppressWarnings("deprecation")
    public RecordActivityIntegrationTest() {
        super("com.smokynote", RecordActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        application = ((Application) getInstrumentation().getTargetContext().getApplicationContext());
        application.onCreate();
    }

    public void testStorageUnmounted() {
        // TODO
    }

    @MediumTest
    public void testFragmentRetain() {
        final SherlockFragmentActivity activity = getActivity();
        final Fragment portraitFragment = activity.getSupportFragmentManager().findFragmentById(R.id.dialog_fragment);

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync();

        final Fragment landscapeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.dialog_fragment);
        assertTrue("Recording fragment must be retained", portraitFragment == landscapeFragment);
    }
}
