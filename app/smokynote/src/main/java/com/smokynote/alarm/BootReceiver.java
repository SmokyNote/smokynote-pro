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
public class BootReceiver extends BroadcastReceiver {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.RCV.BOOT");

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG.info("Device boot completed, schedule alarm.");

        AlarmScheduler scheduler = ((Injector) context.getApplicationContext()).resolve(AlarmScheduler.class);
        scheduler.schedule();
    }
}
