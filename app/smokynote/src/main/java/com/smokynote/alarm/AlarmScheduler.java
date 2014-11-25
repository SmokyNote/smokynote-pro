package com.smokynote.alarm;

import android.app.AlarmManager;

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
    public boolean schedule() {
        Note nextNote = repository.findNext();
        if (nextNote == null) {
            removeAlarm();
            return false;
        } else {
            scheduleAlarm(nextNote);
            return true;
        }
    }

    private void removeAlarm() {
        LOG.info("Unscheduled alarms.");
        // TODO
    }

    private void scheduleAlarm(Note note) {
        LOG.info("Scheduled alarm for {}.", note);
        // TODO
    }
}
