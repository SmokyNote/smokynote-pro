package com.smokynote.record;

/**
 * Could not save recorded file.
 *
 * @author Maksim Zakharov
 * @since 1.0
 */
public class RecordSaveException extends RecordException {

    public RecordSaveException() {
    }

    public RecordSaveException(String detailMessage) {
        super(detailMessage);
    }

    public RecordSaveException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RecordSaveException(Throwable throwable) {
        super(throwable);
    }
}
