package com.crm.feign;

import com.crm.dto.external.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// All the requests are processed through api-gateway
@FeignClient("api-gateway")
public interface Proxy {
    //All the methods you want to use

    //Mapping defined in Customer Controller
    @GetMapping("api/customers")
    ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles();

    @GetMapping("api/support")
    ResponseEntity<List<SupportTicketDTO>> retrieveAllSupportTickets();

    @GetMapping("api/marketing")
    ResponseEntity<List<CampaignDTO>> getAllCampaigns();

    @GetMapping("api/sales-opportunity")
    ResponseEntity<List<SalesOpportunityResponseDTO>> retrieveAllSalesOpportunities();

    @PostMapping("api/notifications/send")
    NotificationDTO sendNotificatonDummy(NotificationDTO notificationDTO);
}
