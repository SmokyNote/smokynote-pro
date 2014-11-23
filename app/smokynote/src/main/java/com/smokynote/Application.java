package com.smokynote;

import com.smokynote.dagger.NotesModule;
import com.smokynote.dagger.SysModule;
import com.smokynote.inject.Injector;
import com.smokynote.orm.DatabaseHelper;
import dagger.ObjectGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class Application extends android.app.Application implements Injector {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE");

    /**
     * Single threaded scheduled executor for simple background sequential tasks.
     * Avoid <code>new Thread</code> constructions if possible.
     */
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private ObjectGraph objectGraph;
    private DatabaseHelper databaseHelper;

    @Override
    public void inject(Object target) {
        objectGraph.inject(target);
    }

    @Override
    public synchronized void onCreate() {
        super.onCreate();

        initDependencyGraph();

        LOG.debug("Application created");
    }

    private void initDependencyGraph() {
        if (objectGraph == null) {
            LOG.info("Initializing ObjectGraph");

            databaseHelper = new DatabaseHelper(this);

            objectGraph = ObjectGraph.create(createSysModule(), createNotesModule());
            objectGraph.validate();
        }
    }

    public <T> T getDependency(Class<T> dependencyClass) {
        return objectGraph.get(dependencyClass);
    }

    private SysModule createSysModule() {
        return new SysModule(this.getApplicationContext(), scheduledExecutorService, databaseHelper);
    }

    private NotesModule createNotesModule() {
        return new NotesModule();
    }

    @Override
    public void onTerminate() {
        LOG.info("Application terminating");

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onTerminate();
    }
}
