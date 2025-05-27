package com.crm.service;

import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;
import com.crm.enums.Status;
import com.crm.exception.InvalidTicketIdException;
import com.crm.exception.InvalidTicketDetailsException;
import com.crm.mapper.SupportTicketMapper;
import com.crm.repository.SupportTicketRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service implementation class for managing support tickets.
 * Contains the business logic for creating, retrieving, updating, and deleting support tickets.
 * Implements the SupportTicketService interface.
 */
@Service
public class SupportTicketServiceImpl implements SupportTicketService {

    // Repository instance for interacting with the database
    private final SupportTicketRepository repository;

    /**
     * Constructor to inject the SupportTicketRepository dependency.
     * @param repository The repository for support tickets.
     */
    public SupportTicketServiceImpl(SupportTicketRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all support tickets from the database.
     * Converts the SupportTicket entities into DTOs before returning.
     * @return List of SupportTicketDTO objects.
     * @throws NoSuchElementException if no tickets are found.
     */
    @Override
    public List<SupportTicketDTO> retrieveAllSupportTickets() throws NoSuchElementException {
        List<SupportTicket> tickets = repository.findAll(); // Retrieve all tickets
        List<SupportTicketDTO> ticketDTOs = new ArrayList<>();

        // Convert each entity to a DTO
        tickets.forEach(ticket -> ticketDTOs.add(SupportTicketMapper.MAPPER.mapToDTO(ticket)));

        // Throw exception if no tickets are found
        if (ticketDTOs.isEmpty()) {
            throw new NoSuchElementException("No support tickets available");
        }

        return ticketDTOs;
    }

    /**
     * Creates a new support ticket in the database.
     * Converts the incoming DTO to an entity, saves it, and returns the saved DTO.
     * @param supportTicketDto The DTO containing ticket details.
     * @return The created SupportTicketDTO.
     * @throws InvalidTicketDetailsException if ticket creation fails due to invalid data.
     */
    @Override
    public SupportTicketDTO createSupportTicket(SupportTicketDTO supportTicketDto) throws InvalidTicketDetailsException {
        SupportTicket ticket = SupportTicketMapper.MAPPER.mapToSupportTicket(supportTicketDto); // Convert DTO to entity
        try {
            SupportTicket savedTicket = repository.save(ticket); // Save ticket in the database
            return SupportTicketMapper.MAPPER.mapToDTO(savedTicket); // Convert saved entity back to DTO
        } catch (Exception e) {
            throw new InvalidTicketDetailsException(e.getMessage()); // Handle exceptions during saving
        }
    }

    /**
     * Retrieves a specific support ticket by its ID.
     * @param ticketId The unique ID of the ticket.
     * @return The SupportTicketDTO with the given ID.
     * @throws NoSuchElementException if no ticket is found with the specified ID.
     */
    @Override
    public SupportTicketDTO getSupportTicketById(Long ticketId) throws NoSuchElementException {
        Optional<SupportTicket> ticket = repository.findById(ticketId); // Find ticket by ID
        if (ticket.isPresent()) {
            return SupportTicketMapper.MAPPER.mapToDTO(ticket.get()); // Convert entity to DTO
        } else {
            throw new NoSuchElementException("No ticket found with the given ID");
        }
    }

    /**
     * Retrieves all support tickets associated with a specific customer ID.
     * Converts the entities into DTOs before returning.
     * @param customerId The ID of the customer.
     * @return List of SupportTicketDTO objects.
     * @throws NoSuchElementException if no tickets are found for the given customer ID.
     */
    @Override
    public List<SupportTicketDTO> getSupportTicketsByCustomer(Long customerId) throws NoSuchElementException {
        List<SupportTicket> tickets = repository.findByCustomerID(customerId); // Find tickets by customer ID
        if (tickets.isEmpty()) {
            throw new NoSuchElementException("No tickets found for the given customer ID");
        } else {
            List<SupportTicketDTO> ticketDTOs = new ArrayList<>();
            tickets.forEach(ticket -> ticketDTOs.add(SupportTicketMapper.MAPPER.mapToDTO(ticket))); // Convert entities to DTOs
            return ticketDTOs;
        }
    }

    /**
     * Retrieves all support tickets with a specific status (e.g., OPEN, CLOSED).
     * Converts the entities into DTOs before returning.
     * @param status The status to filter tickets by.
     * @return List of SupportTicketDTO objects.
     * @throws NoSuchElementException if no tickets are found with the given status.
     */
    @Override
    public List<SupportTicketDTO> getSupportTicketsByStatus(Status status) throws NoSuchElementException {
        List<SupportTicket> tickets = repository.findByStatus(status); // Find tickets by status
        if (tickets.isEmpty()) {
            throw new NoSuchElementException("No tickets found with the given status");
        } else {
            List<SupportTicketDTO> ticketDTOs = new ArrayList<>();
            tickets.forEach(ticket -> ticketDTOs.add(SupportTicketMapper.MAPPER.mapToDTO(ticket))); // Convert entities to DTOs
            return ticketDTOs;
        }
    }

    // Constant for common error messages
    private static final String TICKET_NOT_FOUND_MESSAGE = "Ticket with the given ID does not exist";

    /**
     * Updates the status of a specific ticket.
     * @param ticketId The ID of the ticket to update.
     * @param status The new status for the ticket.
     * @return The updated SupportTicketDTO.
     * @throws InvalidTicketIdException if the ticket ID is invalid.
     */
    @Override
    public SupportTicketDTO updateTicketStatus(Long ticketId, Status status) throws InvalidTicketIdException {
        Optional<SupportTicket> ticket = repository.findById(ticketId); // Find ticket by ID
        if (ticket.isPresent()) {
            SupportTicket existingTicket = ticket.get();
            existingTicket.setStatus(status); // Update status
            return SupportTicketMapper.MAPPER.mapToDTO(repository.save(existingTicket)); // Save and convert to DTO
        } else {
            throw new InvalidTicketIdException(TICKET_NOT_FOUND_MESSAGE);
        }
    }

    /**
     * Assigns a ticket to a specific agent by their ID.
     * @param ticketId The ID of the ticket to assign.
     * @param agentId The ID of the agent.
     * @return The updated SupportTicketDTO with the assigned agent.
     * @throws InvalidTicketIdException if the ticket ID is invalid.
     */
    @Override
    public SupportTicketDTO assignTicketToAgent(Long ticketId, Long agentId) throws InvalidTicketIdException {
        Optional<SupportTicket> ticket = repository.findById(ticketId); // Find ticket by ID
        if (ticket.isPresent()) {
            SupportTicket existingTicket = ticket.get();
            existingTicket.setAssignedAgent(agentId); // Assign agent
            SupportTicket updatedTicket = repository.save(existingTicket); // Save updated ticket
            return SupportTicketMapper.MAPPER.mapToDTO(updatedTicket); // Convert to DTO
        } else {
            throw new InvalidTicketIdException(TICKET_NOT_FOUND_MESSAGE);
        }
    }

    /**
     * Deletes a ticket by its ID.
     * @param ticketId The ID of the ticket to delete.
     * @return True if deletion is successful.
     * @throws InvalidTicketIdException if the ticket ID is invalid.
     */
    @Override
    public boolean deleteSupportTicketById(Long ticketId) throws InvalidTicketIdException {
        Optional<SupportTicket> ticket = repository.findById(ticketId); // Find ticket by ID
        if (ticket.isPresent()) {
            repository.delete(ticket.get()); // Delete the ticket
            return true; // Return success
        } else {
            throw new InvalidTicketIdException(TICKET_NOT_FOUND_MESSAGE);
        }
    }
}