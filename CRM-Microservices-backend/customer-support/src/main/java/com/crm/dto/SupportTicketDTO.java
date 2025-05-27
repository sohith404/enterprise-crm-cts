package com.crm.dto;

import com.crm.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class SupportTicketDTO {
    private Long ticketID;
    @NotNull(message = "Customer ID can not be null!")
    @Min(value = 0, message = "Enter a valid Customer ID")
    private Long customerID;
    @NotNull(message = "Issue Description cannot be null!")
    @NotBlank(message = "Issue Description cannot be blank!")
    private String issueDescription;
    private Long assignedAgent;
    private Status status;
}
