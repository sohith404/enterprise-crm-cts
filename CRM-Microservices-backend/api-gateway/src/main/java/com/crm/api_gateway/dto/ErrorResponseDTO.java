package com.crm.api_gateway.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for error responses.
 * This class is used to encapsulate error details in a structured format.
 */
@Data
@Builder
public class ErrorResponseDTO {
    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * The error code representing the type of error.
     */
    private String code;

    /**
     * The error message providing details about the error.
     */
    private String message;

    /**
     * The path of the request that caused the error.
     */
    private String path;
}