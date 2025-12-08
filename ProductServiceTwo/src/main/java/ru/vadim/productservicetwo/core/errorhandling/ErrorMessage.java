package ru.vadim.productservicetwo.core.errorhandling;

import java.util.Date;

public class ErrorMessage {
    private final Date timestamp;
    private final String message;

    public ErrorMessage(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
