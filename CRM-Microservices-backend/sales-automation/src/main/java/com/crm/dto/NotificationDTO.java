package com.crm.dto;

import com.crm.enums.Type;
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
    private Type type;
    private String status;
    private String subject;
    private String body;
    private String emailFor;
}
