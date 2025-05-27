package com.crm.exception;

public class InvalidTicketIdException extends RuntimeException {
	
    public InvalidTicketIdException(String message) {
        super(message);
    }
}
