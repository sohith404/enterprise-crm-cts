package com.crm.exception;

public class InvalidDateTimeException extends RuntimeException {
    public InvalidDateTimeException(String message) {
        super(message);
    }
}
