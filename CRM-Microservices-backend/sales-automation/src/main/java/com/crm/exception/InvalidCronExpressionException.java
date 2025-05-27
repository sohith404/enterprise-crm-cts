package com.crm.exception;

public class InvalidCronExpressionException extends RuntimeException {
    public InvalidCronExpressionException(String message) {
        super(message);
    }
}
