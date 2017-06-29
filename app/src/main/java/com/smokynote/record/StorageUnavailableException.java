package com.smokynote.record;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class StorageUnavailableException extends RecordException {

    public StorageUnavailableException() {
    }

    public StorageUnavailableException(String detailMessage) {
        super(detailMessage);
    }

    public StorageUnavailableException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public StorageUnavailableException(Throwable throwable) {
        super(throwable);
    }
}
