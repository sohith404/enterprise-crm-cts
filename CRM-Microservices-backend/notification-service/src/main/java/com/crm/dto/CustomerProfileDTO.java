package com.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a customer profile.
 * This DTO encapsulates customer information such as ID, name, phone number, and email.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDTO {
    /**
     * The unique identifier for the customer.
     */
    private Long customerID;
    /**
     * The name of the customer.
     */
    private String name;
    /**
     * The phone number of the customer.
     */
    private String phoneNumber;
    /**
     * The email address of the customer.
     */
    private String emailId;
}