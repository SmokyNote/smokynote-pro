package com.smokynote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.smokynote.alarm.timed.ScreenUnlockActivity;
import com.smokynote.alarm.timed.TimedAlarmActivity;
import com.smokynote.inject.Injector;
import com.smokynote.note.Note;
import com.smokynote.note.NotesRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class OnAlarmReceiver extends BroadcastReceiver {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.ALARM");

    public static final String EXTRA_NOTE_ID = "noteId";

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG.info("Received alarm broadcast.");

        int noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1);
        if (noteId == -1) {
            // Wuuuuuut
            LOG.warn("Note id not passed");
        } else {
            NotesRepository notesRepository = ((Injector) context.getApplicationContext()).resolve(NotesRepository.class);
            Note note = notesRepository.getById(noteId);
            if (note == null) {
                LOG.warn("Note with id {} not found!", noteId);
            } else if (!note.isEnabled()) {
                LOG.warn("Note #{} disabled, abort alarm", noteId);
            } else {
                launchAlarmActivity(context, noteId);
            }
        }
    }

    private void launchAlarmActivity(Context context, int targetNoteId) {
        Intent unlockIntent = new Intent(context, ScreenUnlockActivity.class);
        unlockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        unlockIntent.putExtra(ScreenUnlockActivity.EXTRA_TARGET_CLASS, TimedAlarmActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(TimedAlarmActivity.EXTRA_NOTE_ID, targetNoteId);
        unlockIntent.putExtra(ScreenUnlockActivity.EXTRA_TARGET_CLASS_EXTRAS, extras);

        context.startActivity(unlockIntent);
    }
}
