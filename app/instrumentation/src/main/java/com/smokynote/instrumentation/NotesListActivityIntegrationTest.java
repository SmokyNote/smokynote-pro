package com.smokynote.instrumentation;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.ListView;
import com.smokynote.Application;
import com.smokynote.NotesListActivity;
import com.smokynote.R;
import com.smokynote.note.NoteBuilder;
import com.smokynote.note.NotesRepository;
import com.smokynote.note.Note;
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
