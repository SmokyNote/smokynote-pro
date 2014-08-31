package com.smokynote.note.impl;

import com.smokynote.note.NotesRepository;
import com.smokynote.note.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesRepositoryInMemoryImpl implements NotesRepository {

    private List<Note> notes = new ArrayList<Note>();

    @Override
    public List<Note> getAll() {
        return Collections.unmodifiableList(notes);
    }

    @Override
    public void add(Note note) {
        notes.add(note);
    }

    @Override
    public void save(Note note) {
        notes.remove(note);
        notes.add(note);
    }

    @Override
    public void clear() {
        notes.clear();
    }
}
