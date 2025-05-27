package com.crm.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new ResourceNotFoundException with the specified detail message.
	 *
	 * @param message the detail message
	 */
	public ResourceNotFoundException(String message) {
		super(message);
	}
}