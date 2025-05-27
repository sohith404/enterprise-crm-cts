package com.crm.global;

import com.crm.dto.ErrorResponseDTO;
import com.crm.dto.ValidationErrorResponseDTO;
import com.crm.enums.SalesStage;
import com.crm.exception.*;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Global exception handler for the Sales Automation Module.
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

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponseDTO> handleFeignException(FeignException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(ex.status()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(extractMessage(ex.getMessage()))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles CustomerNotFoundException and returns a 404 Not Found error response.
     *
     * @param ex         The CustomerNotFoundException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 404 status.
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(CustomerNotFoundException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles IllegalArgumentException and returns a 400 Bad Request error response.
     *
     * @param ex         The IllegalArgumentException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message("Bad Value for Sales Stage, EXPECTING: "+ Arrays.toString(SalesStage.values()) +", BUT RECEIVED: "+ Arrays.stream(webRequest.getDescription(false).split("/")).toList().getLast())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles InvalidSalesDetailsException and returns a 400 Bad Request error response.
     *
     * @param ex         The InvalidSalesDetailsException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(InvalidSalesDetailsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(InvalidSalesDetailsException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InvalidDateTimeException and returns a 400 Bad Request error response.
     *
     * @param ex         The InvalidDateTimeException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(InvalidDateTimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestInvalidDateTime(InvalidDateTimeException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InvalidOpportunityIdException and returns a 400 Bad Request error response.
     *
     * @param ex         The InvalidOpportunityIdException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(InvalidOpportunityIdException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestInvalidOpportunityId(InvalidOpportunityIdException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UnknownErrorOccurredException and returns a 500 Internal Server Error response.
     *
     * @param ex         The UnknownErrorOccurredException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 500 status.
     */
    @ExceptionHandler(UnknownErrorOccurredException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestInvalidOpportunityId(UnknownErrorOccurredException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles InvalidCronExpressionException and returns a 400 Bad Request error response.
     *
     * @param ex         The InvalidCronExpressionException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(InvalidCronExpressionException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCronExpression(InvalidCronExpressionException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a 400 Bad Request error response with validation error messages.
     *
     * @param ex      The MethodArgumentNotValidException.
     * @param request The WebRequest.
     * @return ResponseEntity containing ValidationErrorResponseDTO with 400 status and validation error messages.
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

    private static String extractMessage(String response) {
        String regex = "\"message\":\"(.*?)\"";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            // Return the first captured group, which contains the message
            return matcher.group(1);
        }
        return "Message not found";
    }

}