package com.crm.exception;

public class NoReportsAvailableException extends RuntimeException {
    public NoReportsAvailableException(String message) {
        super(message);
    }
}