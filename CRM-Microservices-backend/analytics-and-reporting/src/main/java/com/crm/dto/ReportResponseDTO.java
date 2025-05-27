package com.crm.dto;

import com.crm.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDTO {
    private Long id;
    private ReportType reportType;
    private LocalDateTime generatedDate;
    private Map<String, Object> dataPoints;
}
