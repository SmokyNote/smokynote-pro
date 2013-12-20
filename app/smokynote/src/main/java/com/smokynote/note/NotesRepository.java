package com.smokynote.note;

import com.smokynote.orm.Note;

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
