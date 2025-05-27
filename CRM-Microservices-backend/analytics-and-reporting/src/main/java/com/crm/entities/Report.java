package com.crm.entities;

import com.crm.enums.ReportType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ReportType reportType;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime generatedDate;
    @Lob
    private String dataPoints;
}
