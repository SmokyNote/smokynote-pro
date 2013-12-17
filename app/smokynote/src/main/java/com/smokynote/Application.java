package com.smokynote;

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

    @Override
    public void onCreate() {
        super.onCreate();

        LOG.debug("Application created");
    }
}
