package com.smokynote;

import com.smokynote.dagger.SysModule;
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
public class Application extends android.app.Application {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE");

    /**
     * Single threaded scheduled executor for simple background sequential tasks.
     * Avoid <code>new Thread</code> constructions if possible.
     */
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private ObjectGraph objectGraph;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        initDependencyGraph();

        LOG.debug("Application created");
    }

    private void initDependencyGraph() {
        LOG.info("Initializing ObjectGraph");

        databaseHelper = new DatabaseHelper(this);

        objectGraph = ObjectGraph.create(createSysModule());
        objectGraph.validate();
    }

    private SysModule createSysModule() {
        return new SysModule(scheduledExecutorService, databaseHelper);
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
