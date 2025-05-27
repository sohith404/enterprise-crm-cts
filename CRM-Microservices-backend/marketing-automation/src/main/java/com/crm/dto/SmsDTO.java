package com.crm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SmsDTO {

	@NotNull(message="The message should be minimum 10 characters and maximum 100 charcater")
	private String message;
	private String trackingUrl;
	private String emailFor;
	@Override
	public String toString() {
		return "SmsDTO [message=" + message + ", trackingUrl=" + trackingUrl + ", emailFor=" + emailFor + "]";
	}
	
	
	
}
  