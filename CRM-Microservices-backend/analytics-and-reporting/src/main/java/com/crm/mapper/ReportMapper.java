package com.crm.mapper;

import com.crm.dto.ReportResponseDTO;
import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.entities.Report;
import com.crm.entities.ScheduleConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Mapper
public interface ReportMapper {

    ReportMapper MAPPER = Mappers.getMapper(ReportMapper.class);

    @Mapping(target = "dataPoints", expression = "java(convertDataPointsToMap(report.getDataPoints()))")
    ReportResponseDTO mapToDto(Report report);

    @Mapping(target = "dataPoints", expression = "java(convertDataPointsToString(reportResponseDTO.getDataPoints()))")
    Report mapToReport(ReportResponseDTO reportResponseDTO);

    // Utility method to convert JSON string to Map
    default Map<String, Object> convertDataPointsToMap(String dataPoints) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(dataPoints, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error while converting dataPoints to Map: " + e.getMessage(), e);
        }
    }

    // Utility method to convert Map to JSON string
    default String convertDataPointsToString(Map<String, Object> dataPoints) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(dataPoints);
        } catch (Exception e) {
            throw new RuntimeException("Error while converting dataPoints to String: " + e.getMessage(), e);
        }
    }

    ScheduleConfig mapToScheduleConfig(ScheduleConfigRequestDTO scheduleConfigRequestDTO);

    ScheduleConfigResponseDTO mapToScheduleConfigResponseDTO(ScheduleConfig scheduleConfig);
}
