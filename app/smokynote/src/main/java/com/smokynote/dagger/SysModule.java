package com.smokynote.dagger;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

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

    private final Context context;
    private final ScheduledExecutorService executorService;
    private final DatabaseHelper databaseHelper;

    public SysModule(Context context, ScheduledExecutorService executorService, DatabaseHelper databaseHelper) {
        this.context = context;
        this.executorService = executorService;
        this.databaseHelper = databaseHelper;
    }

    @Provides
    @Singleton
    public LocalBroadcastManager provideLocalBroadcastManager() {
        return LocalBroadcastManager.getInstance(context);
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
