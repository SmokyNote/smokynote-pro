package com.smokynote.alarm;

import android.app.AlarmManager;

import com.smokynote.note.NotesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
@Singleton
public class AlarmScheduler {

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
        return false;
    }
}
