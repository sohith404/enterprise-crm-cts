package com.crm.service;

import com.crm.dto.NotificationDTO;

public interface NotificationService {
    NotificationDTO sendNotification(NotificationDTO notificationDTO);
}
