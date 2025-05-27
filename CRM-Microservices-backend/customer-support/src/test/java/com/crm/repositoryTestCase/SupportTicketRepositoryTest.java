package com.crm.repositoryTestCase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.crm.CustomerSupportApplication;
import com.crm.entities.SupportTicket;
import com.crm.enums.Status;
import com.crm.repository.SupportTicketRepository;

/**
 * Test class for the SupportTicketRepository.
 * This class contains JUnit test cases for validating the behavior of the repository methods.
 */
@DataJpaTest // Configures an in-memory database (H2 by default) for JPA repository testing
@ActiveProfiles("test") // Activates the "test" profile to load the test-specific configuration
@ContextConfiguration(classes = {CustomerSupportApplication.class}) // Loads the application context for testing
class SupportTicketRepositoryTest {

    @Autowired
    private SupportTicketRepository supportTicketRepository; // Injects the repository being tested

    /**
    JUnit test cases for SupportTicket repository methods
    */

    /**
     * Test to validate saving a support ticket (positive scenario).
     * Ensures that a ticket is successfully saved and assigned a ticket ID.
     */
    @Test
    @DisplayName("saveSupportTicket() - Positive")
    void testSaveSupportTicket_positive() {
        // Arrange: Create a new support ticket
        SupportTicket ticket = SupportTicket.builder()
                .customerID(1L)
                .issueDescription("The ticket has been opened")
                .assignedAgent(1L)
                .status(Status.OPEN)
                .build();
        
        // Act: Save the ticket in the repository
        SupportTicket savedSupportTicket = supportTicketRepository.save(ticket);

        // Assert: Verify that the ticket ID is greater than 0
        assertTrue(savedSupportTicket.getTicketID() > 0, "Ticket ID should be greater than 0");
    }

    /**
     * Test to validate finding a support ticket by its ID (positive scenario).
     * Ensures that the saved ticket can be retrieved by its ID.
     */
    @Test
    @DisplayName("findSupportTicketById() - Positive")
    void findSupportTicketByIdTest_positive() {
        // Arrange: Create and save a new support ticket
        SupportTicket ticket = SupportTicket.builder()
                .customerID(1L)
                .issueDescription("The ticket has been opened")
                .assignedAgent(1L)
                .status(Status.OPEN)
                .build();
        SupportTicket savedSupportTicket = supportTicketRepository.save(ticket);

        // Act: Retrieve the ticket by ID
        Optional<SupportTicket> foundTicket = supportTicketRepository.findById(savedSupportTicket.getTicketID());

        // Assert: Verify the ticket exists and matches the saved ticket
        assertTrue(foundTicket.isPresent(), "Support Ticket not found");
        assertEquals(savedSupportTicket.getTicketID(), foundTicket.get().getTicketID());
    }

    /**
     * Test to validate finding a support ticket by an invalid ID (negative scenario).
     * Ensures that no ticket is found when an invalid ID is used.
     */
    @Test
    @DisplayName("findSupportTicketById() - Negative")
    void findSupportTicketByIdTest_negative() {
        // Act: Try to find a ticket by a non-existent ID
        Optional<SupportTicket> savedSupportTicket = supportTicketRepository.findById(999L);

        // Assert: Verify that the ticket is not found
        assertFalse(savedSupportTicket.isPresent(), "Support Ticket should not be found");
    }

    /**
     * Test to validate retrieving all support tickets (positive scenario).
     * Ensures that multiple tickets can be retrieved from the repository.
     */
    @Test
    @DisplayName("findAllSupportTicket() - Positive")
    void findAllSupportTicket_positive() {
        // Arrange: Create and save two support tickets
        SupportTicket ticket1 = SupportTicket.builder()
                .customerID(1L)
                .issueDescription("The ticket has been opened")
                .assignedAgent(1L)
                .status(Status.OPEN)
                .build();
        SupportTicket ticket2 = SupportTicket.builder()
                .customerID(2L)
                .issueDescription("The ticket has been closed")
                .assignedAgent(2L)
                .status(Status.CLOSED)
                .build();
        supportTicketRepository.save(ticket1);
        supportTicketRepository.save(ticket2);

        // Act: Retrieve all tickets from the repository
        List<SupportTicket> allTickets = supportTicketRepository.findAll();

        // Assert: Verify that the ticket list is not empty and contains 2 entries
        assertFalse(allTickets.isEmpty(), "SupportTicket list should not be empty");
        assertEquals(2, allTickets.size(), "Support Ticket should contain 2 entries");
    }

    /**
     * Test to validate retrieving all support tickets when the repository is empty (negative scenario).
     */
    @Test
    @DisplayName("findAllSupportTicket() - Negative")
    void findAllSupportTicket_negative() {
        // Act: Retrieve all tickets from an empty repository
        List<SupportTicket> allTickets = supportTicketRepository.findAll();

        // Assert: Verify that the ticket list is empty
        assertTrue(allTickets.isEmpty(), "Support Ticket list should be empty");
    }

    /**
     * Test to validate updating a support ticket (positive scenario).
     * Ensures that a ticket's status can be updated.
     */
    @Test
    @DisplayName("updateSupportTicket() - Positive")
    void updateSupportTicket_Positive() {
        // Arrange: Create and save a new support ticket
        SupportTicket ticket = SupportTicket.builder()
                .customerID(1L)
                .issueDescription("The ticket has been opened")
                .assignedAgent(1L)
                .status(Status.OPEN)
                .build();
        SupportTicket savedTicket = supportTicketRepository.save(ticket);

        // Act: Update the status of the ticket
        SupportTicket supportTicket = supportTicketRepository.findById(savedTicket.getTicketID()).get();
        supportTicket.setStatus(Status.CLOSED);
        SupportTicket updatedTicket = supportTicketRepository.save(supportTicket);

        // Assert: Verify the ticket status is updated
        assertNotNull(updatedTicket);
        assertEquals(Status.CLOSED, updatedTicket.getStatus());
        assertEquals(savedTicket.getTicketID(), updatedTicket.getTicketID());
    }

    /**
     * Test to validate deleting a support ticket (positive scenario).
     * Ensures that a ticket can be successfully deleted from the repository.
     */
    @Test
    @DisplayName("deleteSupportTicket() - Positive")
    void deleteSupportTicket_Positive() {
        // Arrange: Create and save a support ticket
        SupportTicket ticket = SupportTicket.builder()
                .customerID(1L)
                .issueDescription("The ticket has been opened")
                .assignedAgent(1L)
                .status(Status.OPEN)
                .build();
        SupportTicket savedTicket = supportTicketRepository.save(ticket);

        // Act: Delete the ticket
        supportTicketRepository.delete(savedTicket);

        // Assert: Verify that the ticket no longer exists
        Optional<SupportTicket> newSupportTicket = supportTicketRepository.findById(savedTicket.getTicketID());
        assertFalse(newSupportTicket.isPresent());
    }

    /**
     * Test to validate finding tickets by customer ID (positive scenario).
     */
    @Test
    @DisplayName("findSupportTicketByCustomers() - Positive")
    void findSupportTicketByCustomersTest_Positive() {
        // Arrange: Create and save a support ticket
        SupportTicket ticket = SupportTicket.builder()
                .customerID(1L)
                .issueDescription("The ticket has been opened")
                .assignedAgent(1L)
                .status(Status.OPEN)
                .build();
        supportTicketRepository.save(ticket);

        // Act: Find tickets by customer ID
        List<SupportTicket> supportTicketList = supportTicketRepository.findByCustomerID(1L);

        // Assert: Verify the ticket list is not empty
        assertFalse(supportTicketList.isEmpty(), "SupportTicket list not found");
    }

    /**
     * Test to validate finding tickets by customer ID when no tickets exist (negative scenario).
     */
    @Test
    @DisplayName("findSupportTicketByCustomers() - Negative")
    void findSupportTicketByCustomersTest_Negative() {
        // Act: Find tickets for a non-existent customer ID
        List<SupportTicket> supportTicketList = supportTicketRepository.findByCustomerID(2L);

        // Assert: Verify the ticket list is empty
        assertTrue(supportTicketList.isEmpty(), "SupportTicket list not found");
    }

    /**
     * Test to validate finding tickets by status (positive scenario).
     */
    @Test
    @DisplayName("findSupportTicketByStatus() - Positive")
    void findSupportTicketByStatus_Positive() {
        // Arrange: Create and save a support ticket
        SupportTicket ticket = SupportTicket.builder()
                .customerID(1L)
                .issueDescription("The ticket has been opened")
                .assignedAgent(1L)
                .status(Status.OPEN)
                .build();
        supportTicketRepository.save(ticket);

        // Act: Find tickets by status
        List<SupportTicket> supportTicketList = supportTicketRepository.findByStatus(Status.OPEN);

        // Assert: Verify the ticket list is not empty
        assertFalse(supportTicketList.isEmpty(), "SupportTicket list not found");
    }
    
    /**
     * Test to validate finding tickets by status (negative scenario).
     */
	
	@Test
	@DisplayName("findSupportTicketByStatus() - Negative")
	void findSupportTicketByStatus_Negative() {
		
		List <SupportTicket> supportTicketList = supportTicketRepository.findByStatus(Status.CLOSED);
		assertTrue(supportTicketList.isEmpty(),"SupportTicket list not found");
	}
	
	/**
     * Test to validate saving a support ticket (negative scenario).
     */
	@Test
	@DisplayName("saveSupportTicket() - Negative")
	void testSaveSupportTicket_negative() {
	    SupportTicket ticket = new SupportTicket();
	    ticket.setTicketID(1L);
	    ticket.setCustomerID(1L);
	    ticket.setIssueDescription("The ticket has been closed");
	    ticket.setAssignedAgent(1L);
	    ticket.setStatus(Status.CLOSED);

	    Exception exception = assertThrows(Exception.class, () -> {
	        supportTicketRepository.save(ticket);
	    });

	    assertNotNull(exception, "An exception should be thrown when saving an invalid SupportTicket");
	}

}
