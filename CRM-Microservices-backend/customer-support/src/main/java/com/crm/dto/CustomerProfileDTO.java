package com.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDTO {
    private Long customerID;
    private SupportTicketDTO supportTicketDTO;
    private String name;
    private String contactInfo;
    private List<String> purchaseHistory;
    private List<String> segmentationData;
}
