package com.smokynote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;
import com.smokynote.inject.Injector;
import com.smokynote.note.Note;
import com.smokynote.note.NotesRepository;

import javax.inject.Inject;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesListFragment extends SherlockListFragment implements NotesListAdapter.NoteEnableListener {

    @Inject
    /* private */ NotesRepository notesRepository;

    private NotesListAdapter notesListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((Injector) getActivity().getApplication()).inject(this);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notesListAdapter = new NotesListAdapter(getActivity());
        setListAdapter(notesListAdapter);
        notesListAdapter.setListener(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        notesListAdapter.setNotes(notesRepository.getAll());
        notesListAdapter.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        notesListAdapter.stop();
    }

    @Override
    public void onEnable(Note note, boolean enabled) {
        note.setEnabled(enabled);
        notesRepository.save(note);
        // TODO: broadcast, so we can (un)schedule alarm.
    }
}
