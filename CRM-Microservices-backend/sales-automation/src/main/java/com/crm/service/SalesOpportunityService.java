package com.crm.service;

import com.crm.dto.SalesOpportunityRequestDTO;
import com.crm.dto.SalesOpportunityResponseDTO;
import com.crm.enums.SalesStage;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidOpportunityIdException;
import com.crm.exception.InvalidSalesDetailsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service interface for managing sales opportunities.
 */
public interface SalesOpportunityService {

    /**
     * Retrieves all available sales opportunities.
     *
     * @return A list of SalesOpportunityResponseDTO objects representing all sales opportunities.
     * @throws NoSuchElementException if no sales opportunities are found.
     */
    List<SalesOpportunityResponseDTO> retrieveAllSalesOpportunities() throws NoSuchElementException;

    /**
     * Update existing sales opportunity (deal).
     *
     * @param opportunityID ID of the lead to be updated.
     * @param salesOpportunityRequestDto The DTO representing the sales opportunity to be created.
     * @return The updated SalesOpportunityResponseDTO object.
     * @throws InvalidSalesDetailsException if there is an error during update.
     * @throws NoSuchElementException if no sales opportunity is found with the given ID.
     */
    SalesOpportunityResponseDTO updateSalesOpportunity(Long opportunityID ,SalesOpportunityRequestDTO salesOpportunityRequestDto) throws InvalidSalesDetailsException, NoSuchElementException;

    /**
     * Update sales stage of existing opportunity (deal).
     *
     * @param opportunityID ID of the lead to be updated.
     * @param salesStage New Sales Stage
     * @return The updated SalesOpportunityResponseDTO object.
     * @throws IllegalArgumentException if wrong enum is provided.
     * @throws NoSuchElementException if no sales opportunity is found with the given ID.
     */
    SalesOpportunityResponseDTO updateSalesStage(Long opportunityID ,SalesStage salesStage) throws InvalidSalesDetailsException, NoSuchElementException;


    /**
     * Creates a new sales opportunity (deal) associated with a customer.
     *
     * @param salesOpportunityRequestDto The DTO representing the sales opportunity to be created.
     * @return The created SalesOpportunityResponseDTO object.
     * @throws InvalidSalesDetailsException if there is an error during creation.
     */
    SalesOpportunityResponseDTO createSalesOpportunity(SalesOpportunityRequestDTO salesOpportunityRequestDto) throws InvalidSalesDetailsException;

    /**
     * Retrieves a sales opportunity by its opportunity ID.
     *
     * @param opportunityId The ID of the sales opportunity to retrieve.
     * @return The SalesOpportunityResponseDTO object representing the retrieved sales opportunity.
     * @throws NoSuchElementException if no sales opportunity is found with the given ID.
     */
    SalesOpportunityResponseDTO getOpportunitiesByOpportunity(Long opportunityId) throws NoSuchElementException;

    /**
     * Retrieves all sales opportunities linked to a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return A list of SalesOpportunityResponseDTO objects linked to the customer.
     * @throws NoSuchElementException if no sales opportunities are found for the given customer ID.
     */
    List<SalesOpportunityResponseDTO> getOpportunitiesByCustomer(Long customerId) throws NoSuchElementException;

    /**
     * Retrieves all sales opportunities linked to a specific Sales Stage.
     *
     * @param salesStage The sales stage to filter by.
     * @return A list of SalesOpportunityResponseDTO objects linked to the sales stage.
     * @throws NoSuchElementException if no sales opportunities are found for the given sales stage.
     */
    List<SalesOpportunityResponseDTO> getOpportunitiesBySalesStage(SalesStage salesStage) throws NoSuchElementException;

    /**
     * Retrieves all sales opportunities linked to a specific Estimated Value.
     *
     * @param estimatedValue The estimated value to filter by.
     * @return A list of SalesOpportunityResponseDTO objects linked to the estimated value.
     * @throws NoSuchElementException if no sales opportunities are found for the given estimated value.
     */
    List<SalesOpportunityResponseDTO> getOpportunitiesByEstimatedValue(BigDecimal estimatedValue) throws NoSuchElementException;

    /**
     * Retrieves all sales opportunities linked to a specific Closing Date.
     *
     * @param closingDate The closing date to filter by.
     * @return A list of SalesOpportunityResponseDTO objects linked to the closing date.
     * @throws NoSuchElementException if no sales opportunities are found for the given closing date.
     */
    List<SalesOpportunityResponseDTO> getOpportunitiesByClosingDate(LocalDate closingDate) throws NoSuchElementException;

    /**
     * Retrieves all sales opportunities linked to a specific follow-up reminder date.
     *
     * @param followUpReminder The follow-up reminder date to filter by.
     * @return A list of SalesOpportunityResponseDTO objects linked to the follow-up reminder date.
     * @throws NoSuchElementException if no sales opportunities are found for the given follow-up reminder date.
     */
    List<SalesOpportunityResponseDTO> getOpportunitiesByFollowUpReminder(LocalDate followUpReminder) throws NoSuchElementException;

    /**
     * Creates a follow-up reminder for a sales opportunity.
     *
     * @param opportunityId The ID of the sales opportunity.
     * @param reminderDate  The date for the follow-up reminder.
     * @return The updated SalesOpportunityResponseDTO object.
     * @throws InvalidDateTimeException if the reminder date is invalid.
     * @throws InvalidOpportunityIdException if the opportunity ID is invalid.
     */
    SalesOpportunityResponseDTO scheduleFollowUpReminder(Long opportunityId, LocalDate reminderDate) throws InvalidDateTimeException, InvalidOpportunityIdException;

    /**
     * Deletes a sales opportunity by its opportunity ID.
     *
     * @param opportunityId The ID of the sales opportunity to delete.
     * @return true if the deletion was successful, false otherwise.
     * @throws InvalidOpportunityIdException if the opportunity ID is invalid.
     */
    boolean deleteByOpportunityID(Long opportunityId) throws InvalidOpportunityIdException;
}