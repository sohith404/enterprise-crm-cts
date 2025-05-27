package com.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class NotificationDTO {
    private String employeeID;
    private String status;
    private String subject;
    private String body;
    private String emailFor;
}
