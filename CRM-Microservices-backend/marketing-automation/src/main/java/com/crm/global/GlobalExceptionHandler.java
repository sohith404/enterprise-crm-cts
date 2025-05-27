package com.crm.global;
import java.time.LocalDateTime; 
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.crm.dto.ErrorResponseDTO;
import com.crm.exception.CampaignNotFoundException;
import com.crm.exception.CampaignNotificationFailedException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/**
 * Global exception handler for the CRM application.
 * This class handles various exceptions that may occur during request processing,
 * providing consistent and informative error responses in the form of ErrorResponseDTO.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions thrown when request data fails validation.
     * Returns a ResponseEntity with a BAD_REQUEST status and an ErrorResponseDTO containing
     * details about the validation errors.
     *
     * @param ex      The MethodArgumentNotValidException.
     * @param request The WebRequest, used to retrieve the request URI.
     * @return A ResponseEntity containing an ErrorResponseDTO with validation error details.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
    	Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        String errorMessage;
        if (errors.containsKey("startDate")) {
            errorMessage = errors.get("startDate"); // Use specific error message
        } else if (errors.containsKey("endDate")) {
            errorMessage = errors.get("endDate");
        } else {
            errorMessage = "Validation failed. Check 'errors' for details."; // Default message
        }
    	
    	ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .timeStamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .message(errorMessage+""+errors.toString())
            .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }
    /**
     * Handles data integrity violation exceptions, such as duplicate entry errors.
     * Returns a ResponseEntity with a CONFLICT status and an ErrorResponseDTO containing
     * details about the data integrity violation.
     *
     * @param ex      The DataIntegrityViolationException.
     * @param request The WebRequest, used to retrieve the request URI.
     * @return A ResponseEntity containing an ErrorResponseDTO with data integrity violation details.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
            .code(String.valueOf(HttpStatus.CONFLICT.value()))
            .timeStamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .message(ex.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    /**
     * Handles date/time parsing exceptions, such as invalid date formats.
     * Returns a ResponseEntity with a BAD_REQUEST status and an ErrorResponseDTO containing
     * details about the date/time parsing error.
     *
     * @param ex      The DateTimeParseException.
     * @param request The WebRequest, used to retrieve the request URI.
     * @return A ResponseEntity containing an ErrorResponseDTO with date/time parsing error details.
     */
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleDateTimeParseException(DateTimeParseException ex, WebRequest request) {
    	ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .timeStamp(LocalDateTime.now())
            .path(request.getDescription(false))
            .message(ex.getMessage())
            .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }
    /**
     * Handles campaign not found exceptions.
     * Returns a ResponseEntity with a BAD_REQUEST status and an ErrorResponseDTO indicating
     * that the campaign was not found.
     *
     * @param ex      The CampaignNotFoundException.
     * @param request The WebRequest, used to retrieve the request URI.
     * @return A ResponseEntity containing an ErrorResponseDTO indicating campaign not found.
     */
    @ExceptionHandler(CampaignNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleCampaignNotFoundException(CampaignNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .timeStamp(LocalDateTime.now())
            .path(request.getDescription(false))
            .message(ex.getMessage())
            .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler(CampaignNotificationFailedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ErrorResponseDTO> handleCampaignNotCreated(CampaignNotificationFailedException e,WebRequest request){
    	ErrorResponseDTO errorResponse=ErrorResponseDTO.builder()
    			.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
    			.timeStamp(LocalDateTime.now())
    			.path(request.getDescription(false))
    			.message("Campaign Not Created")
    			.build();
    	return ResponseEntity.badRequest().body(errorResponse);
    }
}