package com.crm.exception;
/**
 * Exception thrown when an enum value is not found in the segmentation data.
 */
public class EnumValueNotFoundException extends RuntimeException {

    /**
     * Constructs a new EnumValueNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public EnumValueNotFoundException(String message) {
        super(message);
    }
}