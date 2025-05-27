package com.crm.api_gateway.global;



import com.crm.api_gateway.dto.ErrorResponseDTO;
import com.crm.api_gateway.exception.InvalidCredentialsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Global exception handler for handling various exceptions across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    /**
     * Handles validation exceptions for method arguments.
     *
     * @param ex the MethodArgumentNotValidException
     * @return a ResponseEntity containing the validation errors and a BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles constraint violation exceptions.
     *
     * @param ex the ConstraintViolationException
     * @return a ResponseEntity containing the constraint violation errors and a BAD_REQUEST status
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other exceptions.
     *
     * @param ex the Exception
     * @param webRequest the WebRequest
     * @return a ResponseEntity containing the exception message and an INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest webRequest) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest webRequest) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

}