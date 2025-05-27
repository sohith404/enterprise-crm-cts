package com.crm.scheduler;

import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.entities.ScheduleConfig;
import com.crm.exception.InvalidCronExpressionException;
import com.crm.mapper.SalesOpportunityMapper;
import com.crm.repository.ScheduleConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class DynamicSchedulerServiceImpl implements DynamicSchedulerService {

    private final ScheduleConfigRepository scheduleConfigRepository;
    private final SchedulerService schedulerService;
    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    private ScheduledFuture<?> reminderTask; // For the reminder task
    private ScheduledFuture<?> closingTask;  // For the closing notification task

    @Autowired
    public DynamicSchedulerServiceImpl(ScheduleConfigRepository repository, SchedulerServiceImpl schedulerService) {
        this.scheduleConfigRepository = repository;
        this.schedulerService = schedulerService;
        taskScheduler.initialize();
    }

    @Override
    public ScheduleConfigResponseDTO updateCronExpression(ScheduleConfigRequestDTO scheduleConfigRequestDTO, String taskName) {
        String newCron = SalesOpportunityMapper.MAPPER.mapToScheduleConfig(scheduleConfigRequestDTO).getCronExpression();
        ScheduleConfig config = scheduleConfigRepository.findByTaskName(taskName)
                .orElse(new ScheduleConfig());
        if (CronExpression.isValidExpression(newCron)) {
            ZonedDateTime nextExecution = CronExpression.parse(newCron).next(ZonedDateTime.now());
            config.setCronExpression(newCron);
            ScheduleConfig saved = scheduleConfigRepository.save(config);

            if ("Send Reminder".equals(taskName)) {
                restartReminderTask(newCron);
            } else if ("Close Leads".equals(taskName)) {
                restartClosingTask(newCron);
            }

            log.info("Next execution scheduled for {}", nextExecution);
            return SalesOpportunityMapper.MAPPER.mapToScheduleConfigResponseDTO(saved);
        } else {
            log.info("Invalid Cron Expression {}", newCron);
            throw new InvalidCronExpressionException("Invalid Cron Expression " + newCron);
        }
    }

    @Override
    public void restartReminderTask(String cronExpression) {
        if (reminderTask != null) {
            reminderTask.cancel(false);
        }
        reminderTask = taskScheduler.schedule(schedulerService::sendFollowUpReminder, new CronTrigger(cronExpression));
        log.info("Reminder task rescheduled with Cron: {}", cronExpression);
    }

    @Override
    public void restartClosingTask(String cronExpression) {
        if (closingTask != null) {
            closingTask.cancel(false);
        }
        closingTask = taskScheduler.schedule(schedulerService::sendClosingNotification, new CronTrigger(cronExpression));
        log.info("Closing task rescheduled with Cron: {}", cronExpression);
    }
}