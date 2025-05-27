package com.crm.controllerTestCase;

import com.crm.controller.SupportTicketControllerImpl;
import com.crm.dto.SupportTicketDTO;
import com.crm.enums.Status;
import com.crm.service.SupportTicketService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for SupportTicketControllerImpl.
 * Validates the behavior of the controller by mocking the underlying service layer.
 */
class SupportTicketControllerImplTest {

    @Mock
    private SupportTicketService service; // Mocked service dependency

    @InjectMocks
    private SupportTicketControllerImpl controller; // Controller under test, with dependencies injected

    /**
     * Sets up the test environment by initializing mocks.
     * Ensures that mocked dependencies are correctly set up before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes the @Mock and @InjectMocks annotations
    }

    /**
     * Test for retrieving all support tickets (success case).
     * Ensures that the controller returns a valid response with the expected tickets.
     */
    @Test
    @DisplayName("RetrieveAllTickets()-success")
    void testRetrieveAllSupportTickets_Success() {
        // Arrange: Create a mock list of tickets
        List<SupportTicketDTO> mockTickets = new ArrayList<>();
        mockTickets.add(new SupportTicketDTO());
        when(service.retrieveAllSupportTickets()).thenReturn(mockTickets); // Mock service behavior

        // Act: Call the controller method
        ResponseEntity<List<SupportTicketDTO>> response = controller.retrieveAllSupportTickets();

        // Assert: Validate the response
        assertNotNull(response); // Ensure the response is not null
        assertEquals(1, response.getBody().size()); // Ensure the response contains the expected number of tickets
        verify(service, times(1)).retrieveAllSupportTickets(); // Verify that the service method was called once
    }

    /**
     * Test for creating a support ticket (success case).
     * Ensures that the controller returns a valid response for a successful creation.
     */
    @Test
    @DisplayName("createSupportTicket()-success")
    void testCreateSupportTicket_Success() {
        // Arrange: Create mock DTO objects for input and output
        SupportTicketDTO ticketDTO = new SupportTicketDTO();
        SupportTicketDTO savedTicketDTO = new SupportTicketDTO();
        when(service.createSupportTicket(ticketDTO)).thenReturn(savedTicketDTO); // Mock service behavior

        // Act: Call the controller method
        ResponseEntity<SupportTicketDTO> response = controller.createSupportTicket(ticketDTO);

        // Assert: Validate the response
        assertNotNull(response); // Ensure the response is not null
        assertNotNull(response.getBody()); // Ensure the response body contains the created ticket
        verify(service, times(1)).createSupportTicket(ticketDTO); // Verify that the service method was called once
    }

    /**
     * Test for retrieving a support ticket by ID (success case).
     * Ensures that the controller returns the correct ticket when a valid ID is provided.
     */
    @Test
    @DisplayName("GetSupportTicketById()-success")
    void testGetSupportTicketById_Success() {
        // Arrange: Create a mock DTO
        SupportTicketDTO ticketDTO = new SupportTicketDTO();
        when(service.getSupportTicketById(1L)).thenReturn(ticketDTO); // Mock service behavior

        // Act: Call the controller method
        ResponseEntity<SupportTicketDTO> response = controller.getSupportTicketById(1L);

        // Assert: Validate the response
        assertNotNull(response); // Ensure the response is not null
        assertNotNull(response.getBody()); // Ensure the response body contains the ticket
        verify(service, times(1)).getSupportTicketById(1L); // Verify that the service method was called once
    }

    /**
     * Test for retrieving support tickets by customer ID (success case).
     * Ensures that the controller returns the expected tickets for a valid customer ID.
     */
    @Test
    @DisplayName("GetSupportTicketByCustomer()-success")
    void testGetSupportTicketsByCustomer_Success() {
        // Arrange: Create a mock list of tickets
        List<SupportTicketDTO> mockTickets = new ArrayList<>();
        mockTickets.add(new SupportTicketDTO());
        when(service.getSupportTicketsByCustomer(123L)).thenReturn(mockTickets); // Mock service behavior

        // Act: Call the controller method
        ResponseEntity<List<SupportTicketDTO>> response = controller.getSupportTicketsByCustomer(123L);

        // Assert: Validate the response
        assertNotNull(response); // Ensure the response is not null
        assertEquals(1, response.getBody().size()); // Ensure the response contains the expected number of tickets
        verify(service, times(1)).getSupportTicketsByCustomer(123L); // Verify that the service method was called once
    }

    /**
     * Test for retrieving support tickets by status (success case).
     * Ensures that the controller returns the correct tickets for a specific status.
     */
    @Test
    @DisplayName("GetSupportTicketByStatus()-success")
    void testGetSupportTicketsByStatus_Success() {
        // Arrange: Create a mock list of tickets
        List<SupportTicketDTO> mockTickets = new ArrayList<>();
        mockTickets.add(new SupportTicketDTO());
        when(service.getSupportTicketsByStatus(Status.OPEN)).thenReturn(mockTickets); // Mock service behavior

        // Act: Call the controller method
        ResponseEntity<List<SupportTicketDTO>> response = controller.getSupportTicketsByStatus(Status.OPEN);

        // Assert: Validate the response
        assertNotNull(response); // Ensure the response is not null
        assertEquals(1, response.getBody().size()); // Ensure the response contains the expected number of tickets
        verify(service, times(1)).getSupportTicketsByStatus(Status.OPEN); // Verify that the service method was called once
    }

    /**
     * Test for updating the status of a support ticket (success case).
     * Ensures that the controller updates the ticket status correctly.
     */
    @Test
    @DisplayName("UpdateTicketStatus()-success")
    void testUpdateTicketStatus_Success() {
        // Arrange: Create a mock DTO
        SupportTicketDTO ticketDTO = new SupportTicketDTO();
        when(service.updateTicketStatus(1L, Status.CLOSED)).thenReturn(ticketDTO); // Mock service behavior

        // Act: Call the controller method
        ResponseEntity<SupportTicketDTO> response = controller.updateTicketStatus(1L, Status.CLOSED);

        // Assert: Validate the response
        assertNotNull(response); // Ensure the response is not null
        assertNotNull(response.getBody()); // Ensure the response body contains the updated ticket
        verify(service, times(1)).updateTicketStatus(1L, Status.CLOSED); // Verify that the service method was called once
    }

    /**
     * Test for assigning a support ticket to an agent (success case).
     * Ensures that the controller assigns the ticket to the specified agent correctly.
     */
    @Test
    @DisplayName("AssignTicketToAgent()-success")
    void testAssignTicketToAgent_Success() {
        // Arrange: Create a mock DTO
        SupportTicketDTO ticketDTO = new SupportTicketDTO();
        when(service.assignTicketToAgent(1L, 2L)).thenReturn(ticketDTO); // Mock service behavior

        // Act: Call the controller method
        ResponseEntity<SupportTicketDTO> response = controller.assignTicketToAgent(1L, 2L);

        // Assert: Validate the response
        assertNotNull(response); // Ensure the response is not null
        assertNotNull(response.getBody()); // Ensure the response body contains the updated ticket
        verify(service, times(1)).assignTicketToAgent(1L, 2L); // Verify that the service method was called once
    }

    /**
     * Test for deleting a support ticket by ID (success case).
     * Ensures that the controller deletes the ticket successfully and returns a confirmation message.
     */
    @Test
    @DisplayName("DeleteSupportTicketById()-success")
    void testDeleteSupportTicketById_Success() {
        // Arrange: Mock the service to return true for a successful deletion
        when(service.deleteSupportTicketById(1L)).thenReturn(true);

        // Act: Call the controller method
        ResponseEntity<String> response = controller.deleteSupportTicketById(1L);

        // Assert: Validate the response
        assertNotNull(response); // Ensure the response is not null
        assertNotNull(response.getBody()); // Ensure the response contains the confirmation message
        assertTrue(response.getBody().contains("Successfully deleted")); // Check the response message
        verify(service, times(1)).deleteSupportTicketById(1L); // Verify that the service method was called once
    }
}

