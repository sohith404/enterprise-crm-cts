package com.crm.scheduler;

import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.entities.ScheduleConfig;
import com.crm.exception.InvalidCronExpressionException;
import com.crm.mapper.SalesOpportunityMapper;
import com.crm.repository.ScheduleConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DynamicSchedulerServiceImplTest {


    @Mock
    private ScheduleConfigRepository scheduleConfigRepository;

    @Mock
    private SchedulerServiceImpl schedulerServiceImpl;

    @Mock
    private SalesOpportunityMapper salesOpportunityMapper;

    @InjectMocks
    private DynamicSchedulerServiceImpl dynamicSchedulerService;

    @Test
    void updateCronExpressionShouldUpdateAndReturnConfigWhenValidCronExpressionProvided() {
        ScheduleConfigRequestDTO scheduleConfigRequestDTO = ScheduleConfigRequestDTO.builder()
                .taskName("Send Reminder")
                .cronExpression("0 0/5 * * * ?")
                .build();

        ScheduleConfig config = ScheduleConfig.builder()
                .id(1L)
                .taskName("Send Reminder")
                .cronExpression("0 0/5 * * * ?")
                .build();

        when(scheduleConfigRepository.findByTaskName("Send Reminder")).thenReturn(Optional.of(config));
        when(scheduleConfigRepository.save(any(ScheduleConfig.class))).thenReturn(config);

        ScheduleConfigResponseDTO result = dynamicSchedulerService.updateCronExpression(scheduleConfigRequestDTO, "Send Reminder");

        verify(scheduleConfigRepository).save(config);
        assertEquals("0 0/5 * * * ?", result.getCronExpression());
    }

    @Test
    void setReminderScheduleShouldThrowExceptionWhenInvalidCronExpressionProvided() {
        ScheduleConfigRequestDTO scheduleConfigRequestDTO = ScheduleConfigRequestDTO.builder()
                .taskName("Sample Task")
                .cronExpression("invalid-cron")
                .build();

        when(scheduleConfigRepository.findByTaskName("Send Reminder")).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidCronExpressionException.class, () -> {
            dynamicSchedulerService.updateCronExpression(scheduleConfigRequestDTO, "Send Reminder");
        });

        String expectedMessage = "Invalid Cron Expression invalid-cron";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void setReminderScheduleShouldUpdateAndReturnConfigWhenValidCronExpressionProvided() {
        ScheduleConfigRequestDTO scheduleConfigRequestDTO = ScheduleConfigRequestDTO.builder()
                .taskName("Close Leads")
                .cronExpression("0 0/5 * * * ?")
                .build();

        ScheduleConfig config = ScheduleConfig.builder()
                .id(1L)
                .taskName("Close Leads")
                .cronExpression("0 0/5 * * * ?")
                .build();

        when(scheduleConfigRepository.findByTaskName("Close Leads")).thenReturn(Optional.of(config));
        when(scheduleConfigRepository.save(any(ScheduleConfig.class))).thenReturn(config);

        ScheduleConfigResponseDTO result = dynamicSchedulerService.updateCronExpression(scheduleConfigRequestDTO, "Close Leads");

        verify(scheduleConfigRepository).save(config);
        assertEquals("0 0/5 * * * ?", result.getCronExpression());
    }

    @Test
    void updateCronExpressionShouldThrowExceptionWhenInvalidCronExpressionProvided() {
        ScheduleConfigRequestDTO scheduleConfigRequestDTO = ScheduleConfigRequestDTO.builder()
                .taskName("Sample Task")
                .cronExpression("invalid-cron")
                .build();

        when(scheduleConfigRepository.findByTaskName("Close Leads")).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidCronExpressionException.class, () -> {
            dynamicSchedulerService.updateCronExpression(scheduleConfigRequestDTO, "Close Leads");
        });

        String expectedMessage = "Invalid Cron Expression invalid-cron";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}