package com.crm.service;

import com.crm.dto.SupportTicketDTO;
import com.crm.enums.Status;
import com.crm.exception.InvalidSupportTicketException;
import com.crm.exception.InvalidTicketDetailsException;

import java.util.List;
import java.util.NoSuchElementException;

public interface SupportTicketService {
    // Retrieves all support tickets
    List<SupportTicketDTO> retrieveAllSupportTickets() throws NoSuchElementException;

    // Creates a new support ticket
    SupportTicketDTO createSupportTicket(SupportTicketDTO supportTicketDto) throws InvalidTicketDetailsException;

    // Retrieves a specific support ticket by ID
    SupportTicketDTO getSupportTicketById(Long ticketId) throws NoSuchElementException;

    // Retrieves all support tickets for a specific customer
    List<SupportTicketDTO> getSupportTicketsByCustomer(Long customerId) throws NoSuchElementException;

    // Retrieves all support tickets by status (Open/Closed)
    List<SupportTicketDTO> getSupportTicketsByStatus(Status status) throws NoSuchElementException;

    // Updates the status of a support ticket
    SupportTicketDTO updateTicketStatus(Long ticketId, Status status) throws InvalidSupportTicketException;

    // Assigns a support ticket to a specific agent
    SupportTicketDTO assignTicketToAgent(Long ticketId, Long agentId) throws InvalidSupportTicketException;

    // Deletes a support ticket by ID
    boolean deleteSupportTicketById(Long ticketId) throws InvalidSupportTicketException;

}
