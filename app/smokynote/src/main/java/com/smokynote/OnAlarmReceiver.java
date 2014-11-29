package com.smokynote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class OnAlarmReceiver extends BroadcastReceiver {

    private static final Logger LOG = LoggerFactory.getLogger("SMOKYNOTE.ALARM");

    public static final String EXTRA_NOTE_ID = "noteId";

    @Override
    public void onReceive(Context context, Intent intent) {
        LOG.info("Received alarm broadcast.");

        // TODO: get alarm by id, endure it exists and in enabled state
    }
}
