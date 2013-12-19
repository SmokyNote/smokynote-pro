package com.smokynote.note.impl;

import com.smokynote.note.NotesRepository;
import com.smokynote.orm.Note;

import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesRepositoryImpl implements NotesRepository {

    @Override
    public List<Note> getAll() {
        return Collections.emptyList();
    }
}
