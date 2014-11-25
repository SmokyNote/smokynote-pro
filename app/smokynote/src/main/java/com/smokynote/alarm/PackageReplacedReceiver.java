package com.smokynote.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smokynote.inject.Injector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class PackageReplacedReceiver extends BroadcastReceiver {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.RCV.REPLACE");

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getDataString().contains("com.smokynote")) {
            LOG.info("Package replaced, setting alarms.");

            AlarmScheduler scheduler = ((Injector) context.getApplicationContext()).resolve(AlarmScheduler.class);
            scheduler.schedule();
        }
    }
}
