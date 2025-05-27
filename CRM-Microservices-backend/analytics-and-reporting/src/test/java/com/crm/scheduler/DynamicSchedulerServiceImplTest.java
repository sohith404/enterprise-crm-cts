package com.crm.scheduler;

import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.entities.ScheduleConfig;
import com.crm.exception.InvalidCronExpressionException;
import com.crm.mapper.ReportMapper;
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
    private ReportMapper reportMapper;

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

        when(scheduleConfigRepository.findByTaskName("SendNotification")).thenReturn(Optional.of(config));
        when(scheduleConfigRepository.save(any(ScheduleConfig.class))).thenReturn(config);

        ScheduleConfigResponseDTO result = dynamicSchedulerService.updateCronExpression(scheduleConfigRequestDTO);

        verify(scheduleConfigRepository).save(config);
        assertEquals("0 0/5 * * * ?", result.getCronExpression());
    }

    @Test
    void updateCronExpressionShouldThrowExceptionWhenInvalidCronExpressionProvided() {
        ScheduleConfigRequestDTO scheduleConfigRequestDTO = ScheduleConfigRequestDTO.builder()
                .taskName("Sample Task")
                .cronExpression("invalid-cron")
                .build();

        when(scheduleConfigRepository.findByTaskName("SendNotification")).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidCronExpressionException.class, () -> {
            dynamicSchedulerService.updateCronExpression(scheduleConfigRequestDTO);
        });

        String expectedMessage = "Invalid Cron Expression invalid-cron";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}