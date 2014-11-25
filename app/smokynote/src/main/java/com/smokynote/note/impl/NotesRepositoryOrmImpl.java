package com.smokynote.note.impl;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.smokynote.note.Note;
import com.smokynote.note.NotesRepository;
import com.smokynote.orm.DatabaseHelper;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class NotesRepositoryOrmImpl implements NotesRepository {

    private final DatabaseHelper databaseHelper;

    @Inject
    public NotesRepositoryOrmImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Nullable
    @Override
    public Note findNext() {
        try {
            QueryBuilder<Note, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where()
                    .ge("schedule", DateTime.now()).and()
                    .eq("enabled", true).and()
                    .isNull("deletion_time");
            queryBuilder.orderBy("schedule", true);
            return queryBuilder.queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Note> getAll() {
        try {
            QueryBuilder<Note, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().isNull("deletion_time");
            queryBuilder.orderBy("schedule", false);
            return queryBuilder.query();
        } catch (SQLException e) {
            // RuntimeExceptionDao is not so runtime in some cases.
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Note> getMarkedForDeletion() {
        // TODO: remove copy/paste
        try {
            QueryBuilder<Note, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().isNotNull("deletion_time");
            queryBuilder.orderBy("schedule", false);
            return queryBuilder.query();
        } catch (SQLException e) {
            // RuntimeExceptionDao is not so runtime in some cases.
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Note note) {
        dao().create(note);
    }

    @Override
    public void save(Note note) {
        dao().update(note);
    }

    @Override
    public Note getById(Integer id) {
        return dao().queryForId(id);
    }

    @Override
    public void markDeleted(Integer id, boolean deleted) {
        final Note note = dao().queryForId(id);
        if (note != null) {
            markDeleted(note, deleted);
        }
    }

    private void markDeleted(Note note, boolean deleted) {
        if (deleted) {
            note.setDeletionTime(DateTime.now());
        } else {
            note.setDeletionTime(null);
        }

        dao().update(note);
    }

    @Override
    public void clear() {
        try {
            dao().deleteBuilder().delete();
        } catch (SQLException e) {
            // TODO:
        }
    }

    private RuntimeExceptionDao<Note, Integer> dao() {
        return databaseHelper.getRuntimeExceptionDao(Note.class);
    }
}
