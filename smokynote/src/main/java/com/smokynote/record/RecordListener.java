package com.smokynote.record;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public interface RecordListener {

    /**
     * Recording can't be started because of unavailability of device storage.
     */
    void onStorageUnavailable();

    /**
     * Media recorder preparing failed.
     */
    void onRecorderPrepareFailed();
}
