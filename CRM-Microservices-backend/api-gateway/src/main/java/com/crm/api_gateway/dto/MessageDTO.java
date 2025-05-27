package com.crm.api_gateway.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDTO {
    private String message;
    private String status;
}
