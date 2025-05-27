package com.crm.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schedule_config")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;
    private String cronExpression;
}