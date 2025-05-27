package com.crm.exception;

/**
 * Exception thrown when a notification is not found or when notification data is invalid.
 * This exception extends {@link RuntimeException} and is used to indicate that a notification
 * operation could not be completed due to missing or invalid notification data.
 */
public class NotificationNotFoundException extends RuntimeException {
    /**
     * Constructs a new NotificationNotFoundException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public NotificationNotFoundException(String message) {
        super(message);
    }
}