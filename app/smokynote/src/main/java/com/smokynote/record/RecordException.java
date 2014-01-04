package com.smokynote.record;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public class RecordException extends Exception {

    public RecordException() {
    }

    public RecordException(String detailMessage) {
        super(detailMessage);
    }

    public RecordException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RecordException(Throwable throwable) {
        super(throwable);
    }
}
