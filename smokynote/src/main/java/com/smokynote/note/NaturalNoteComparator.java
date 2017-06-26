package com.smokynote.note;

import org.joda.time.DateTime;

import java.util.Comparator;

/**
 * Sort Notes in a natural way.
 * Future-scheduled Notes are sorted ascending, Note that will trigger Alarm first, goes first.
 * Outdated Notes are sorted descending.
 *
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NaturalNoteComparator implements Comparator<Note> {

    private final DateTime now = DateTime.now();

    @Override
    public int compare(Note lhs, Note rhs) {
        if (isFutureAlarm(lhs)) {
            if (isFutureAlarm(rhs)) {
                return lhs.getSchedule().compareTo(rhs.getSchedule());
            } else {
                return -1;
            }
        } else {
            if (isFutureAlarm(lhs)) {
                return 1;
            } else {
                return rhs.getSchedule().compareTo(lhs.getSchedule());
            }
        }
    }

    private boolean isFutureAlarm(Note note) {
        return note.getSchedule().isAfter(now);
    }
}
