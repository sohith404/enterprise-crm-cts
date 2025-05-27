package com.crm.controller;

import com.crm.dto.SalesOpportunityRequestDTO;
import com.crm.dto.SalesOpportunityResponseDTO;
import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.enums.SalesStage;
import com.crm.exception.UnknownErrorOccurredException;
import com.crm.scheduler.DynamicSchedulerService;
import com.crm.service.SalesOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing sales opportunities.
 * Provides endpoints for retrieving, creating, updating, and deleting sales opportunities.
 */
@RestController
public class    SalesOpportunityControllerImpl implements SalesOpportunityController {


    private final SalesOpportunityService service;
    private final DynamicSchedulerService schedulerService;

    @Autowired
    public SalesOpportunityControllerImpl(SalesOpportunityService service, DynamicSchedulerService schedulerService) {
        this.service = service;
        this.schedulerService = schedulerService;
    }


    /**
     * Retrieves list of all available sales opportunities.
     *
     * @return ResponseEntity containing a list of SalesOpportunityResponseDTO objects representing all sales opportunities, or an empty list if none exist.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityResponseDTO>> retrieveAllSalesOpportunities() {
        List<SalesOpportunityResponseDTO> salesOpportunityRequestDTOList = service.retrieveAllSalesOpportunities();
        return new ResponseEntity<>(salesOpportunityRequestDTOList, HttpStatus.OK);
    }

    /**
     * Creates a new sales opportunity.
     *
     * @param salesOpportunityRequestDto the DTO representing the sales opportunity to be created.
     * @return ResponseEntity containing the created SalesOpportunityResponseDTO object.
     */
    @Override
    public ResponseEntity<SalesOpportunityResponseDTO> createSalesOpportunity(SalesOpportunityRequestDTO salesOpportunityRequestDto) {
        SalesOpportunityResponseDTO salesOpportunity = service.createSalesOpportunity(salesOpportunityRequestDto);
        return new ResponseEntity<>(salesOpportunity, HttpStatus.OK);
    }

    /**
     * Updates an existing sales opportunity.
     *
     * @param salesOpportunityRequestDto the DTO representing the sales opportunity to be updated.
     * @param opportunityId ID of lead to be updated.
     * @return ResponseEntity containing the created SalesOpportunityResponseDTO object.
     */
    @Override
    public ResponseEntity<SalesOpportunityResponseDTO> updateSalesOpportunity(Long opportunityId,SalesOpportunityRequestDTO salesOpportunityRequestDto) {
        SalesOpportunityResponseDTO salesOpportunity = service.updateSalesOpportunity(opportunityId,salesOpportunityRequestDto);
        return new ResponseEntity<>(salesOpportunity, HttpStatus.OK);
    }


    /**
     * Updates sales-stage of an existing sales opportunity.
     *
     * @param salesStage New Sales Stage.
     * @param opportunityId ID of lead to be updated.
     * @return ResponseEntity containing the created SalesOpportunityResponseDTO object.
     */
    @Override
    public ResponseEntity<SalesOpportunityResponseDTO> updateSalesStage(Long opportunityId, String salesStage){
        SalesOpportunityResponseDTO salesOpportunity = service.updateSalesStage(opportunityId,SalesStage.valueOf(salesStage));
        return new ResponseEntity<>(salesOpportunity, HttpStatus.OK);
    }


    /**
     * Retrieves a sales opportunity by its opportunity ID.
     *
     * @param opportunityId the ID of the sales opportunity to be retrieved.
     * @return ResponseEntity containing the SalesOpportunityResponseDTO object representing the retrieved sales opportunity.
     */
    @Override
    public ResponseEntity<SalesOpportunityResponseDTO> getOpportunitiesByOpportunity(Long opportunityId) {
        SalesOpportunityResponseDTO result = service.getOpportunitiesByOpportunity(opportunityId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves sales opportunities by customer ID.
     *
     * @param customerId the ID of the customer whose sales opportunities are to be retrieved.
     * @return ResponseEntity containing a list of SalesOpportunityResponseDTO objects representing the retrieved sales opportunities.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesByCustomer(Long customerId) {
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesByCustomer(customerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves sales opportunities by sales stage.
     *
     * @param salesStage the sales stage to filter sales opportunities by.
     * @return ResponseEntity containing a list of SalesOpportunityResponseDTO objects representing the retrieved sales opportunities.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesBySalesStage(SalesStage salesStage) {
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesBySalesStage(salesStage);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves sales opportunities by estimated value.
     *
     * @param estimatedValue the estimated value to filter sales opportunities by.
     * @return ResponseEntity containing a list of SalesOpportunityResponseDTO objects representing the retrieved sales opportunities.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesByEstimatedValue(BigDecimal estimatedValue) {
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesByEstimatedValue(estimatedValue);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves sales opportunities by closing date.
     *
     * @param closingDate the closing date to filter sales opportunities by.
     * @return ResponseEntity containing a list of SalesOpportunityResponseDTO objects representing the retrieved sales opportunities.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesByClosingDate(LocalDate closingDate) {
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesByClosingDate(closingDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves sales opportunities by follow-up reminder date.
     *
     * @param followUpReminder the follow-up reminder date to filter sales opportunities by.
     * @return ResponseEntity containing a list of SalesOpportunityResponseDTO objects representing the retrieved sales opportunities.
     */
    @Override
    public ResponseEntity<List<SalesOpportunityResponseDTO>> getOpportunitiesByFollowUpReminder(LocalDate followUpReminder) {
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesByFollowUpReminder(followUpReminder);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Schedules a follow-up reminder for a sales opportunity.
     *
     * @param opportunityId the ID of the sales opportunity to schedule the reminder for.
     * @param reminderDate  the date for the follow-up reminder.
     * @return ResponseEntity containing the updated SalesOpportunityResponseDTO object.
     */
    @Override
    public ResponseEntity<SalesOpportunityResponseDTO> scheduleFollowUpReminder(Long opportunityId, LocalDate reminderDate) {
        SalesOpportunityResponseDTO result = service.scheduleFollowUpReminder(opportunityId, reminderDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Deletes a sales opportunity by its opportunity ID.
     *
     * @param opportunityId the ID of the sales opportunity to be deleted.
     * @return ResponseEntity containing a success message if the deletion was successful.
     */
    @Override
    public ResponseEntity<String> deleteByOpportunityID(Long opportunityId) {
        boolean success = service.deleteByOpportunityID(opportunityId);
        if (success) {
            return new ResponseEntity<>("{\"message\": \"Successfully deleted Lead with ID " + opportunityId + "\"}", HttpStatus.OK);
        } else {
            throw new UnknownErrorOccurredException("Some error occurred while deleting Lead with ID " + opportunityId);
        }
    }

    /**
     * Configures the cron job schedule for follow-up reminders.
     *
     * @param scheduleConfigRequestDTO containing the new cron expression.
     * @return ResponseEntity containing the updated ScheduleConfigDTO object.
     */
    @Override
    public ResponseEntity<ScheduleConfigResponseDTO> configFollowUpReminderSchedule(ScheduleConfigRequestDTO scheduleConfigRequestDTO) {
        ScheduleConfigResponseDTO result = schedulerService.updateCronExpression(scheduleConfigRequestDTO, "Send Reminder");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Configures the cron job schedule for follow-up reminders.
     *
     * @param scheduleConfigRequestDTO containing the new cron expression.
     * @return ResponseEntity containing the updated ScheduleConfigDTO object.
     */
    @Override
    public ResponseEntity<ScheduleConfigResponseDTO> configClosingSchedule(ScheduleConfigRequestDTO scheduleConfigRequestDTO) {
        ScheduleConfigResponseDTO result = schedulerService.updateCronExpression(scheduleConfigRequestDTO, "Close Leads");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
