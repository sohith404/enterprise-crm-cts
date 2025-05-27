package com.crm.global;

import com.crm.dto.ErrorResponseDTO;
import com.crm.dto.ValidationErrorResponseDTO;
import com.crm.exception.InvalidCronExpressionException;
import com.crm.exception.InvalidDataRecievedException;
import com.crm.exception.NoReportsAvailableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * Global exception handler for the Report Automation Module.
 * Handles various exceptions and returns appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     *
     * @param ex         The InvalidDetailsException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 400 status.
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(JsonProcessingException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Handles NoSuchElementException and returns a 404 Not Found error response.
     *
     * @param ex         The NoSuchElementException.
     * @param webRequest The WebRequest.
     * @return ResponseEntity containing ErrorResponseDTO with 404 status.
     */

    @ExceptionHandler(IllegalArgumentException.class)
    //* Handles InvalidSalesDetailsException and returns a 400 Bad Request error response.
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message("Wrong value provided for 'Type', expected : [SALES, SUPPORT, MARKETING, CUSTOMER], provided : " + Arrays.stream(webRequest.getDescription(false).split("/")).toList().getLast())
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
    @ExceptionHandler(InvalidDataRecievedException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestInvalidDateTime(InvalidDataRecievedException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

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
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDTO> NoSuchElementException(NoSuchElementException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoReportsAvailableException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoReportsAvailableException(NoReportsAvailableException ex, WebRequest webRequest) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .timestamp(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

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