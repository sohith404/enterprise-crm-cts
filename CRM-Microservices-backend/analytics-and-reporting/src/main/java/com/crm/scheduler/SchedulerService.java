package com.crm.scheduler;


import com.crm.dto.external.NotificationDTO;

import java.util.List;

/**
 * Service for scheduling and sending notifications.
 */
public interface SchedulerService {
    /**
     * Sends scheduled notifications.
     *
     * @return A list of NotificationDTO objects representing the notifications that were sent.
     */
    public List<NotificationDTO> sendNotifications();
}