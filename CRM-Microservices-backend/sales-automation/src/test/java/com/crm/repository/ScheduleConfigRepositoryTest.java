package com.crm.repository;

import com.crm.entities.ScheduleConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ScheduleConfigRepositoryTest {

    @Autowired
    private ScheduleConfigRepository repository;

    @Test
    @DisplayName("save() - Positive")
    void saveScheduleConfigShouldPersistDataSuccessfully() {
        ScheduleConfig sendNotification = ScheduleConfig.builder()
                .taskName("Send Notification")
                .cronExpression("* * * * * *")
                .build();

        ScheduleConfig saved = repository.save(sendNotification);
        assertTrue(saved.getId() > 0);
    }

    @Test
    @DisplayName("findById() - Positive")
    void findScheduleConfigByIdShouldReturnConfigWhenIdExists() {
        ScheduleConfig sendNotification = ScheduleConfig.builder()
                .taskName("Send Notification")
                .cronExpression("* * * * * *")
                .build();

        ScheduleConfig saved = repository.save(sendNotification);
        Optional<ScheduleConfig> configOptional = repository.findById(saved.getId());
        assertTrue(configOptional.isPresent());
    }

    @Test
    @DisplayName("findById() - Negative")
    void findScheduleConfigByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Optional<ScheduleConfig> configOptional = repository.findById(1L);
        assertFalse(configOptional.isPresent());
    }

    @Test
    @DisplayName("findAll() - Positive")
    void findAllScheduleConfigsShouldReturnListOfConfigsWhenDataExists() {
        ScheduleConfig obj1 = ScheduleConfig.builder()
                .taskName("Send Notification")
                .cronExpression("* * * * * *")
                .build();
        ScheduleConfig obj2 = ScheduleConfig.builder()
                .taskName("Send Notification")
                .cronExpression("* * * * * *")
                .build();
        ScheduleConfig obj3 = ScheduleConfig.builder()
                .taskName("Send Notification")
                .cronExpression("* * * * * *")
                .build();

        repository.save(obj1);
        repository.save(obj2);
        repository.save(obj3);

        List<ScheduleConfig> scheduleConfigList = repository.findAll();
        assertFalse(scheduleConfigList.isEmpty());
    }

    @Test
    @DisplayName("findAll() - Negative")
    void findAllScheduleConfigsShouldReturnEmptyListWhenNoDataExists() {
        List<ScheduleConfig> scheduleConfigList = repository.findAll();
        assertTrue(scheduleConfigList.isEmpty());
    }

    @Test
    @DisplayName("update() - Positive")
    void updateScheduleConfigShouldModifyExistingConfig() {
        ScheduleConfig obj1 = ScheduleConfig.builder()
                .taskName("Send Notification")
                .cronExpression("* * * * * *")
                .build();
        ScheduleConfig scheduleConfig = repository.save(obj1);

        scheduleConfig.setCronExpression("1 2 3 4 5 6");
        ScheduleConfig updatedScheduleConfig = repository.save(scheduleConfig);
        assertEquals("1 2 3 4 5 6", updatedScheduleConfig.getCronExpression());
    }

    @Test
    @DisplayName("delete() - Positive")
    void deleteScheduleConfigShouldRemoveConfigFromRepository() {
        ScheduleConfig sendNotification = ScheduleConfig.builder()
                .taskName("Send Notification")
                .cronExpression("* * * * * *")
                .build();

        ScheduleConfig saved = repository.save(sendNotification);
        repository.delete(saved);
        Optional<ScheduleConfig> configOptional = repository.findById(saved.getId());
        assertFalse(configOptional.isPresent());
    }


    @Test
    @DisplayName("findByTaskName() - Positive")
    void findScheduleConfigByTaskNameShouldReturnConfigWhenTaskNameExists() {
        ScheduleConfig sendNotification = ScheduleConfig.builder()
                .taskName("Send Notification")
                .cronExpression("* * * * * *")
                .build();

        repository.save(sendNotification);
        Optional<ScheduleConfig> scheduleConfig = repository.findByTaskName(sendNotification.getTaskName());
        assertTrue(scheduleConfig.isPresent());
    }

    @Test
    @DisplayName("findByTaskName() - Negative")
    void findScheduleConfigByTaskNameShouldReturnEmptyOptionalWhenTaskNameDoesNotExist() {
        Optional<ScheduleConfig> scheduleConfig = repository.findByTaskName("Task Name");
        assertTrue(scheduleConfig.isEmpty());
    }
}
