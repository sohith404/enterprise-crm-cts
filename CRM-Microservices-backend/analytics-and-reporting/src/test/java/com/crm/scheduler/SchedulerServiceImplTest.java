package com.crm.scheduler;
import com.crm.dto.ReportResponseDTO;
import com.crm.dto.external.NotificationDTO;
import com.crm.enums.Type;
import com.crm.exception.InvalidDataRecievedException;
import com.crm.feign.Proxy;
import com.crm.mapper.ReportMapper;
import com.crm.repository.ReportRepository;
import com.crm.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceImplTest {

    @Mock
    private Proxy proxy;

    @Mock
    private ReportService reportService;

    @Mock
    private ReportRepository repository;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private SchedulerServiceImpl service;

    private ReportResponseDTO salesReport;
    private ReportResponseDTO customerReport;
    private ReportResponseDTO supportReport;
    private ReportResponseDTO marketingReport;

    private void setupReportResponses() {
        salesReport = new ReportResponseDTO();
        salesReport.setDataPoints(Map.of("opportunitiesInProgress", 10, "totalEstimatedValue", 500000, "opportunitiesWon", 300000, "opportunitiesLost", 200000));
        customerReport = new ReportResponseDTO();
        customerReport.setDataPoints(Map.of("totalCustomers", 100, "inactiveCustomers", 20, "top5Customers", List.of(Map.of("name", "Customer1", "purchaseCount", 5), Map.of("name", "Customer2", "purchaseCount", 3))));
        supportReport = new ReportResponseDTO();
        supportReport.setDataPoints(Map.of("totalTickets", 50, "openTickets", 10, "closedTickets", 40, "ticketsPerAgent", Map.of("Agent1", 25, "Agent2", 25), "top5Customers", Map.of("Customer1", 10, "Customer2", 5)));
        marketingReport = new ReportResponseDTO();
        marketingReport.setDataPoints(Map.of("totalCampaigns", 10, "totalCustomerInteractions", 1000, "topCampaign", "Campaign1", "completedCampaigns", 5, "activeCampaigns", 5, "zeroInteractionCampaigns", 0, "campaignsByType", Map.of("Email", 5, "Social Media", 5)));
    }

    @Test
    void sendNotificationsShouldReturnFourNotifications() throws JsonProcessingException, InvalidDataRecievedException {
        setupReportResponses();

        when(reportService.generateSalesReport()).thenReturn(salesReport);
        when(reportService.generateCustomerReport()).thenReturn(customerReport);
        when(reportService.generateSupportReport()).thenReturn(supportReport);
        when(reportService.generateMarketingReport()).thenReturn(marketingReport);
        when(proxy.sendNotificatonDummy(any(NotificationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<NotificationDTO> notifications = service.sendNotifications();

        assertEquals(4, notifications.size());
    }

    @Test
    void sendNotificationsShouldCreateSalesNotification() throws JsonProcessingException, InvalidDataRecievedException {
        setupReportResponses();

        when(reportService.generateSalesReport()).thenReturn(salesReport);
        when(reportService.generateCustomerReport()).thenReturn(customerReport);
        when(reportService.generateSupportReport()).thenReturn(supportReport);
        when(reportService.generateMarketingReport()).thenReturn(marketingReport);
        when(proxy.sendNotificatonDummy(any(NotificationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<NotificationDTO> notifications = service.sendNotifications();

        assertEquals(Type.EMAIL, notifications.get(0).getType());
        assertEquals("ANALYTICS REPORT FOR SALES-AUTOMATION", notifications.get(0).getSubject());
        assertEquals("sailavanya1509@gmail.com", notifications.get(0).getEmployeeID());
        assertTrue(notifications.get(0).getBody().contains("Dear Sales Team,"));
    }

    @Test
    void sendNotificationsShouldCreateCustomerNotification() throws JsonProcessingException, InvalidDataRecievedException {
        setupReportResponses();

        when(reportService.generateSalesReport()).thenReturn(salesReport);
        when(reportService.generateCustomerReport()).thenReturn(customerReport);
        when(reportService.generateSupportReport()).thenReturn(supportReport);
        when(reportService.generateMarketingReport()).thenReturn(marketingReport);
        when(proxy.sendNotificatonDummy(any(NotificationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<NotificationDTO> notifications = service.sendNotifications();

        assertEquals(Type.EMAIL, notifications.get(1).getType());
        assertEquals("ANALYTICS REPORT FOR CUSTOMER-DATA-MANAGEMENT", notifications.get(1).getSubject());
        assertEquals("sailavanya1509@gmail.com", notifications.get(1).getEmployeeID());
        assertTrue(notifications.get(1).getBody().contains("Dear Customer Data Management Team,"));
    }

    @Test
    void sendNotificationsShouldCreateSupportNotification() throws JsonProcessingException, InvalidDataRecievedException {
        setupReportResponses();

        when(reportService.generateSalesReport()).thenReturn(salesReport);
        when(reportService.generateCustomerReport()).thenReturn(customerReport);
        when(reportService.generateSupportReport()).thenReturn(supportReport);
        when(reportService.generateMarketingReport()).thenReturn(marketingReport);
        when(proxy.sendNotificatonDummy(any(NotificationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<NotificationDTO> notifications = service.sendNotifications();

        assertEquals(Type.EMAIL, notifications.get(2).getType());
        assertEquals("ANALYTICS REPORT FOR CUSTOMER-SUPPORT", notifications.get(2).getSubject());
        assertEquals("sailavanya1509@gmail.com", notifications.get(2).getEmployeeID());
        assertTrue(notifications.get(2).getBody().contains("Dear Support Team,"));
    }

    @Test
    void sendNotificationsShouldCreateMarketingNotification() throws JsonProcessingException, InvalidDataRecievedException {
        setupReportResponses();

        when(reportService.generateSalesReport()).thenReturn(salesReport);
        when(reportService.generateCustomerReport()).thenReturn(customerReport);
        when(reportService.generateSupportReport()).thenReturn(supportReport);
        when(reportService.generateMarketingReport()).thenReturn(marketingReport);
        when(proxy.sendNotificatonDummy(any(NotificationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<NotificationDTO> notifications = service.sendNotifications();

        assertEquals(Type.EMAIL, notifications.get(3).getType());
        assertEquals("ANALYTICS REPORT FOR MARKETING-AUTOMATION", notifications.get(3).getSubject());
        assertEquals("sailavanya1509@gmail.com", notifications.get(3).getEmployeeID());
        assertTrue(notifications.get(3).getBody().contains("Dear Marketing Team,"));
    }

    @Test
    void sendNotificationsShouldCallDummyClassFourTimes() throws JsonProcessingException, InvalidDataRecievedException {
        setupReportResponses();

        when(reportService.generateSalesReport()).thenReturn(salesReport);
        when(reportService.generateCustomerReport()).thenReturn(customerReport);
        when(reportService.generateSupportReport()).thenReturn(supportReport);
        when(reportService.generateMarketingReport()).thenReturn(marketingReport);
        when(proxy.sendNotificatonDummy(any(NotificationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.sendNotifications();

        verify(proxy, times(4)).sendNotificatonDummy(any(NotificationDTO.class));
    }
}