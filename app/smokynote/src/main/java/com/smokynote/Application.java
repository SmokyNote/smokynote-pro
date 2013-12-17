package com.smokynote;

import com.smokynote.dagger.SysModule;
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

    @Override
    public void onCreate() {
        super.onCreate();

        initDependencyGraph();

        LOG.debug("Application created");
    }

    private void initDependencyGraph() {
        LOG.info("Initializing ObjectGraph");

        objectGraph = ObjectGraph.create(createSysModule());
        objectGraph.validate();
    }

    private SysModule createSysModule() {
        return new SysModule(scheduledExecutorService);
    }
}
