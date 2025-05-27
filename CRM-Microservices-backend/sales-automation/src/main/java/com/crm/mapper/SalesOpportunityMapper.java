package com.crm.mapper;

import com.crm.dto.SalesOpportunityRequestDTO;
import com.crm.dto.SalesOpportunityResponseDTO;
import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.entities.SalesOpportunity;
import com.crm.entities.ScheduleConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SalesOpportunityMapper {

    //SalesOpportunityMapper using Factory
    SalesOpportunityMapper MAPPER = Mappers.getMapper(SalesOpportunityMapper.class);

    //SalesOpportunity to SalesOpportunityRequestDTO
    SalesOpportunityRequestDTO mapToRequestDTO(SalesOpportunity salesOpportunity);

    //SalesOpportunityRequestDTO to SalesOpportunity
    SalesOpportunity mapToSalesOpportunity(SalesOpportunityRequestDTO salesOpportunityRequestDTO);

    //SalesOpportunity to SalesOpportunityRequestDTO
    SalesOpportunityResponseDTO mapToResponseDTO(SalesOpportunity salesOpportunity);

    //SalesOpportunityDTO to SalesOpportunity
    ScheduleConfigRequestDTO mapToScheduleConfigDTO(ScheduleConfig scheduleConfig);

    //SalesOpportunityDTO to SalesOpportunity
    ScheduleConfigResponseDTO mapToScheduleConfigResponseDTO(ScheduleConfig scheduleConfig);

    ScheduleConfig mapToScheduleConfig(ScheduleConfigRequestDTO scheduleConfig);
}
