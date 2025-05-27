package com.crm.exception;

public class InvalidSalesDetailsException extends RuntimeException {
    public InvalidSalesDetailsException(String message) {
        super(message);
    }
}
