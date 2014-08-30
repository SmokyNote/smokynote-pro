package com.smokynote.note;

import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Builder for {@link Note} entity.
 * <p/>
 * Ensures you have set all necessary non-null field on {@link #build()} call.
 *
 * @author Maksim Zakharov
 * @since 1.0
 */
@NotThreadSafe
public class NoteBuilder {

    private Integer id;

    private String description;
    private DateTime schedule;
    private boolean enabled;
    private String filename;

    public NoteBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public NoteBuilder withDescription(@Nonnull String description) {
        this.description = description;
        return this;
    }

    public NoteBuilder withSchedule(@Nonnull DateTime schedule) {
        this.schedule = schedule;
        return this;
    }

    public NoteBuilder withEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public NoteBuilder withFilename(@Nonnull String filename) {
        this.filename = filename;
        return this;
    }

    public Note build() {
        checkState();
        return doBuild();
    }

    private void checkState() {
        if (description == null) {
            throw new NoteConstructionException("Missing required field 'description'");
        }
        if (schedule == null) {
            throw new NoteConstructionException("Missing required field 'schedule'");
        }
        if (filename == null) {
            throw new NoteConstructionException("Missing required field 'filename'");
        }
    }

    private Note doBuild() {
        final Note note = new Note();
        if (id != null) {
            note.setId(id);
        }
        note.setDescription(description);
        note.setSchedule(schedule);
        note.setEnabled(enabled);
        note.setFilename(filename);
        return note;
    }

    public static class NoteConstructionException extends IllegalStateException {

        public NoteConstructionException(String detailMessage) {
            super(detailMessage);
        }
    }
}
