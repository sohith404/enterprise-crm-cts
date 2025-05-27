package com.crm.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ValidationErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<String> messages;
    private String path;

}
