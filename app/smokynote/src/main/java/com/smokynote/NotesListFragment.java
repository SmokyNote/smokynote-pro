package com.smokynote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.internal.view.menu.MenuBuilder;
import com.actionbarsherlock.internal.view.menu.MenuPopupHelper;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.smokynote.inject.Injector;
import com.smokynote.note.Note;
import com.smokynote.note.NotesRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesListFragment extends SherlockListFragment implements NotesListAdapter.NoteEnableListener, NotesListAdapter.NoteMenuListener {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.LIST");

    @Inject
    /* private */ NotesRepository notesRepository;

    private NotesListAdapter notesListAdapter;
    private MenuPopupHelper menuPopupHelper;

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
        notesListAdapter.setNoteEnableListener(this);
        notesListAdapter.setNoteMenuListener(this);

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
    public void onDestroyView() {
        if (menuPopupHelper != null) {
            menuPopupHelper.dismiss();
        }

        super.onDestroyView();
    }

    @Override
    public void onEnable(Note note, boolean enabled) {
        note.setEnabled(enabled);
        notesRepository.save(note);
        // TODO: broadcast, so we can (un)schedule alarm.
    }

    @Override
    public void onMenuRequested(final Note note, View anchor) {
        final MenuBuilder menuBuilder = new MenuBuilder(anchor.getContext());
        new MenuInflater(anchor.getContext()).inflate(R.menu.note_context_menu, menuBuilder);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.note_action_play:
                        LOG.info("Selected Play action for Note {}", note);
                        return true;
                    case R.id.note_action_schedule:
                        LOG.info("Selected Schedule action for Note {}", note);
                        return true;
                    case R.id.note_action_delete:
                        LOG.info("Selected Delete action for Note {}", note);
                        return true;
                    default:
                        LOG.warn("Unhandled menu item with id = {}", item.getItemId());
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {
                // pass
            }
        });
        menuPopupHelper = new MenuPopupHelper(anchor.getContext(), menuBuilder, anchor);
        menuPopupHelper.show();
    }
}
