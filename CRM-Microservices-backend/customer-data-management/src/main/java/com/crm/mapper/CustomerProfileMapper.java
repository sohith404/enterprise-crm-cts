package com.crm.mapper;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper class for converting between CustomerProfile entities and CustomerProfileDTOs.
 */
@Component
@Slf4j
public class CustomerProfileMapper {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a CustomerProfileMapper with the specified ObjectMapper.
     *
     * @param objectMapper the ObjectMapper to use for JSON processing
     */
    public CustomerProfileMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Converts a CustomerProfile entity to a CustomerProfileDTO.
     *
     * @param customerProfile the CustomerProfile entity to convert
     * @return the converted CustomerProfileDTO
     */
    public CustomerProfileDTO toDTO(CustomerProfile customerProfile) {
        CustomerProfileDTO dto = CustomerProfileDTO.builder()
                .customerID(customerProfile.getCustomerID())
                .name(customerProfile.getName())
                .emailId(customerProfile.getEmailId())
                .phoneNumber(customerProfile.getPhoneNumber())
                .purchaseHistory(customerProfile.getPurchaseHistory())
                .segmentationData(new HashMap<>()) // Initialize segmentationData
                .build();

        if (customerProfile.getSegmentationData() != null && !customerProfile.getSegmentationData().isEmpty()) {
            try {
                JsonNode jsonNode = objectMapper.readTree(customerProfile.getSegmentationData()).get("segmentationData");
                if (jsonNode != null) {
                    Map<String, String> segmentationData = dto.getSegmentationData();
                    segmentationData.put("Region", jsonNode.has("Region") ? jsonNode.get("Region").asText() : null);
                    segmentationData.put("Interest", jsonNode.has("Interest") ? jsonNode.get("Interest").asText() : null);
                    segmentationData.put("Purchasing Habits", jsonNode.has("Purchasing Habits") ? jsonNode.get("Purchasing Habits").asText() : "NEW");
                }
            } catch (JsonProcessingException e) {
                log.error("JsonParsingException Occurred -> {}", e.getMessage());
            }
        }

        return dto;
    }

    /**
     * Converts a CustomerProfileDTO to a CustomerProfile entity.
     *
     * @param customerProfileDTO the CustomerProfileDTO to convert
     * @return the converted CustomerProfile entity
     */
    public CustomerProfile toEntity(CustomerProfileDTO customerProfileDTO) {
        CustomerProfile entity = CustomerProfile.builder()
                .customerID(customerProfileDTO.getCustomerID())
                .name(customerProfileDTO.getName())
                .emailId(customerProfileDTO.getEmailId())
                .phoneNumber(customerProfileDTO.getPhoneNumber())
                .purchaseHistory(customerProfileDTO.getPurchaseHistory())
                .build();

        Map<String, String> segmentationData = customerProfileDTO.getSegmentationData();
        if (segmentationData != null && (!segmentationData.isEmpty())) {
            Map<String, Map<String, String>> wrapperMap = new HashMap<>();
            wrapperMap.put("segmentationData", segmentationData);

            try {
                entity.setSegmentationData(objectMapper.writeValueAsString(wrapperMap));
            } catch (JsonProcessingException e) {
                log.error("JsonParsingException Occurred -> {}", e.getMessage());
            }
        }

        return entity;
    }
}