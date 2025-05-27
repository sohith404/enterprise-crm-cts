package com.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.crm.dto.CampaignDTO;
import com.crm.entities.Campaign;

@Mapper(componentModel = "spring") // Correct annotation
// Add this annotation
public interface CampaignMapper {

	CampaignMapper  MAPPER = Mappers.getMapper(CampaignMapper.class);
    CampaignDTO mapToDTO(Campaign campaign);

    Campaign mapToCampaign(CampaignDTO campaignDTO);
}