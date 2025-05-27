package com.crm.scheduler;

import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;

/**
 * Service for dynamically managing scheduled tasks.
 */
public interface DynamicSchedulerService {

    /**
     * Updates the cron expression for the scheduled task.
     *
     * @param scheduleConfigRequestDTO The DTO containing the new cron expression.
     * @return The updated ScheduleConfigResponseDTO.
     */
    ScheduleConfigResponseDTO updateCronExpression(ScheduleConfigRequestDTO scheduleConfigRequestDTO);

    /**
     * Restarts the scheduled task with the provided cron expression.
     *
     * @param cronExpression The new cron expression to use.
     */
    void restartScheduledTask(String cronExpression);
}