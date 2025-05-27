package com.crm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for Customer Profile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDTO {

    /**
     * The unique ID of the customer.
     */
    private Long customerID;

    /**
     * The name of the customer.
     */
    @NotBlank(message = "Name cannot be blank")
    private String name;

    /**
     * The email ID of the customer.
     */
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    private String emailId;

    /**
     * The phone number of the customer.
     */
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    /**
     * The purchase history of the customer.
     */
    private List<String> purchaseHistory;

    /**
     * The Segmentation Data of the customer.
     */
    private Map<String, String> segmentationData;


}