package com.crm.global;

import com.crm.dto.ErrorResponseDTO;
import com.crm.exception.NotificationNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the CRM Module.
 * Handles various exceptions and returns appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler { // Corrected class name

    /**
     * Handles general exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllException(Exception ex, WebRequest webRequest) {
        ErrorResponseDTO errorResponse = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), webRequest);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles JSON processing exceptions.
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponseDTO> handleJsonProcessingException(JsonProcessingException ex, WebRequest webRequest) {
        ErrorResponseDTO errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid JSON format: " + ex.getMessage(), webRequest);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles notification not found exceptions.
     */
    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotificationNotFoundException(NotificationNotFoundException ex, WebRequest webRequest) {
        ErrorResponseDTO errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), webRequest);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation exceptions (e.g., @Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds the ErrorResponseDTO.
     */
    private ErrorResponseDTO buildErrorResponse(HttpStatus status, String message, WebRequest webRequest) {
        String path = webRequest != null ? webRequest.getDescription(false) : "unknown";
        if (path == null) {
            path = "unknown";
        }

        return ErrorResponseDTO.builder()
                .code(String.valueOf(status.value()))
                .timestamp(LocalDateTime.now())
                .path(path)
                .message(message)
                .build();
    }
}