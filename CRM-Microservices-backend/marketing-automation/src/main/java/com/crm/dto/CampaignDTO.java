package com.crm.dto;
import com.crm.enums.Type;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class CampaignDTO {
    private Long campaignID;
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
    private String name;
    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be present or future")
    private LocalDate startDate;
    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "End date must be present or future")
    private LocalDate endDate;
    @NotNull(message = "Type cannot be null")
    private Type type;
    private int customerInteractions;
    private String trackingUrl;
    public boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true; // If one of the dates is null, validation will fail with @NotNull.
        }
        return !endDate.isBefore(startDate);
    }
}