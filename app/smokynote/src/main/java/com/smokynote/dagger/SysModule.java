package com.smokynote.dagger;

import com.smokynote.Application;
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

    public SysModule(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    @Provides
    @Singleton
    public ScheduledExecutorService provideExecutorService() {
        return executorService;
    }
}
