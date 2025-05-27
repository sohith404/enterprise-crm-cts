package com.crm.feign;

import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// All the requests are processed through api-gateway
@FeignClient("api-gateway")
public interface Proxy {
    //All the methods you want to use
    @GetMapping("/api/customers/{customerId}")
    ResponseEntity<CustomerProfileDTO> getCustomerById(@PathVariable long customerId);


    @PostMapping("api/notifications/send")
    NotificationDTO sendNotificaton(NotificationDTO notificationDTO);
}
