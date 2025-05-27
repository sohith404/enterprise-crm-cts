package com.crm.dto;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class ErrorResponseDTO {

	private String code;
	private LocalDateTime timeStamp;
	private String path;
	private String message;
	@Override
	public String toString() {
		return "ErrorResponseDTO [code=" + code + ", timeStamp=" + timeStamp + ", path=" + path + ", message=" + message
				+ "]";
	}
	
}
