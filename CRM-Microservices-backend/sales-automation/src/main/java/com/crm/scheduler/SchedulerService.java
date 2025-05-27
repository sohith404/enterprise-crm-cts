package com.crm.scheduler;

import com.crm.dto.NotificationDTO;

import java.util.List;

/**
 * Service for scheduling and sending notifications.
 */
public interface SchedulerService {
    /**
     * Sends Follow-Up Reminders.
     *
     * @return A list of NotificationDTO objects representing the emails that were sent.
     */
    List<NotificationDTO> sendFollowUpReminder();

    /**
     * Sends Closing notifications.
     *
     * @return A list of NotificationDTO objects representing the emails that were sent.
     */
    List<NotificationDTO> sendClosingNotification();
}
