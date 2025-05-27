package com.crm.service;

import com.crm.dto.ErrorResponseDTO;
import com.crm.dto.ReportResponseDTO;
import com.crm.dto.external.CampaignDTO;
import com.crm.dto.external.CustomerProfileDTO;
import com.crm.dto.external.SalesOpportunityResponseDTO;
import com.crm.dto.external.SupportTicketDTO;
import com.crm.entities.Report;
import com.crm.enums.ReportType;
import com.crm.enums.SalesStage;
import com.crm.enums.Status;
import com.crm.enums.Type;
import com.crm.exception.InvalidDataRecievedException;
import com.crm.feign.Proxy;
import com.crm.mapper.ReportMapper;
import com.crm.repository.ReportRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService{

    private final Proxy proxy;
    private final ReportRepository repository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReportServiceImpl(Proxy proxy, ReportRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.proxy = proxy;
    }

    /**
     * @return
     */
    @Override
    public ReportResponseDTO generateCustomerReport() throws JsonProcessingException, InvalidDataRecievedException {
        ResponseEntity<?> response = proxy.getAllCustomerProfiles();
        if(response.getBody() instanceof ErrorResponseDTO || response.getBody() == null){
            ErrorResponseDTO errorResponseDTO = (ErrorResponseDTO) response.getBody();
            throw new InvalidDataRecievedException(errorResponseDTO.getMessage());
        }
        List<CustomerProfileDTO> customerProfiles = (List<CustomerProfileDTO>) response.getBody();
// Aggregating Data for Report
        long totalCustomers = customerProfiles.size();

        long inactiveCustomers = customerProfiles.stream()
                .filter(c -> c.getPurchaseHistory().isEmpty())
                .count();

        // Top 5 Customers by Purchase Volume
        List<Map<String, Object>> top5Customers = customerProfiles.stream()
                .sorted(Comparator.comparingInt(c -> -c.getPurchaseHistory().size()))
                .limit(5)
                .map(c -> Map.<String, Object>of(
                        "customerId", c.getCustomerID(),
                        "name", c.getName(),
                        "purchaseCount", c.getPurchaseHistory().size()
                ))
                .toList();

        // Create Data Points
        Map<String, Object> dataPoints = new HashMap<>();
        dataPoints.put("totalCustomers", totalCustomers);
        dataPoints.put("inactiveCustomers", inactiveCustomers);
        dataPoints.put("top5Customers", top5Customers);

        // Save Report
        Report report = new Report();
        report.setReportType(ReportType.CUSTOMER);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints(objectMapper.writeValueAsString(dataPoints));
        Report savedReport = repository.save(report);
        return ReportMapper.MAPPER.mapToDto(savedReport);
    }

    /**
     * @return
     */
    @Override
    public ReportResponseDTO generateSalesReport() throws JsonProcessingException, InvalidDataRecievedException {
        ResponseEntity<?> response = proxy.retrieveAllSalesOpportunities();
        if(response.getBody() instanceof ErrorResponseDTO || response.getBody() == null){
            ErrorResponseDTO errorResponseDTO = (ErrorResponseDTO) response.getBody();
            throw new InvalidDataRecievedException(errorResponseDTO.getMessage());
        }
        List<SalesOpportunityResponseDTO> salesOpportunities = (List<SalesOpportunityResponseDTO>) response.getBody();


        // Aggregate Data for Report
        BigDecimal totalEstimatedValue = salesOpportunities.stream()
                .map(SalesOpportunityResponseDTO::getEstimatedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal opportunitiesWonValue = salesOpportunities.stream()
                .filter(s -> s.getSalesStage() == SalesStage.CLOSED_WON)
                .map(SalesOpportunityResponseDTO::getEstimatedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal opportunitiesLostValue = salesOpportunities.stream()
                .filter(s -> s.getSalesStage() == SalesStage.CLOSED_LOST)
                .map(SalesOpportunityResponseDTO::getEstimatedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long opportunitiesInProgress = salesOpportunities.stream()
                .filter(s -> s.getSalesStage() == SalesStage.PROSPECTING || s.getSalesStage() == SalesStage.QUALIFICATION)
                .count();

        // Create Data Points
        Map<String, Object> dataPoints = new HashMap<>();
        dataPoints.put("totalEstimatedValue", totalEstimatedValue);
        dataPoints.put("opportunitiesWon", opportunitiesWonValue);
        dataPoints.put("opportunitiesLost", opportunitiesLostValue);
        dataPoints.put("opportunitiesInProgress", opportunitiesInProgress);

        // Save Report
        Report report = new Report();
        report.setReportType(ReportType.SALES);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints(objectMapper.writeValueAsString(dataPoints));
        Report savedReport = repository.save(report);
        return ReportMapper.MAPPER.mapToDto(savedReport);
    }

    /**
     * @return
     */
    @Override
    public ReportResponseDTO generateSupportReport() throws JsonProcessingException, InvalidDataRecievedException {
        ResponseEntity<?> response = proxy.retrieveAllSupportTickets();
        if(response.getBody() instanceof ErrorResponseDTO || response.getBody() == null){
            ErrorResponseDTO errorResponseDTO = (ErrorResponseDTO) response.getBody();
            throw new InvalidDataRecievedException(errorResponseDTO.getMessage());
        }
        List<SupportTicketDTO> supportTickets = (List<SupportTicketDTO>) response.getBody();

        // Aggregating Data for Report
        long totalTickets = supportTickets.size();

        long openTickets = supportTickets.stream()
                .filter(ticket -> ticket.getStatus() == Status.OPEN)
                .count();

        long closedTickets = supportTickets.stream()
                .filter(ticket -> ticket.getStatus() == Status.CLOSED)
                .count();

        // Grouping Tickets by Assigned Agent, handling null values
        Map<Long, Long> ticketsPerAgent = supportTickets.stream()
                .filter(ticket -> ticket.getAssignedAgent() != null) // Filter out tickets with null assigned agent
                .collect(Collectors.groupingBy(SupportTicketDTO::getAssignedAgent, Collectors.counting()));

        // Top 5 Customers with Most Tickets
        Map<Long, Long> top5Customers = supportTickets.stream()
                .collect(Collectors.groupingBy(SupportTicketDTO::getCustomerID, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Create Data Points
        Map<String, Object> dataPoints = new HashMap<>();
        dataPoints.put("totalTickets", totalTickets);
        dataPoints.put("openTickets", openTickets);
        dataPoints.put("closedTickets", closedTickets);
        dataPoints.put("ticketsPerAgent", ticketsPerAgent);
        dataPoints.put("top5Customers", top5Customers);

        // Save Report
        Report report = new Report();
        report.setReportType(ReportType.SUPPORT);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints(objectMapper.writeValueAsString(dataPoints));

        Report savedReport = repository.save(report);
        return ReportMapper.MAPPER.mapToDto(savedReport);
    }

    /**
     * @return
     */
    @Override
    public ReportResponseDTO generateMarketingReport() throws JsonProcessingException, InvalidDataRecievedException {
        ResponseEntity<?> response = proxy.getAllCampaigns();
        if(response.getBody() instanceof ErrorResponseDTO || response.getBody() == null){
            ErrorResponseDTO errorResponseDTO = (ErrorResponseDTO) response.getBody();
            throw new InvalidDataRecievedException(errorResponseDTO.getMessage());
        }
        List<CampaignDTO> campaigns = (List<CampaignDTO>) response.getBody();

        // Aggregating Data for Report
        long totalCampaigns = campaigns.size();

        long activeCampaigns = campaigns.stream()
                .filter(c -> !LocalDate.now().isBefore(c.getStartDate()) && !LocalDate.now().isAfter(c.getEndDate()))
                .count();

        long completedCampaigns = campaigns.stream()
                .filter(c -> LocalDate.now().isAfter(c.getEndDate()))
                .count();

        int totalCustomerInteractions = campaigns.stream()
                .mapToInt(CampaignDTO::getCustomerInteractions)
                .sum();

        // Grouping Campaigns by Type
        Map<Type, Long> campaignsByType = campaigns.stream()
                .collect(Collectors.groupingBy(CampaignDTO::getType, Collectors.counting()));

        // Top Campaign by Interactions
        Optional<CampaignDTO> topCampaign = campaigns.stream()
                .max(Comparator.comparingInt(CampaignDTO::getCustomerInteractions));

        // Campaigns with Zero Interactions
        long zeroInteractionCampaigns = campaigns.stream()
                .filter(c -> c.getCustomerInteractions() == 0)
                .count();

        // Create Data Points
        Map<String, Object> dataPoints = new HashMap<>();
        dataPoints.put("totalCampaigns", totalCampaigns);
        dataPoints.put("activeCampaigns", activeCampaigns);
        dataPoints.put("completedCampaigns", completedCampaigns);
        dataPoints.put("totalCustomerInteractions", totalCustomerInteractions);
        dataPoints.put("campaignsByType", campaignsByType);
        dataPoints.put("topCampaign", topCampaign.map(CampaignDTO::getName).orElse("N/A"));
        dataPoints.put("zeroInteractionCampaigns", zeroInteractionCampaigns);

        // Save Report
        Report report = new Report();
        report.setReportType(ReportType.MARKETING);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints(objectMapper.writeValueAsString(dataPoints));

        Report savedReport = repository.save(report);
        return ReportMapper.MAPPER.mapToDto(savedReport);
    }

    /**
     * @return
     * @throws NoSuchElementException
     */
    @Override
    public ReportResponseDTO getReportById(Long reportId) throws NoSuchElementException {
        Optional<Report> optionalReport = repository.findById(reportId);

        if(optionalReport.isEmpty()){
            throw new NoSuchElementException("No report found with ID: " + reportId);
        }

        return ReportMapper.MAPPER.mapToDto(optionalReport.get());
    }

@Override
public List<ReportResponseDTO> getReportByType(ReportType reportType) {
    List<Report> reportList = repository.findByReportType(reportType);
    if (reportList.isEmpty()) {
        throw new NoSuchElementException("Report not found with type: " + reportType);
    }
    List<ReportResponseDTO> result = new ArrayList<>();
    reportList.forEach(report -> {
        ReportResponseDTO reportResponseDTO = ReportMapper.MAPPER.mapToDto(report); // Use the injected mapper
        result.add(reportResponseDTO);
    });
    return result;
}
    @Override
    public List<ReportResponseDTO> getAllReports() {
        List<Report> reports = repository.findAll();
        return reports.stream()
                .map(ReportMapper.MAPPER::mapToDto)
                .toList();
    }

}
