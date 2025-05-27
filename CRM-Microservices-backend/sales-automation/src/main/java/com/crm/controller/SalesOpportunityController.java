package com.crm.controller;

import com.crm.dto.*;
import com.crm.enums.SalesStage;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/sales-opportunity")
@Tag(name = "Sales-Automation APIs", description = "Sales-Automation APIs helps to automate the sales process. Employees from sales/marketing teams can create new leads linked to customers. This module manages the lifecycle of leads (Sales Opportunities) and uses a scheduler to trigger notifications or reminders based on pre-set follow-up reminders.")
public interface SalesOpportunityController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all available lead opportunities", description = "Retrieves a list of all sales opportunities currently stored in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of sales opportunities", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No sales opportunities found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<SalesOpportunityResponseDTO>> retrieveAllSalesOpportunities();

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new Lead in the database", description = "Creates a new sales opportunity (lead) and stores it in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created new sales opportunity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<SalesOpportunityResponseDTO> createSalesOpportunity(@Valid @RequestBody SalesOpportunityRequestDTO salesOpportunityRequestDto);

    @PutMapping(value = "/{opportunityId}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update existing Lead in the database", description = "Updates an existing sales opportunity (lead) and stores it in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated sales opportunity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Lead not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<SalesOpportunityResponseDTO> updateSalesOpportunity(@PathVariable Long opportunityId,@Valid @RequestBody SalesOpportunityRequestDTO salesOpportunityRequestDto);

    @PatchMapping(value = "/{opportunityId}/{salesStage}",consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update sales-stage of existing Lead in the database", description = "Updates sales-stage of an existing sales opportunity (lead) and stores it in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated sales opportunity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Lead not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<SalesOpportunityResponseDTO> updateSalesStage(@PathVariable Long opportunityId,@PathVariable String salesStage);

    @GetMapping(value = "/{opportunityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a particular lead opportunity based on ID", description = "Retrieves a specific sales opportunity based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sales opportunity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sales opportunity not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<SalesOpportunityResponseDTO> getOpportunitiesByOpportunity(@Parameter(description = "ID of the sales opportunity to retrieve") @PathVariable Long opportunityId);

    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all particular lead opportunities with provided Customer ID", description = "Retrieves all sales opportunities associated with a specific customer ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sales opportunities", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No sales opportunities found for the customer", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesByCustomer(@Parameter(description = "ID of the customer") @PathVariable Long customerId);

    @GetMapping(value = "/salesStage/{salesStage}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a particular opportunities with provided Sales-Stage", description = "Retrieves all sales opportunities that match the specified sales stage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sales opportunities", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No sales opportunities found for the sales stage", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesBySalesStage(@Parameter(description = "Sales stage to filter by") @PathVariable SalesStage salesStage);

    @GetMapping(value = "/estimatedValue/{estimatedValue}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a particular opportunities with provided Estimated Value", description = "Retrieves all sales opportunities that match the specified estimated value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sales opportunities", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No sales opportunities found for the estimated value", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesByEstimatedValue(@Parameter(description = "Estimated value to filter by") @PathVariable BigDecimal estimatedValue);

    @GetMapping(value = "/closingDate/{closingDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all lead opportunities matching provided Closing Date", description = "Retrieves all sales opportunities that match the specified closing date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sales opportunities", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No sales opportunities found for the closing date", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesByClosingDate(@Parameter(description = "Closing date to filter by") @PathVariable LocalDate closingDate);

    @GetMapping(value = "/followUpReminder/{followUpReminder}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all lead opportunities matching provided Follow-Up Reminder", description = "Retrieves all sales opportunities that match the specified follow-up reminder date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sales opportunities", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No sales opportunities found for the follow-up reminder date", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesByFollowUpReminder(@Parameter(description = "Follow-up reminder date to filter by") @PathVariable LocalDate followUpReminder);

    @PostMapping(value = "/followUpReminder", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Set follow-up reminder for the Lead with given ID", description = "Sets a follow-up reminder date for a specific sales opportunity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set follow-up reminder", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SalesOpportunityResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sales opportunity not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<SalesOpportunityResponseDTO> scheduleFollowUpReminder(@Parameter(description = "ID of the sales opportunity") @RequestParam Long opportunityId, @RequestParam LocalDate reminderDate);

    @DeleteMapping(value = "/{opportunityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Lead with given ID", description="Deletes a sales opportunity based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted sales opportunity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Sales opportunity not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<String> deleteByOpportunityID(@Parameter(description = "ID of the sales opportunity to delete") @PathVariable Long opportunityId);

    @PostMapping(value = "/setReminderSchedule", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Set the time for sending reminder emails", description = "Configures the cron job schedule for sending follow-up reminder emails.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully configured cron job", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ScheduleConfigRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid cron expression", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<ScheduleConfigResponseDTO> configFollowUpReminderSchedule(@Valid @RequestBody ScheduleConfigRequestDTO scheduleConfigRequestDTO);

    @PostMapping(value = "/setClosingSchedule", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Set the time for closing leads", description = "Configures the cron job schedule for closing leads.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully configured cron job", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ScheduleConfigRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid cron expression", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<ScheduleConfigResponseDTO> configClosingSchedule(@Valid @RequestBody ScheduleConfigRequestDTO scheduleConfigRequestDTO);

}
