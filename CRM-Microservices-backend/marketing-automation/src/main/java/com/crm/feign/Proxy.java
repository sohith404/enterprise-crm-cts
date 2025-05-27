package com.crm.feign;
import com.crm.dto.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("api-gateway")
public interface Proxy {

    @PostMapping("/api/notifications/send")
    NotificationDTO sendNotification(NotificationDTO notificationDTO);
}
