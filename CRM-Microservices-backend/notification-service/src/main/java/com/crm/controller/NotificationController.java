package com.crm.controller;

import com.crm.dto.NotificationDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("api/notifications")
@Validated
public interface NotificationController {
  
    @PostMapping("send")
    public ResponseEntity<NotificationDTO> sendNotification(@Valid NotificationDTO notificationDTO);
}
