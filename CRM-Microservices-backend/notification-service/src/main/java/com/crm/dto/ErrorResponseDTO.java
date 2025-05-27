package com.crm.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponseDTO {
	private String code;
	private LocalDateTime timestamp;
	private String path;
	private String message;
}
