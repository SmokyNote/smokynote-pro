package com.smokynote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.internal.view.menu.MenuBuilder;
import com.actionbarsherlock.internal.view.menu.MenuPopupHelper;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cocosw.undobar.UndoBarController;
import com.smokynote.inject.Injector;
import com.smokynote.note.Note;
import com.smokynote.note.NotesRepository;
import com.smokynote.playback.PlaybackActivity;
import com.smokynote.timer.TimePickerActivity;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesListFragment extends SherlockListFragment implements NotesListAdapter.NoteEnableListener, NotesListAdapter.NoteMenuListener {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.LIST");

    private static final int ACTIVITY_TIME_PICKER = 20;

    private static final String EXTRA_NOTE_ID = "targetNoteId";

    public static final String ACTION_SCHEDULE = "smokynote.note.schedule";
    public static final String ACTION_ENABLE = "smokynote.note.enable";
    public static final String ACTION_DELETE = "smokynote.note.delete";
    public static final String ACTION_RESTORE = "smokynote.note.restore";

    @Inject
    /* private */ NotesRepository notesRepository;
    @Inject
    /* private */ LocalBroadcastManager broadcastManager;

    private NotesListAdapter notesListAdapter;
    private MenuPopupHelper menuPopupHelper;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleNotesChanged();
        }
    };

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
        registerListeners();
    }

    @Override
    public void onPause() {
        super.onPause();

        notesListAdapter.stop();
        unregisterListeners();
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

        broadcastManager.sendBroadcast(new Intent(ACTION_ENABLE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_TIME_PICKER:
                handleTimePickerResult(resultCode, data);
                break;
            default:
                LOG.warn("Unhandled activity result, requestCode = {}", requestCode);
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Listeners

    private void registerListeners() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SCHEDULE);
        filter.addAction(ACTION_DELETE);
        filter.addAction(ACTION_RESTORE);

        broadcastManager.registerReceiver(broadcastReceiver, filter);
    }

    private void unregisterListeners() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void handleNotesChanged() {
        notesListAdapter.setNotes(notesRepository.getAll());
    }

    // Playback

    private void openPlayback(Note note) {
        Intent intent = new Intent(getActivity().getApplicationContext(), PlaybackActivity.class);
        intent.putExtra(PlaybackActivity.EXTRA_NOTE, note);
        startActivity(intent);
    }

    // Scheduling

    private void promptNewSchedule(Note note) {
        final Intent intent = new Intent(getActivity().getApplicationContext(), TimePickerActivity.class);
        // Pass Note id as extra, so we can use it later.
        final Bundle extras = new Bundle();
        extras.putInt(EXTRA_NOTE_ID, note.getId());
        intent.putExtra(TimePickerActivity.EXTRA_TRANSFER, extras);
        startActivityForResult(intent, ACTIVITY_TIME_PICKER);
    }

    private void handleTimePickerResult(int resultCode, Intent data) {
        switch (resultCode) {
            case TimePickerActivity.RESULT_TIME_SELECTED:
                handleTimePickerSuccess(data);
                break;
            default:
                LOG.warn("Unhandled TimePicker result code {}", resultCode);
        }
    }

    private void handleTimePickerSuccess(Intent data) {
        final Bundle extras = data.getBundleExtra(TimePickerActivity.EXTRA_TRANSFER);
        // TODO: can it be null for any reason?
        final Integer noteId = extras.getInt(EXTRA_NOTE_ID);
        final DateTime schedule = (DateTime) data.getSerializableExtra(TimePickerActivity.EXTRA_TIME);

        LOG.debug("Got picked time, saving new Note");

        scheduleNote(noteId, schedule);
    }

    private void scheduleNote(Integer noteId, DateTime schedule) {
        final Note note = notesRepository.getById(noteId);
        note.setSchedule(schedule);
        notesRepository.save(note);

        broadcastManager.sendBroadcast(new Intent(ACTION_SCHEDULE));
    }

    // Deleting

    /**
     * Mark Note for deletion, we'll periodically collect marked Notes and delete them.
     *
     * @param noteId Note id to delete
     */
    private void deleteNote(Integer noteId) {
        notesRepository.markDeleted(noteId, true);

        broadcastManager.sendBroadcast(new Intent(ACTION_DELETE));

        showDeleteUndoBar(noteId);
    }

    private void showDeleteUndoBar(Integer noteId) {
        final Bundle undoToken = new Bundle();
        undoToken.putInt(EXTRA_NOTE_ID, noteId);
        new UndoBarController.UndoBar(getActivity())
                .message("Note deleted")
                .token(undoToken)
                .listener(new UndoBarController.UndoListener() {
                    @Override
                    public void onUndo(Parcelable token) {
                        if (token != null) {
                            undoNoteDeletion(token);
                        }
                    }
                })
                .show();
    }

    private void undoNoteDeletion(@Nonnull Parcelable token) {
        Bundle bundle = (Bundle) token;
        Integer noteId = bundle.getInt(EXTRA_NOTE_ID);
        notesRepository.markDeleted(noteId, false);

        broadcastManager.sendBroadcast(new Intent(ACTION_RESTORE));
    }

    // Context menu

    @Override
    public void onMenuRequested(Note note, View anchor) {
        LOG.debug("Requested context menu for {}", note);
        final Context context = anchor.getContext();
        final MenuBuilder menuBuilder = createMenuBuilder(note, context);
        menuPopupHelper = new MenuPopupHelper(context, menuBuilder, anchor);
        menuPopupHelper.show();
    }

    private MenuBuilder createMenuBuilder(Note note, Context context) {
        final MenuBuilder menuBuilder = new MenuBuilder(context);
        new MenuInflater(context).inflate(R.menu.note_context_menu, menuBuilder);
        menuBuilder.setCallback(new NoteMenuCallback(note));
        return menuBuilder;
    }

    private class NoteMenuCallback implements MenuBuilder.Callback {

        private final Note targetNote;

        private NoteMenuCallback(Note targetNote) {
            this.targetNote = targetNote;
        }

        @Override
        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.note_action_play:
                    LOG.info("Selected Play action for Note {}", targetNote);
                    openPlayback(targetNote);
                    return true;
                case R.id.note_action_schedule:
                    LOG.info("Selected Schedule action for Note {}", targetNote);
                    promptNewSchedule(targetNote);
                    return true;
                case R.id.note_action_delete:
                    LOG.info("Selected Delete action for Note {}", targetNote);
                    deleteNote(targetNote.getId());
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
    }
}
