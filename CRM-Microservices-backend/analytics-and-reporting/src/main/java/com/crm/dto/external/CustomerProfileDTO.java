package com.crm.dto.external;

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
    private String name;

    /**
     * The email ID of the customer.
     */
    private String emailId;

    /**
     * The phone number of the customer.
     */
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