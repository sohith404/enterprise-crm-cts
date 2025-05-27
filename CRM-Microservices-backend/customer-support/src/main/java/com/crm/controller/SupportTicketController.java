package com.crm.controller;

import com.crm.dto.*;
import com.crm.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@Tag(name = "Customer Support Module APIs", description = "APIs for managing customer support tickets, tracking issues, and providing resolutions.")
public interface    SupportTicketController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve all support tickets", description = "Fetches a list of all customer support tickets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of support tickets", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SupportTicketDTO.class))),
            @ApiResponse(responseCode = "404", description = "No support tickets found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<SupportTicketDTO>> retrieveAllSupportTickets();
    

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new support ticket", description = "Registers a new customer support ticket in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new support ticket", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SupportTicketDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<SupportTicketDTO> createSupportTicket(@Valid @RequestBody SupportTicketDTO supportTicketRequestDTO);
    

    @GetMapping(value = "/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve a support ticket by ID", description = "Fetches details of a specific support ticket using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved support ticket", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SupportTicketDTO.class))),
            @ApiResponse(responseCode = "404", description = "Support ticket not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<SupportTicketDTO> getSupportTicketById(@Parameter(description = "ID of the support ticket to retrieve") @PathVariable Long ticketId);
    

    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve support tickets by Customer ID", description = "Fetches all support tickets associated with a specific customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved support tickets", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SupportTicketDTO.class))),
            @ApiResponse(responseCode = "404", description = "No support tickets found for the customer", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByCustomer(@Parameter(description = "ID of the customer") @PathVariable Long customerId);
    

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve support tickets by status", description = "Fetches all support tickets filtered by their status (e.g., Open, Closed).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved support tickets", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SupportTicketDTO.class))),
            @ApiResponse(responseCode = "404", description = "No support tickets found for the specified status", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByStatus(@Parameter(description = "Status of the support tickets to filter by") @PathVariable Status status);
    

    @PatchMapping(value = "/{ticketId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update the status of a support ticket", description = "Changes the status of a specific support ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the status of the support ticket", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SupportTicketDTO.class))),
            @ApiResponse(responseCode = "404", description = "Support ticket not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<SupportTicketDTO> updateTicketStatus(@Parameter(description = "ID of the support ticket") @PathVariable Long ticketId, @Parameter(description = "New status of the support ticket") @RequestParam Status status);
    

    @DeleteMapping(value = "/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a support ticket by ID", description = "Removes a support ticket from the system based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the support ticket", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Support ticket not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<String> deleteSupportTicketById(@Parameter(description = "ID of the support ticket to delete") @PathVariable Long ticketId);
    
    
    @PutMapping(value = "/{ticketId}/{agentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Assigns an agent to a support ticket", description = "Assigns a support ticket from the system to an agent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully assigned the support ticket", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Support ticket not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<SupportTicketDTO> assignTicketToAgent(@Parameter(description = "ID of the support ticket to be assigned") @PathVariable long ticketId,@Parameter(description = "ID of the agent to be assigned") @PathVariable long agentId);
}
