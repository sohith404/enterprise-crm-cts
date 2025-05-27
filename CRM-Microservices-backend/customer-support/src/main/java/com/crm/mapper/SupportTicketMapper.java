package com.crm.mapper;

import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupportTicketMapper {

    //SupportTicketMapper using Factory
    SupportTicketMapper MAPPER = Mappers.getMapper(SupportTicketMapper.class);

    //SupportTicket to SupportTicketDTO
    SupportTicketDTO mapToDTO(SupportTicket supportTicket);
    
    //SupportTicketDTO to SupportTicket
    SupportTicket mapToSupportTicket(SupportTicketDTO supportTicketDTO);
}
