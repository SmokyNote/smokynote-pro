package com.smokynote.instrumentation;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import com.smokynote.Application;
import com.smokynote.NotesListActivity;
import com.smokynote.R;
import com.smokynote.note.NotesRepository;
import com.smokynote.orm.Note;
import org.joda.time.DateTime;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Integration test for {@link NotesListActivity}.
 *
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesListActivityIntegrationTest extends ActivityInstrumentationTestCase2<NotesListActivity> {

    Application application;

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

    public void testRecordButtonPresented() {
        assertNotNull("Expected 'Record note' button to be presented", getActivity().findViewById(R.id.add_note));
    }

    public void testNotesPresented() {
        updateNotesRepository();

        getInstrumentation().waitForIdleSync();

        assertThat("Expected exactly one Note to be listed", listView().getChildCount(), equalTo(1));
    }

    private void updateNotesRepository() {
        final NotesRepository notesRepository = application.getDependency(NotesRepository.class);
        notesRepository.clear();
        notesRepository.add(createTestNote());
    }

    private Note createTestNote() {
        final Note note = new Note();
        note.setId(1);
        note.setSchedule(DateTime.now());
        return note;
    }

    private ListView listView() {
        return (ListView) getActivity().findViewById(android.R.id.list);
    }
}
