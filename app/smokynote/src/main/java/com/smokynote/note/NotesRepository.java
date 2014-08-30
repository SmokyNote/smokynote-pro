package com.smokynote.note;

import java.util.List;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public interface NotesRepository {

    List<Note> getAll();

    void add(Note note);

    /**
     * Remove all notes from repository.
     */
    void clear();
}
