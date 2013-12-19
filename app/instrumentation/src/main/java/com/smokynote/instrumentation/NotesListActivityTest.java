package com.smokynote.instrumentation;

import android.test.ActivityInstrumentationTestCase2;
import com.smokynote.*;
import com.smokynote.R;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesListActivityTest extends ActivityInstrumentationTestCase2<NotesListActivity> {

    public NotesListActivityTest() {
        // Android 2.1 requires pkg to be presented.
        super("com.smokynote", NotesListActivity.class);
    }

    public void testRecordButtonPresented() {
        assertNotNull("Expected 'Record note' button to be presented", getActivity().findViewById(R.id.add_note));
    }
}
