package com.smokynote.note.impl;

import com.smokynote.note.NotesRepository;
import com.smokynote.note.Note;

import org.joda.time.DateTime;

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
        final List<Note> availableNotes = new ArrayList<Note>(notes.size());
        for (Note note : notes) {
            if (!note.isDeleted()) {
                availableNotes.add(note);
            }
        }
        return Collections.unmodifiableList(availableNotes);
    }

    @Override
    public List<Note> getMarkedForDeletion() {
        final List<Note> deletedNotes = new ArrayList<Note>(notes.size());
        for (Note note : notes) {
            if (note.isDeleted()) {
                deletedNotes.add(note);
            }
        }
        return Collections.unmodifiableList(deletedNotes);
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
    public Note getById(Integer id) {
        for (Note note : notes) {
            if (note.getId().equals(id)) {
                return note;
            }
        }

        return null;
    }

    @Override
    public void markDeleted(Integer id) {
        Note note = getById(id);
        note.setDeletionTime(DateTime.now());
    }

    @Override
    public void clear() {
        notes.clear();
    }
}
