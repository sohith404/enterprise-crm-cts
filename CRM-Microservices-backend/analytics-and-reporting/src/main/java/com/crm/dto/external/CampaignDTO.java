package com.crm.dto.external;

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
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Type type;
    private int customerInteractions;
    private String trackingUrl;
}