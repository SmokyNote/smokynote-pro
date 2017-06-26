package com.smokynote.record;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class RecorderPrepareException extends RecordException {

    public RecorderPrepareException() {
    }

    public RecorderPrepareException(String detailMessage) {
        super(detailMessage);
    }

    public RecorderPrepareException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RecorderPrepareException(Throwable throwable) {
        super(throwable);
    }
}
