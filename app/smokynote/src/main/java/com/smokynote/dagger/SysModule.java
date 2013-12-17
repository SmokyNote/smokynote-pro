package com.smokynote.dagger;

import com.smokynote.Application;
import com.smokynote.orm.DatabaseHelper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
@Module(library = true, injects = {Application.class})
public class SysModule {

    private final ScheduledExecutorService executorService;
    private final DatabaseHelper databaseHelper;

    public SysModule(ScheduledExecutorService executorService, DatabaseHelper databaseHelper) {
        this.executorService = executorService;
        this.databaseHelper = databaseHelper;
    }

    @Provides
    @Singleton
    public ScheduledExecutorService provideExecutorService() {
        return executorService;
    }

    @Provides
    @Singleton
    public DatabaseHelper provideDatabaseHelper() {
        return databaseHelper;
    }
}
