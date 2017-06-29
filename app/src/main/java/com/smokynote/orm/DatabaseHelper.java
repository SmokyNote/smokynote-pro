package com.smokynote.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.smokynote.note.Note;

import java.sql.SQLException;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "smokynote.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        // See http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_4.html#Config-Optimization
        // super(context, DB_NAME, null, DB_VERSION, R.raw.ormlite_config);
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Note.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Note.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
