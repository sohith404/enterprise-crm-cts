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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private Proxy proxy;
    @Mock
    private ReportRepository repository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    private List<CustomerProfileDTO> customerProfiles;
    private List<SalesOpportunityResponseDTO> salesOpportunities;
    private List<SupportTicketDTO> supportTickets;
    private List<CampaignDTO> campaigns;

    @BeforeEach
    void setUp() {
        customerProfiles = Arrays.asList(
                CustomerProfileDTO.builder()
                        .customerID(1L)
                        .name("Customer1")
                        .emailId("email1")
                        .phoneNumber("phone1")
                        .purchaseHistory(Arrays.asList("product1"))
                        .build(),
                CustomerProfileDTO.builder()
                        .customerID(2L)
                        .name("Customer2")
                        .emailId("email2")
                        .phoneNumber("phone2")
                        .purchaseHistory(Arrays.asList("product2", "product3"))
                        .build()
        );
        salesOpportunities = Arrays.asList(
                SalesOpportunityResponseDTO.builder()
                        .opportunityID(1L)
                        .estimatedValue(BigDecimal.valueOf(1000))
                        .salesStage(SalesStage.CLOSED_WON)
                        .build(),
                SalesOpportunityResponseDTO.builder()
                        .opportunityID(2L)
                        .estimatedValue(BigDecimal.valueOf(2000))
                        .salesStage(SalesStage.PROSPECTING)
                        .build(),
                SalesOpportunityResponseDTO.builder()
                        .opportunityID(3L)
                        .estimatedValue(BigDecimal.valueOf(500))
                        .salesStage(SalesStage.CLOSED_LOST)
                        .build()
        );
        supportTickets = Arrays.asList(
                SupportTicketDTO.builder()
                        .ticketID(1L)
                        .customerID(1L)
                        .issueDescription("Issue1")
                        .status(Status.OPEN)
                        .assignedAgent(1L)
                        .build(),
                SupportTicketDTO.builder()
                        .ticketID(2L)
                        .customerID(2L)
                        .issueDescription("Issue2")
                        .status(Status.CLOSED)
                        .assignedAgent(2L)
                        .build()
        );
        campaigns = Arrays.asList(
                CampaignDTO.builder()
                        .campaignID(1L)
                        .name("Campaign1")
                        .type(Type.EMAIL)
                        .startDate(LocalDate.now().minusDays(10))
                        .endDate(LocalDate.now().plusDays(10))
                        .customerInteractions(100)
                        .build(),
                CampaignDTO.builder()
                        .campaignID(2L)
                        .name("Campaign2")
                        .type(Type.SOCIAL_MEDIA)
                        .startDate(LocalDate.now().minusDays(20))
                        .endDate(LocalDate.now().minusDays(5))
                        .customerInteractions(200)
                        .build(),
                CampaignDTO.builder()
                        .campaignID(3L)
                        .name("Campaign3")
                        .type(Type.EMAIL)
                        .startDate(LocalDate.now().plusDays(5))
                        .endDate(LocalDate.now().plusDays(20))
                        .customerInteractions(0)
                        .build()
        );
    }

    @Test
    void generateCustomerReport_Success() throws Exception {
        ResponseEntity<List<CustomerProfileDTO>> responseEntity = new ResponseEntity<>(customerProfiles, HttpStatus.OK);
        when(proxy.getAllCustomerProfiles()).thenReturn(responseEntity);

        Report report = new Report();
        report.setId(1L);
        report.setReportType(ReportType.CUSTOMER);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints("{}");
        when(repository.save(any(Report.class))).thenReturn(report);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        ReportResponseDTO reportResponseDTO = reportService.generateCustomerReport();

        assertNotNull(reportResponseDTO);
        assertEquals(ReportType.CUSTOMER, reportResponseDTO.getReportType());
        verify(proxy, times(1)).getAllCustomerProfiles();
        verify(repository, times(1)).save(any(Report.class));
    }

    @Test
    void generateCustomerReport_InvalidData() {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .code("400")
                .message("Invalid data")
                .path("/customers")
                .build();
        ResponseEntity<ErrorResponseDTO> responseEntity = new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        when(proxy.getAllCustomerProfiles()).thenReturn((ResponseEntity) responseEntity);

        assertThrows(InvalidDataRecievedException.class, () -> reportService.generateCustomerReport());
    }

    @Test
    void generateSalesReport_Success() throws Exception {
        ResponseEntity<List<SalesOpportunityResponseDTO>> responseEntity = new ResponseEntity<>(salesOpportunities, HttpStatus.OK);
        when(proxy.retrieveAllSalesOpportunities()).thenReturn(responseEntity);

        Report report = new Report();
        report.setId(2L);
        report.setReportType(ReportType.SALES);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints("{}");
        when(repository.save(any(Report.class))).thenReturn(report);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        ReportResponseDTO reportResponseDTO = reportService.generateSalesReport();

        assertNotNull(reportResponseDTO);
        assertEquals(ReportType.SALES, reportResponseDTO.getReportType());
        verify(proxy, times(1)).retrieveAllSalesOpportunities();
        verify(repository, times(1)).save(any(Report.class));
    }

    @Test
    void generateSalesReport_InvalidData() {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .code("400")
                .message("Invalid data")
                .path("/sales")
                .build();
        ResponseEntity<ErrorResponseDTO> responseEntity = new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        when(proxy.retrieveAllSalesOpportunities()).thenReturn((ResponseEntity) responseEntity);

        assertThrows(InvalidDataRecievedException.class, () -> reportService.generateSalesReport());
    }

    @Test
    void generateSupportReport_Success() throws Exception {
        ResponseEntity<List<SupportTicketDTO>> responseEntity = new ResponseEntity<>(supportTickets, HttpStatus.OK);
        when(proxy.retrieveAllSupportTickets()).thenReturn(responseEntity);

        Report report = new Report();
        report.setId(3L);
        report.setReportType(ReportType.SUPPORT);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints("{}");
        when(repository.save(any(Report.class))).thenReturn(report);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        ReportResponseDTO reportResponseDTO = reportService.generateSupportReport();

        assertNotNull(reportResponseDTO);
        assertEquals(ReportType.SUPPORT, reportResponseDTO.getReportType());
        verify(proxy, times(1)).retrieveAllSupportTickets();
        verify(repository, times(1)).save(any(Report.class));
    }

    @Test
    void generateSupportReport_InvalidData() {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .code("400")
                .message("Invalid data")
                .path("/support")
                .build();
        ResponseEntity<ErrorResponseDTO> responseEntity = new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        when(proxy.retrieveAllSupportTickets()).thenReturn((ResponseEntity) responseEntity);

        assertThrows(InvalidDataRecievedException.class, () -> reportService.generateSupportReport());
    }
    @Test
    void generateMarketingReport_Success() throws Exception {
        ResponseEntity<List<CampaignDTO>> responseEntity = new ResponseEntity<>(campaigns, HttpStatus.OK);
        when(proxy.getAllCampaigns()).thenReturn(responseEntity);

        Report report = new Report();
        report.setId(4L);
        report.setReportType(ReportType.MARKETING);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints("{}");
        when(repository.save(any(Report.class))).thenReturn(report);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        ReportResponseDTO reportResponseDTO = reportService.generateMarketingReport();

        assertNotNull(reportResponseDTO);
        assertEquals(ReportType.MARKETING, reportResponseDTO.getReportType());
        verify(proxy, times(1)).getAllCampaigns();
        verify(repository, times(1)).save(any(Report.class));
    }
    @Test
    void generateMarketingReport_InvalidData() {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .code("400")
                .message("Invalid data")
                .path("/campaigns")
                .build();
        ResponseEntity<ErrorResponseDTO> responseEntity = new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        when(proxy.getAllCampaigns()).thenReturn((ResponseEntity) responseEntity);

        assertThrows(InvalidDataRecievedException.class, () -> reportService.generateMarketingReport());
    }
    @Test
    void generateCustomerReport_NoCustomers() throws Exception {
        ResponseEntity<List<CustomerProfileDTO>> responseEntity = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        when(proxy.getAllCustomerProfiles()).thenReturn(responseEntity);

        Report report = new Report();
        report.setId(5L);
        report.setReportType(ReportType.CUSTOMER);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints("{}");
        when(repository.save(any(Report.class))).thenReturn(report);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        ReportResponseDTO reportResponseDTO = reportService.generateCustomerReport();

        assertNotNull(reportResponseDTO);
        assertEquals(ReportType.CUSTOMER, reportResponseDTO.getReportType());
        verify(proxy, times(1)).getAllCustomerProfiles();
        verify(repository, times(1)).save(any(Report.class));
    }


    @Test
    void generateSalesReport_NoOpportunities() throws Exception {
        ResponseEntity<List<SalesOpportunityResponseDTO>> responseEntity = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        when(proxy.retrieveAllSalesOpportunities()).thenReturn(responseEntity);

        Report report = new Report();
        report.setId(6L);
        report.setReportType(ReportType.SALES);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints("{}");
        when(repository.save(any(Report.class))).thenReturn(report);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        ReportResponseDTO reportResponseDTO = reportService.generateSalesReport();

        assertNotNull(reportResponseDTO);
        assertEquals(ReportType.SALES, reportResponseDTO.getReportType());
        verify(proxy, times(1)).retrieveAllSalesOpportunities();
        verify(repository, times(1)).save(any(Report.class));
    }
    @Test
    void generateSupportReport_NoTickets() throws Exception {
        ResponseEntity<List<SupportTicketDTO>> responseEntity = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        when(proxy.retrieveAllSupportTickets()).thenReturn(responseEntity);

        Report report = new Report();
        report.setId(7L);
        report.setReportType(ReportType.SUPPORT);
        report.setGeneratedDate(LocalDateTime.now());
        report.setDataPoints("{}");
        when(repository.save(any(Report.class))).thenReturn(report);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        ReportResponseDTO reportResponseDTO = reportService.generateSupportReport();

        assertNotNull(reportResponseDTO);
        assertEquals(ReportType.SUPPORT, reportResponseDTO.getReportType());
        verify(proxy, times(1)).retrieveAllSupportTickets();
        verify(repository, times(1)).save(any(Report.class));
    }
     @Test
     void getReportById() {
       String dataPointsJson = "{\"totalCustomers\":3, \"inactiveCustomers\":0, \"top5Customers\":[{\"customerId\":1, \"purchaseCount\":2, \"name\":\"John Doe\"}, {\"customerId\":2, \"purchaseCount\":2, \"name\":\"Jane Smith\"}, {\"customerId\":3, \"purchaseCount\":2, \"name\":\"Alice Johnson\"}]}";
       Report report = Report.builder().reportType(ReportType.CUSTOMER).id(1L).dataPoints(dataPointsJson).generatedDate(LocalDateTime.now()).build();
       ReportResponseDTO expectedDto = ReportResponseDTO.builder().reportType(ReportType.CUSTOMER).id(1L).build();

     when(repository.findById(1L)).thenReturn(Optional.of(report));
       ReportResponseDTO response = reportService.getReportById(1L);
   assertEquals(expectedDto.getId(), response.getId());
       assertEquals(expectedDto.getReportType(), response.getReportType());
  }
    @Test
  void getReportById_NotFound() {
      when(repository.findById(1L)).thenReturn(Optional.empty());
       assertThrows(NoSuchElementException.class, () -> reportService.getReportById(1L));
   }



        @Test
    void getAllReports_EmptyList() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ReportResponseDTO> result = reportService.getAllReports();

        // Assert
        assertEquals(0, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getReportByType_whenReportsNotFound_shouldThrowNoSuchElementException() {
        // Arrange
        ReportType reportType = ReportType.SUPPORT;
        when(repository.findByReportType(reportType)).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> reportService.getReportByType(reportType));
        verify(repository, times(1)).findByReportType(reportType);
        verify(reportMapper, never()).mapToDto(any(Report.class));
    }


    @Test
    void getAllReports_whenReportsNotFound_shouldReturnEmptyList() {
        // Arrange
        when(repository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<ReportResponseDTO> result = reportService.getAllReports();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
        verify(reportMapper, never()).mapToDto(any(Report.class));
    }

    @Test
    void getReportByType_NotFound() {
        // Arrange
        ReportType reportType = ReportType.CUSTOMER;
        when(repository.findByReportType(reportType)).thenReturn(List.of());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> reportService.getReportByType(reportType));

        verify(repository, times(1)).findByReportType(reportType);
        verify(reportMapper, never()).mapToDto(any(Report.class));
    }




    @Test
    void getReportByType_Found() {
        ReportType reportType = ReportType.CUSTOMER;
        Report report1 = Report.builder()
                .id(1L)
                .reportType(reportType)
                .generatedDate(LocalDateTime.now())
                .dataPoints("{}")
                .build();
        Report report2 = Report.builder()
                .id(2L)
                .reportType(reportType)
                .generatedDate(LocalDateTime.now())
                .dataPoints("{}")
                .build();

        ReportResponseDTO dto1 = ReportResponseDTO.builder()
                .id(1L)
                .reportType(reportType)
                .dataPoints(Map.of())
                .build();
        ReportResponseDTO dto2 = ReportResponseDTO.builder()
                .id(2L)
                .reportType(reportType)
                .dataPoints(Map.of())
                .build();

        when(repository.findByReportType(reportType)).thenReturn(Arrays.asList(report1, report2));


        List<ReportResponseDTO> result = reportService.getReportByType(reportType);

        assertEquals(2, result.size());
        assertEquals(dto1.getDataPoints(), result.get(0).getDataPoints());
        assertEquals(dto2.getDataPoints(), result.get(1).getDataPoints());
        assertTrue(EqualsBuilder.reflectionEquals(dto1, result.get(0), "generatedDate", "dataPoints"));
        assertTrue(EqualsBuilder.reflectionEquals(dto2, result.get(1), "generatedDate", "dataPoints"));

        verify(repository, times(1)).findByReportType(reportType);
    }
}