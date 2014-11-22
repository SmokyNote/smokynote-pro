package com.smokynote.instrumentation;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cocosw.undobar.UndoBarController;
import com.robotium.solo.Solo;
import com.smokynote.Application;
import com.smokynote.NotesListActivity;
import com.smokynote.R;
import com.smokynote.note.Note;
import com.smokynote.note.NoteBuilder;
import com.smokynote.note.NotesRepository;
import com.smokynote.playback.PlaybackActivity;
import com.smokynote.record.RecordActivity;

import org.joda.time.DateTime;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration test for {@link NotesListActivity}.
 *
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesListActivityIntegrationTest extends ActivityInstrumentationTestCase2<NotesListActivity> {

    Application application;

    @SuppressWarnings("deprecation")
    public NotesListActivityIntegrationTest() {
        // Android 2.1 requires pkg to be presented.
        super("com.smokynote", NotesListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        application = ((Application) getInstrumentation().getTargetContext().getApplicationContext());
        application.onCreate();
    }

    @SmallTest
    public void testRecordButtonPresented() {
        assertNotNull("Expected 'Record note' button to be presented", getActivity().findViewById(R.id.add_note));
    }

    @SmallTest
    public void testLaunchRecording() throws Throwable {
        final Instrumentation.ActivityMonitor monitor =
                getInstrumentation().addMonitor(RecordActivity.class.getName(), null, true);

        final Button addNoteButton = (Button) getActivity().findViewById(R.id.add_note);
        runTestOnUiThread(new Runnable() {
            public void run() {
                addNoteButton.performClick();
            }
        });

        assertTrue(getInstrumentation().checkMonitorHit(monitor, 1));
    }

    @SmallTest
    public void testNotesPresented() {
        // Prepare repository before #getActivity() first call
        prepareNotesRepository();

        assertThat("Expected exactly one Note to be listed", listView().getCount(), equalTo(1));
    }

    @SmallTest
    public void testNoteEnableButtonPersistState() throws Throwable {
        // Prepare repository before #getActivity() first call
        NotesRepository repository = prepareNotesRepository();
        assertTrue("Note must be enabled before test", repository.getAll().get(0).isEnabled());

        final Button toggleStatusButton = (Button) getActivity().findViewById(R.id.toggle_alarm);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleStatusButton.performClick();
            }
        });

        assertFalse("Note must be disabled", repository.getAll().get(0).isEnabled());
    }

    @SmallTest
    public void testPlaybackCalled() {
        // Prepare repository before #getActivity() first call
        prepareNotesRepository();
        final Solo solo = new Solo(getInstrumentation(), getActivity());

        // Open context menu
        final View contextMenuButton = getActivity().findViewById(R.id.note_actions);
        solo.clickOnView(contextMenuButton);

        // Choose playback
        solo.clickOnText(getActivity().getResources().getString(R.string.note_action_play));

        solo.assertCurrentActivity("PlaybackActivity should be launched", PlaybackActivity.class);
    }

    @MediumTest
    public void testUndoDeletion() throws Throwable {
        // Prepare repository before #getActivity() first call
        NotesRepository repository = prepareNotesRepository();
        assertEquals("Must be exactly one Note before test", 1, repository.getAll().size());
        final Solo solo = new Solo(getInstrumentation(), getActivity());

        // Step 1: delete
        final View contextMenuButton = getActivity().findViewById(R.id.note_actions);
        solo.clickOnView(contextMenuButton);

        solo.clickOnText(getActivity().getResources().getString(R.string.note_action_delete));
        solo.waitForView(UndoBarController.class, 1, 500);

        assertEquals("Note expected to be marked for deletion", 1, repository.getMarkedForDeletion().size());

        // Step 2: undo
        solo.clickOnText(getActivity().getResources().getString(R.string.undo));
        waitForNoView(solo, UndoBarController.class, 500);

        assertEquals("Note expected to be restored", 0, repository.getMarkedForDeletion().size());
    }

    private void waitForNoView(Solo solo, Class<? extends View> aClass, int timeout) {
        for (int i = 0; i < timeout; i += 50) {
            if (solo.getCurrentViews(aClass).isEmpty()) {
                return;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // pass
            }
        }
    }

    private NotesRepository prepareNotesRepository() {
        final NotesRepository notesRepository = application.getDependency(NotesRepository.class);
        notesRepository.clear();
        notesRepository.add(createTestNote());
        return notesRepository;
    }

    private Note createTestNote() {
        return new NoteBuilder()
                .withId(1)
                .withSchedule(DateTime.now().plusSeconds(60))
                .withFilename("")
                .withDescription("")
                .withEnabled(true)
                .build();
    }

    private ListView listView() {
        return (ListView) getActivity().findViewById(android.R.id.list);
    }
}
