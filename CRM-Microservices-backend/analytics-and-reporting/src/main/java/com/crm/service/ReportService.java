package com.crm.service;

import com.crm.dto.ReportResponseDTO;
import com.crm.enums.ReportType;
import com.crm.exception.InvalidDataRecievedException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service interface for generating and retrieving reports.
 */
public interface ReportService {

    /**
     * Generates a customer report.
     *
     * @return ReportResponseDTO containing the customer report data.
     * @throws JsonProcessingException if there is an error processing JSON data.
     * @throws InvalidDataRecievedException if invalid data is received from the external service.
     */
    ReportResponseDTO generateCustomerReport() throws JsonProcessingException, InvalidDataRecievedException;

    /**
     * Generates a sales report.
     *
     * @return ReportResponseDTO containing the sales report data.
     * @throws JsonProcessingException if there is an error processing JSON data.
     * @throws InvalidDataRecievedException if invalid data is received from the external service.
     */
    ReportResponseDTO generateSalesReport() throws JsonProcessingException, InvalidDataRecievedException;

    /**
     * Generates a support report.
     *
     * @return ReportResponseDTO containing the support report data.
     * @throws JsonProcessingException if there is an error processing JSON data.
     * @throws InvalidDataRecievedException if invalid data is received from the external service.
     */
    ReportResponseDTO generateSupportReport() throws JsonProcessingException, InvalidDataRecievedException;

    /**
     * Generates a marketing report.
     *
     * @return ReportResponseDTO containing the marketing report data.
     * @throws JsonProcessingException if there is an error processing JSON data.
     * @throws InvalidDataRecievedException if invalid data is received from the external service.
     */
    ReportResponseDTO generateMarketingReport() throws JsonProcessingException, InvalidDataRecievedException;

    /**
     * Retrieves a report by its ID.
     *
     * @param reportId the ID of the report to retrieve.
     * @return ReportResponseDTO containing the report data.
     * @throws NoSuchElementException if no report is found with the given ID.
     */
    ReportResponseDTO getReportById(Long reportId) throws NoSuchElementException;

    /**
     * Retrieves a report by its type.
     *
     * @param reportType the type of the report to retrieve.
     * @return ReportResponseDTO containing the report data.
     */
    List<ReportResponseDTO> getReportByType(ReportType reportType);

    /**
     * Retrieves all reports.
     *
     * @return List of ReportResponseDTO containing all reports.
     */
    List<ReportResponseDTO> getAllReports();
}