package com.crm.dto;
import com.crm.enums.Type;
import lombok.Data;
@Data
public class NotificationDTO {
	private Type type;
	private String status;
	private String subject;
	private String body;
	private String emailFor;
	private String trackingUrl;
	
}
