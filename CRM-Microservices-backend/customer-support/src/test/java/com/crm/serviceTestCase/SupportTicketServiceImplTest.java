package com.crm.serviceTestCase;

import com.crm.dto.SupportTicketDTO;
import com.crm.entities.SupportTicket;
import com.crm.enums.Status;
import com.crm.exception.InvalidTicketIdException;
import com.crm.exception.InvalidTicketDetailsException;
import com.crm.mapper.SupportTicketMapper;
import com.crm.repository.SupportTicketRepository;
import com.crm.service.SupportTicketServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the SupportTicketServiceImpl.
 * Ensures that the service layer behaves correctly by mocking the repository layer.
 */
class SupportTicketServiceImplTest {

    @InjectMocks
    private SupportTicketServiceImpl service; // Service under test

    @Mock
    private SupportTicketRepository repository; // Mocked repository dependency

    /**
     * Sets up the test environment by initializing mocks.
     * This method is called before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes the @Mock and @InjectMocks annotations
    }

    /**
     * Test case for retrieving all support tickets when tickets exist (positive case).
     * Verifies that the service correctly returns a list of tickets.
     */
    @Test
    @DisplayName("retrieveAllSupportTickets()-positive")
    void retrieveAllSupportTickets_ShouldReturnList_WhenTicketsExist() {
        // Arrange: Mock the repository to return a list of tickets
        List<SupportTicket> tickets = Arrays.asList(
            new SupportTicket(),
            new SupportTicket()
        );
        when(repository.findAll()).thenReturn(tickets);

        // Act: Call the service method
        List<SupportTicketDTO> result = service.retrieveAllSupportTickets();

        // Assert: Verify the size of the returned list and repository interaction
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    /**
     * Test case for retrieving all support tickets when no tickets exist (negative case).
     * Verifies that the service throws a NoSuchElementException.
     */
    @Test
    @DisplayName("retrieveAllSupportTickets()-negative")
    void retrieveAllSupportTickets_ShouldThrowException_WhenNoTicketsExist() {
        // Arrange: Mock the repository to return an empty list
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act and Assert: Verify the exception is thrown
        assertThrows(NoSuchElementException.class, () -> service.retrieveAllSupportTickets());
        verify(repository, times(1)).findAll();
    }

    /**
     * Test case for creating a support ticket with valid input (positive case).
     * Verifies that the service successfully saves the ticket.
     */
    @Test
    @DisplayName("createSupportTicket()-positive")
    void createSupportTicket_ShouldReturnTicket_WhenValidInput() {
        // Arrange: Create a mock ticket and DTO
        SupportTicket ticket = new SupportTicket();
        SupportTicketDTO dto = SupportTicketMapper.MAPPER.mapToDTO(ticket);
        when(repository.save(any(SupportTicket.class))).thenReturn(ticket);

        // Act: Call the service method
        SupportTicketDTO result = service.createSupportTicket(dto);

        // Assert: Verify the saved ticket is not null and repository interaction
        assertNotNull(result);
        verify(repository, times(1)).save(any(SupportTicket.class));
    }

    /**
     * Test case for creating a support ticket with invalid input (negative case).
     * Verifies that the service throws an InvalidTicketDetailsException.
     */
    @Test
    @DisplayName("createSupportTicket()-negative")
    void createSupportTicket_ShouldThrowException_WhenInvalidInput() {
        // Arrange: Mock the repository to throw a runtime exception
        when(repository.save(any(SupportTicket.class))).thenThrow(RuntimeException.class);
        SupportTicketDTO dto = new SupportTicketDTO();

        // Act and Assert: Verify the exception is thrown
        assertThrows(InvalidTicketDetailsException.class, () -> service.createSupportTicket(dto));
        verify(repository, times(1)).save(any(SupportTicket.class));
    }

    /**
     * Test case for retrieving a support ticket by ID when the ticket exists (positive case).
     * Verifies that the service correctly returns the ticket.
     */
    @Test
    @DisplayName("getSupportTicketById()-positive")
    void getSupportTicketById_ShouldReturnTicket_WhenIdExists() {
        // Arrange: Mock the repository to return a ticket
        SupportTicket ticket = new SupportTicket();
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act: Call the service method
        SupportTicketDTO result = service.getSupportTicketById(1L);

        // Assert: Verify the returned ticket is not null and repository interaction
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
    }

    /**
     * Test case for retrieving a support ticket by ID when the ticket does not exist (negative case).
     * Verifies that the service throws a NoSuchElementException.
     */
    @Test
    @DisplayName("getSupportTicketById()-negative")
    void getSupportTicketById_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange: Mock the repository to return an empty Optional
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert: Verify the exception is thrown
        assertThrows(NoSuchElementException.class, () -> service.getSupportTicketById(1L));
        verify(repository, times(1)).findById(1L);
    }

    /**
     * Test case for retrieving tickets by customer ID when tickets exist (positive case).
     * Verifies that the service correctly returns a list of tickets.
     */
    @Test
    @DisplayName("getSupportTicketsByCustomer()-positive")
    void getSupportTicketsByCustomer_ShouldReturnList_WhenTicketsExist() {
        // Arrange: Mock the repository to return a list of tickets
        List<SupportTicket> tickets = Arrays.asList(
            new SupportTicket(),
            new SupportTicket()
        );
        when(repository.findByCustomerID(101L)).thenReturn(tickets);

        // Act: Call the service method
        List<SupportTicketDTO> result = service.getSupportTicketsByCustomer(101L);

        // Assert: Verify the size of the returned list and repository interaction
        assertEquals(2, result.size());
        verify(repository, times(1)).findByCustomerID(101L);
    }

    /**
     * Test case for retrieving tickets by customer ID when no tickets exist (negative case).
     * Verifies that the service throws a NoSuchElementException.
     */
    @Test
    @DisplayName("getSupportTicketsByCustomer()-negative")
    void getSupportTicketsByCustomer_ShouldThrowException_WhenNoTicketsExist() {
        // Arrange: Mock the repository to return an empty list
        when(repository.findByCustomerID(101L)).thenReturn(Arrays.asList());

        // Act and Assert: Verify the exception is thrown
        assertThrows(NoSuchElementException.class, () -> service.getSupportTicketsByCustomer(101L));
        verify(repository, times(1)).findByCustomerID(101L);
    }

    /**
     * Test case for updating the status of a support ticket when the ticket exists (positive case).
     * Verifies that the service updates the status correctly.
     */
    @Test
    @DisplayName("updateTicketStatus()-positive")
    void updateTicketStatus_ShouldUpdateStatus_WhenTicketExists() {
        // Arrange: Mock the repository to find and save a ticket
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketID(1L);
        ticket.setStatus(Status.OPEN);
        ticket.setCustomerID(1L);
        ticket.setAssignedAgent(1L);
        ticket.setIssueDescription("xyz");
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        ticket.setStatus(Status.CLOSED);
        when(repository.save(any())).thenReturn(ticket);

        // Act: Call the service method
        SupportTicketDTO result = service.updateTicketStatus(1L, Status.CLOSED);

        // Assert: Verify the status is updated and repository interaction
        assertEquals(Status.CLOSED, result.getStatus());
        verify(repository, times(1)).save(any(SupportTicket.class));
    }

    /**
     * Test case for updating the status of a support ticket when the ticket does not exist (negative case).
     * Verifies that the service throws an InvalidTicketIdException.
     */
    @Test
    @DisplayName("updateTicketStatus()-negative")
    void updateTicketStatus_ShouldThrowException_WhenTicketDoesNotExist() {
        // Arrange: Mock the repository to return an empty Optional
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert: Verify the exception is thrown
        assertThrows(InvalidTicketIdException.class, () -> service.updateTicketStatus(1L, Status.CLOSED));
        verify(repository, times(1)).findById(1L);
    }
    
    /**
     * Test case for deleting the status of a support ticket when the ticket exists (positive case).
     * Verifies that the service updates the status correctly.
     */
    @Test
    @DisplayName("deleteSupportTicketById()-positive")
    void deleteSupportTicketById_ShouldReturnTrue_WhenTicketExists() {
        // Arrange: Set up a mock SupportTicket
        SupportTicket ticket = new SupportTicket(); // Create a new SupportTicket object
        when(repository.findById(1L)).thenReturn(Optional.of(ticket)); // Mock the repository to return the ticket when findById is called with ID 1L

        // Act: Call the service method to delete the ticket
        boolean result = service.deleteSupportTicketById(1L);

        // Assert: Verify that the ticket is successfully deleted
        assertTrue(result); // Confirm that the service returns true, indicating the ticket was deleted
        verify(repository, times(1)).delete(ticket); // Verify that the repository's delete method is called exactly once with the ticket object
    }
    
    /**
     * Test case for deleting the status of a support ticket when the ticket does not exist (negative case).
     * Verifies that the service throws an InvalidTicketIdException.
     */
    @Test
    @DisplayName("deleteSupportTicketById()-negative")
    void deleteSupportTicketById_ShouldThrowException_WhenTicketDoesNotExist() {
        // Arrange: Mock the repository to return an empty Optional
        when(repository.findById(1L)).thenReturn(Optional.empty()); // Simulate a scenario where the ticket with ID 1L does not exist in the database

        // Act and Assert: Call the service method and verify it throws an InvalidTicketIdException
        assertThrows(InvalidTicketIdException.class, () -> service.deleteSupportTicketById(1L)); // Ensure the exception is thrown when the method is called
        verify(repository, times(1)).findById(1L); // Verify that the repository's findById method is called exactly once with ID 1L
    }
}
