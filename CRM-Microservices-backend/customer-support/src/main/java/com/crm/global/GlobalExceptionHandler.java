package com.crm.global;

import com.crm.dto.ErrorResponseDTO;
import com.crm.dto.ValidationErrorResponseDTO;
import com.crm.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Global exception handler for the Customer Support Module.
 * Handles various exceptions and returns appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
     * Handles NoSuchElementException and returns a 404 Not Found error response.
     *
     * @param ex         The NoSuchElementException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 404 status.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(NoSuchElementException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handles InvalidSupportTicketException and returns a 400 Bad Request error response.
     *
     * @param ex         The InvalidSupportTicketException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(InvalidSupportTicketException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidSupportTicket(InvalidSupportTicketException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles AgentAssignmentException and returns a 400 Bad Request error response.
     *
     * @param ex         The AgentAssignmentException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(AgentAssignmentException.class)
    public ResponseEntity<ErrorResponseDTO> handleAgentAssignmentException(AgentAssignmentException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.CONFLICT.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    /**
     * Handles UnknownErrorOccurredException and returns a 400 Bad Request error response.
     *
     * @param ex         The UnknownErrorOccurredException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(UnknownErrorOccurredException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnknownError(UnknownErrorOccurredException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * Handles MethodArgumentNotValidException and returns a 400 Bad Request error response.
     *
     * @param ex         The MethodArgumentNotValidException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errorMessage = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(e -> errorMessage.add(e.getDefaultMessage()));

        ValidationErrorResponseDTO errorResponse = ValidationErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .messages(errorMessage)
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}