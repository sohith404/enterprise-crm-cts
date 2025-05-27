package com.crm.dto;

import com.crm.enums.Status;
import com.crm.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Data Transfer Object (DTO) representing a notification.
 * This DTO encapsulates the data required for sending notifications to customers or employees.
 * It includes information such as recipient IDs, notification type, status, subject, and body.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class NotificationDTO {
    /**
     * Employee ID to whom the notification should be sent.
     */
    private String employeeID;
    /**
     * Type of notification (e.g., EMAIL, SMS).
     * Must be provided and cannot be null.
     */
    @NotNull(message = "Type must be provided and should not be null")
    private Type type;
    /**
     * Status of the notification (e.g., SENT, FAILED).
     */
    private Status status;
    /**
     * Subject of the notification.
     */
    private String subject;
    /**
     * Body of the notification.
     * Must contain some information and cannot be blank.
     */
    @NotBlank(message = "Body must contain some information")
    private String body;
    /**
     * Specifies whether the notification is for a customer or an employee.
     * Must be provided and cannot be blank.
     */
    @NotBlank(message = "Need to mention whether customer or employee")
    private String emailFor;
}