package com.smokynote.instrumentation;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.cocosw.undobar.UndoBarController;
import com.robotium.solo.Solo;
import com.smokynote.Application;
import com.smokynote.note.Note;
import com.smokynote.note.NoteBuilder;
import com.smokynote.playback.PlaybackActivity;

import org.joda.time.DateTime;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class PlaybackActivityIntegrationTest extends ActivityInstrumentationTestCase2<PlaybackActivity> {

    Application application;

    @SuppressWarnings("deprecation")
    public PlaybackActivityIntegrationTest() {
        super("com.smokynote", PlaybackActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        application = ((Application) getInstrumentation().getTargetContext().getApplicationContext());
        application.onCreate();
    }

    @SmallTest
    public void testLaunchWithNoExtras() {
        // TODO
    }

    @SmallTest
    public void testLaunchNoFile() {
        setActivityIntent(activityIntent(noteWithNotExistingFile()));

        final Solo solo = new Solo(getInstrumentation(), getActivity());
        boolean shown = solo.waitForView(UndoBarController.class, 1, 500);

        assertTrue("Error message expected to be shown", shown);
    }

    private Intent activityIntent(Note note) {
        final Intent intent = new Intent();
        intent.putExtra(PlaybackActivity.EXTRA_NOTE, note);
        return intent;
    }

    private Note noteWithNotExistingFile() {
        return new NoteBuilder()
                .withId(1)
                .withDescription("")
                .withSchedule(DateTime.now())
                .withFilename("/not/existing")
                .build();
    }
}
