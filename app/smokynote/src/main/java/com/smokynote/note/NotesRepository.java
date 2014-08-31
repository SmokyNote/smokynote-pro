package com.smokynote.note;

import java.util.List;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public interface NotesRepository {

    /**
     * Return all the Notes sorted by schedule, descending.
     *
     * @return all existing Notes
     */
    List<Note> getAll();

    void add(Note note);

    void save(Note note);

    Note getById(Integer id);

    /**
     * Remove all notes from repository.
     */
    void clear();
}
