package com.smokynote.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.smokynote.OnAlarmReceiver;
import com.smokynote.note.Note;
import com.smokynote.note.NotesRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
@Singleton
public class AlarmScheduler {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.SCHEDULER");

    private final NotesRepository repository;
    private final AlarmManager alarmManager;

    @Inject
    public AlarmScheduler(NotesRepository repository, AlarmManager alarmManager) {
        this.repository = repository;
        this.alarmManager = alarmManager;
    }

    /**
     * Schedule alarm for nearest note.
     *
     * @return true if alarm was set otherwise false
     */
    public boolean schedule(Context context) {
        Note nextNote = repository.findNext();
        if (nextNote == null) {
            removeAlarm();
            return false;
        } else {
            scheduleAlarm(nextNote, context);
            return true;
        }
    }

    private void removeAlarm() {
        // Alarms can't be removed. Instead, we perform additional check when alarm triggers.
    }

    private void scheduleAlarm(Note note, Context context) {
        LOG.info("Scheduling alarm for {}.", note);

        Intent intent = new Intent(context, OnAlarmReceiver.class);
        // Do not pass Integer or other wrappers to Intent extras
        intent.putExtra(OnAlarmReceiver.EXTRA_NOTE_ID, note.getId().intValue());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, getRtcTime(note), pendingIntent);
    }

    private long getRtcTime(Note note) {
        // Start 2 seconds later than actual time.
        return note.getSchedule().getMillis() + 2000;
    }
}
