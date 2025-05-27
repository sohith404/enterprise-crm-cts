package com.crm.controller;

import com.crm.dto.*;
import com.crm.enums.ReportType;
import com.crm.exception.InvalidCronExpressionException;
import com.crm.exception.InvalidDataRecievedException;
import com.crm.scheduler.DynamicSchedulerService;
import com.crm.service.ReportService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReportService reportService;

    @MockitoBean
    private DynamicSchedulerService schedulerService;

    private static List<ReportResponseDTO> list;

    @BeforeEach
    void setup(){
        list = new ArrayList<>();

        // Adding multiple instances of ReportResponseDTO
        list.add(ReportResponseDTO.builder()
                .id(1L)
                .reportType(ReportType.MARKETING)
                .generatedDate(LocalDateTime.now().minusDays(1))
                .dataPoints(new HashMap<>() {{
                    put("Total Sales", 15000);
                    put("Region", "North");
                }})
                .build());

        list.add(ReportResponseDTO.builder()
                .id(2L)
                .reportType(ReportType.CUSTOMER)
                .generatedDate(LocalDateTime.now().minusDays(2))
                .dataPoints(new HashMap<>() {{
                    put("New Customers", 250);
                    put("Returning Customers", 75);
                }})
                .build());

        list.add(ReportResponseDTO.builder()
                .id(3L)
                .reportType(ReportType.SALES)
                .generatedDate(LocalDateTime.now())
                .dataPoints(new HashMap<>() {{
                    put("Revenue", 500000);
                    put("Expenses", 200000);
                }})
                .build());
        list.add(ReportResponseDTO.builder()
                .id(4L)
                .reportType(ReportType.SUPPORT)
                .generatedDate(LocalDateTime.now())
                .dataPoints(new HashMap<>() {{
                    put("Revenue", 500000);
                    put("Expenses", 200000);
                }})
                .build());
    }

    @Test
    @DisplayName("GET /api/analytics ==> 200")
    void retrieveAllReportsShouldReturn200AndListOfReports() throws Exception {
        // api: GET /api/analytics ==> 200 : List<ReportResponseDTO>
        List<ReportResponseDTO> expected = list;
        when(reportService.getAllReports()).thenReturn(list);

        MvcResult mvcResult = mockMvc.perform(get("/api/analytics"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<ReportResponseDTO> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<ReportResponseDTO>>() {
        });
        assertEquals(expected, actual);
        verify(reportService, times(1)).getAllReports();
    }

    @Test
    @DisplayName("GET /api/analytics ==> 404")
    void retrieveAllReportsShouldReturn404AndErrorResponseDTO() throws Exception {
        // api: GET /api/analytics ==> 404 : List<ErrorResponseDTO>
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/analytics")
                .message("No Reports Available")
                .build();

        when(reportService.getAllReports()).thenReturn(new ArrayList<>());

        MvcResult mvcResult = mockMvc.perform(get("/api/analytics"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
       ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ErrorResponseDTO>() {
        });
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(reportService, times(1)).getAllReports();
    }

    @Test
    @DisplayName("GET /api/analytics/type/{type} ==> 200")
    void retrieveReportsByTypeShouldReturn200AndListOfReports() throws Exception {
        // api: GET /api/analytics/type/{type} ==> 200 : List<ReportResponseDTO>
        List<ReportResponseDTO> expected = Collections.singletonList(list.getFirst());
        when(reportService.getReportByType(ReportType.MARKETING)).thenReturn(Collections.singletonList(list.getFirst()));

        MvcResult mvcResult = mockMvc.perform(get("/api/analytics/type/MARKETING"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<ReportResponseDTO> actual = objectMapper.readValue(jsonResponse, new TypeReference<List<ReportResponseDTO>>() {
        });
        assertEquals(expected, actual);
        verify(reportService, times(1)).getReportByType(ReportType.MARKETING);
    }

    @Test
    @DisplayName("GET /api/analytics/type/{type} ==> 404")
    void retrieveReportsByTypeShouldReturn404AndErrorResponseDTO() throws Exception {
        // api: GET /api/analytics/type/{type} ==> 404 : List<ErrorResponseDTO>
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/analytics/type/MARKETING")
                .message("Report not found with type: 1")
                .build();

        when(reportService.getReportByType(ReportType.MARKETING)).thenThrow(new NoSuchElementException("Report not found with type: " + 1));


        MvcResult mvcResult = mockMvc.perform(get("/api/analytics/type/MARKETING"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ErrorResponseDTO>() {
        });
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(reportService, times(1)).getReportByType(ReportType.MARKETING);
    }


    @Test
    @DisplayName("GET /api/analytics/{id} ==> 200")
    void retrieveReportsByIdShouldReturn200AndListOfReports() throws Exception {
        // api: GET /api/analytics/{id} ==> 200 : List<ReportResponseDTO>
        ReportResponseDTO expected = list.getFirst();
        when(reportService.getReportById(1L)).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/analytics/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ReportResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ReportResponseDTO>() {
        });
        assertEquals(expected, actual);
        verify(reportService, times(1)).getReportById(1L);
    }

    @Test
    @DisplayName("GET /api/analytics/{id} ==> 404")
    void retrieveReportByIdShouldReturn404AndErrorResponseDTO() throws Exception {
        // api: GET /api/analytics/{id} ==> 404 : List<ErrorResponseDTO>
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("404")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/analytics/1")
                .message("No report found with ID: 1")
                .build();

        when(reportService.getReportById(1L)).thenThrow(new NoSuchElementException("No report found with ID: " + 1));

        MvcResult mvcResult = mockMvc.perform(get("/api/analytics/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ErrorResponseDTO>() {
        });
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(reportService, times(1)).getReportById(1L);
    }


    @Test
    @DisplayName("POST /api/analytics/sales ==> 200")
    void generateSalesReportsShouldReturn200AndListOfSalesReports() throws Exception {
        // api: GET /api/analytics/sales ==> 200 : List<ReportResponseDTO>
        ReportResponseDTO expected = list.get(2);
        when(reportService.generateSalesReport()).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/sales"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ReportResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ReportResponseDTO>() {
        });
        assertEquals(expected, actual);
        verify(reportService, times(1)).generateSalesReport();
    }

    @Test
    @DisplayName("POST /api/analytics/sales ==> 400")
    void generateSalesReportsShouldReturn400AndErrorResponseDTO() throws Exception {
        // api: POST /api/analytics ==> 404 : List<ErrorResponseDTO>
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/analytics/sales")
                .message("Invalid Data Received")
                .build();

        when(reportService.generateSalesReport()).thenThrow(new InvalidDataRecievedException("Invalid Data Received"));

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/sales"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ErrorResponseDTO>() {
        });
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(reportService, times(1)).generateSalesReport();
    }

    @Test
    @DisplayName("POST /api/analytics/supportTickets ==> 200")
    void generateSupportTicketsReportsShouldReturn200AndListOfSalesReports() throws Exception {
        // api: GET /api/analytics/supportTickets ==> 200 : List<ReportResponseDTO>
        ReportResponseDTO expected = list.getLast();
        when(reportService.generateSupportReport()).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/supportTickets"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ReportResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ReportResponseDTO>() {
        });
        assertEquals(expected, actual);
        verify(reportService, times(1)).generateSupportReport();
    }

    @Test
    @DisplayName("POST /api/analytics/supportTickets ==> 400")
    void generateSupportTicketsReportsShouldReturn400AndErrorResponseDTO() throws Exception {
        // api: POST /api/supportTickets ==> 404 : List<ErrorResponseDTO>
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/analytics/supportTickets")
                .message("Invalid Data Received")
                .build();

        when(reportService.generateSupportReport()).thenThrow(new InvalidDataRecievedException("Invalid Data Received"));

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/supportTickets"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ErrorResponseDTO>() {
        });
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(reportService, times(1)).generateSupportReport();
    }


    @Test
    @DisplayName("POST /api/analytics/marketingCampaigns ==> 200")
    void generateMarketingReportsShouldReturn200AndListOfSalesReports() throws Exception {
        // api: GET /api/analytics/marketingCampaigns ==> 200 : List<ReportResponseDTO>
        ReportResponseDTO expected = list.getFirst();
        when(reportService.generateMarketingReport()).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/marketingCampaigns"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ReportResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ReportResponseDTO>() {
        });
        assertEquals(expected, actual);
        verify(reportService, times(1)).generateMarketingReport();
    }

    @Test
    @DisplayName("POST /api/analytics/marketingCampaigns ==> 400")
    void generateMarketingReportsShouldReturn400AndErrorResponseDTO() throws Exception {
        // api: POST /api/marketingCampaigns ==> 400 : List<ErrorResponseDTO>
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/analytics/marketingCampaigns")
                .message("Invalid Data Received")
                .build();

        when(reportService.generateMarketingReport()).thenThrow(new InvalidDataRecievedException("Invalid Data Received"));

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/marketingCampaigns"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ErrorResponseDTO>() {
        });
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(reportService, times(1)).generateMarketingReport();
    }


    @Test
    @DisplayName("POST /api/analytics/customers ==> 200")
    void generateCustomerReportsShouldReturn200AndListOfSalesReports() throws Exception {
        // api: GET /api/analytics/customers ==> 200 : List<ReportResponseDTO>
        ReportResponseDTO expected = list.get(1);
        when(reportService.generateCustomerReport()).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ReportResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ReportResponseDTO>() {
        });
        assertEquals(expected, actual);
        verify(reportService, times(1)).generateCustomerReport();
    }

    @Test
    @DisplayName("POST /api/analytics/customers ==> 400")
    void generateCustomerReportsShouldReturn400AndErrorResponseDTO() throws Exception {
        // api: POST /api/customers ==> 400 : List<ErrorResponseDTO>
        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .code("400")
                .timestamp(LocalDateTime.now())
                .path("uri=/api/analytics/customers")
                .message("Invalid Data Received")
                .build();

        when(reportService.generateCustomerReport()).thenThrow(new InvalidDataRecievedException("Invalid Data Received"));

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/customers"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, new TypeReference<ErrorResponseDTO>() {
        });
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(reportService, times(1)).generateCustomerReport();
    }

    @Test
    @DisplayName("POST /api/sales-opportunity/configureCron ==> 200")
    void configCronJobShouldReturn200AndUpdatedScheduleConfig() throws Exception {
        ScheduleConfigRequestDTO scheduleConfigRequestDTO = ScheduleConfigRequestDTO.builder()
                .cronExpression("* * * * * *")
                .build();

        ScheduleConfigResponseDTO expected = ScheduleConfigResponseDTO.builder()
                .cronExpression("* * * * * *")
                .taskName("SendNotification")
                .id(1L)
                .build();


        when(schedulerService.updateCronExpression(scheduleConfigRequestDTO)).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/configureCron")
                        .content("{\"cronExpression\":\"* * * * * *\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ScheduleConfigResponseDTO actual = objectMapper.readValue(jsonResponse, ScheduleConfigResponseDTO.class);
        assertEquals(expected, actual);
        verify(schedulerService, times(1)).updateCronExpression(scheduleConfigRequestDTO);
    }

    @Test
    @DisplayName("POST /api/analytics/configureCron ==> 400")
    void configCronJobShouldReturn400WhenInvalidCronExpression() throws Exception {
        ScheduleConfigRequestDTO scheduleConfigRequestDTO = ScheduleConfigRequestDTO.builder()
                .cronExpression("a 2 c aa 2 1")
                .build();

        ErrorResponseDTO expected = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .code("400")
                .message("Invalid Cron Expression " + scheduleConfigRequestDTO.getCronExpression())
                .path("uri=/api/analytics/configureCron")
                .build();


        when(schedulerService.updateCronExpression(scheduleConfigRequestDTO)).thenThrow(new InvalidCronExpressionException("Invalid Cron Expression " + scheduleConfigRequestDTO.getCronExpression()));

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/configureCron")
                        .content("{\"cronExpression\":\"a 2 c aa 2 1\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ErrorResponseDTO.class);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(schedulerService, times(1)).updateCronExpression(scheduleConfigRequestDTO);
    }

    @Test
    @DisplayName("POST /INVALID-BODY ==> 400")
    void configFollowUpReminderScheduleShouldReturn400WhenRequestBodyValidationFails() throws Exception {
        ValidationErrorResponseDTO expected = ValidationErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .messages(List.of("Cron expression cannot be blank"))
                .path("uri=/api/analytics/configureCron")
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/api/analytics/configureCron")
                        .content("{\"cronExpression\":\"\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ValidationErrorResponseDTO actual = objectMapper.readValue(jsonResponse, ValidationErrorResponseDTO.class);
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getMessages(), actual.getMessages());
        verify(schedulerService, times(0)).updateCronExpression(any(ScheduleConfigRequestDTO.class));
    }

}
