package com.crm.repository;

import com.crm.entities.ScheduleConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleConfigRepository extends JpaRepository<ScheduleConfig, Long> {
    Optional<ScheduleConfig> findByTaskName(String taskName);
}