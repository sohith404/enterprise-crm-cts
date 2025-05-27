package com.crm.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private String code;
    private String message;
    private String path;
}
