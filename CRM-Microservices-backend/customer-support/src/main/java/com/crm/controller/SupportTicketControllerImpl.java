package com.crm.controller;

import com.crm.dto.SupportTicketDTO;
import com.crm.enums.Status;
import com.crm.service.SupportTicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing customer support tickets.
 * Provides end points for creating, retrieving, updating, and deleting support tickets.
 */
@RestController
public class SupportTicketControllerImpl implements SupportTicketController {

    private final SupportTicketService service;

    public SupportTicketControllerImpl(SupportTicketService service) {
        this.service = service;
    }

    /**
     * Retrieves all support tickets.
     *
     * @return ResponseEntity containing a list of SupportTicketDTO objects representing all tickets, or an empty list if none exist.
     */
    @Override
    public ResponseEntity<List<SupportTicketDTO>> retrieveAllSupportTickets() {
        List<SupportTicketDTO> supportTickets = service.retrieveAllSupportTickets();
        return new ResponseEntity<>(supportTickets, HttpStatus.OK);
    }

    /**
     * Creates a new support ticket.
     *
     * @param supportTicketRequestDTO the DTO representing the support ticket to be created.
     * @return ResponseEntity containing the created SupportTicketDTO object.
     */
    @Override
    public ResponseEntity<SupportTicketDTO> createSupportTicket(SupportTicketDTO supportTicketRequestDTO) {
        SupportTicketDTO newTicket = service.createSupportTicket(supportTicketRequestDTO);
        return new ResponseEntity<>(newTicket, HttpStatus.CREATED);
    }

    /**
     * Retrieves a support ticket by its ticket ID.
     *
     * @param ticketId the ID of the support ticket to be retrieved.
     * @return ResponseEntity containing the SupportTicketDTO object representing the retrieved ticket.
     */
    @Override
    public ResponseEntity<SupportTicketDTO> getSupportTicketById(Long ticketId) {
        SupportTicketDTO ticket = service.getSupportTicketById(ticketId);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    /**
     * Retrieves support tickets by customer ID.
     *
     * @param customerId the ID of the customer whose support tickets are to be retrieved.
     * @return ResponseEntity containing a list of SupportTicketDTO objects representing the retrieved tickets.
     */
    @Override
    public ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByCustomer(Long customerId) {
        List<SupportTicketDTO> tickets = service.getSupportTicketsByCustomer(customerId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    /**
     * Retrieves support tickets by their status.
     *
     * @param status the status to filter support tickets by (e.g., OPEN, CLOSED).
     * @return ResponseEntity containing a list of SupportTicketDTO objects representing the retrieved tickets.
     */
    @Override
    public ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByStatus(Status status) {
        List<SupportTicketDTO> tickets = service.getSupportTicketsByStatus(status);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    /**
     * Updates the status of a support ticket.
     *
     * @param ticketId the ID of the support ticket to update.
     * @param status   the new status of the support ticket.
     * @return ResponseEntity containing the updated SupportTicketDTO object.
     */
    @Override
    public ResponseEntity<SupportTicketDTO> updateTicketStatus(Long ticketId, Status status) {
        SupportTicketDTO updatedTicket = service.updateTicketStatus(ticketId, status);
        return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
    }

    /**
     * Deletes a support ticket by its ticket ID.
     *
     * @param ticketId the ID of the support ticket to delete.
     * @return ResponseEntity containing a success message if the deletion was successful.
     */
    @Override
    public ResponseEntity<String> deleteSupportTicketById(Long ticketId) {
        boolean isDeleted = service.deleteSupportTicketById(ticketId);
        if (isDeleted) {
            return new ResponseEntity<>("{\"message\": \"Successfully deleted support ticket with ID " + ticketId + "\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"message\": \"Failed to delete support ticket with ID " + ticketId + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Assigns the ticket to a Agent.
     *
     * @param ticketId the ID of the support ticket to assign.
     * @param agentId the ID of the ticket assigned to.
     * @return ResponseEntity containing the SupportTicketDTO object.
     */
	public ResponseEntity<SupportTicketDTO> assignTicketToAgent(long ticketId, long agentId) {
		SupportTicketDTO assignTicket = service.assignTicketToAgent(ticketId, agentId);
		return new ResponseEntity<>(assignTicket, HttpStatus.OK);
	}
}
