package com.crm.scheduler;

import com.crm.dto.ReportResponseDTO;
import com.crm.dto.external.NotificationDTO;
import com.crm.entities.EmailFormat;
import com.crm.enums.Type;
import com.crm.exception.InvalidDataRecievedException;
import com.crm.feign.Proxy;
import com.crm.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    private final ReportService service;
    private final Proxy proxy;

    @Autowired
    public SchedulerServiceImpl(ReportService service, Proxy proxy) {
        this.service = service;
        this.proxy = proxy;
    }

    @Override
    public List<NotificationDTO> sendNotifications() {
        log.info("STARTING CRON JOB -> SEND NOTIFICATIONS");
        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        try {
           //Generating reports
            ReportResponseDTO reportResponseDTOForSalesAutomation = service.generateSalesReport();
            ReportResponseDTO reportResponseDTOForCustomerDataManagement = service.generateCustomerReport();
            ReportResponseDTO reportResponseDTOForCustomerSupport = service.generateSupportReport();
            ReportResponseDTO reportResponseDTOForMarketingAutomation = service.generateMarketingReport();

            // Generate notifications
            notificationDTOS.add(createAndSendNotification(
                    "ANALYTICS REPORT FOR SALES-AUTOMATION",
                    "sailavanya1509@gmail.com",
                    generateEmailForSales(reportResponseDTOForSalesAutomation)
            ));

            notificationDTOS.add(createAndSendNotification(
                    "ANALYTICS REPORT FOR CUSTOMER-DATA-MANAGEMENT",
                    "sailavanya1509@gmail.com",
                    generateEmailForCustomers(reportResponseDTOForCustomerDataManagement)
            ));

            notificationDTOS.add(createAndSendNotification(
                    "ANALYTICS REPORT FOR CUSTOMER-SUPPORT",
                    "sailavanya1509@gmail.com",
                    generateEmailForSupport(reportResponseDTOForCustomerSupport)
            ));

            notificationDTOS.add(createAndSendNotification(
                    "ANALYTICS REPORT FOR MARKETING-AUTOMATION",
                    "sailavanya1509@gmail.com",
                    generateEmailForMarketing(reportResponseDTOForMarketingAutomation)
            ));

        } catch (JsonProcessingException e) {
            log.error("EMAIL SERVICE FAILED DUE TO JsonProcessingException with message -> {}", e.getMessage());
        } catch (InvalidDataRecievedException e) {
            log.error("EMAIL SERVICE FAILED DUE TO Invalid Data with message -> {}", e.getMessage());
        }

        return notificationDTOS;
    }

    private EmailFormat generateEmailForSales(ReportResponseDTO reportResponseDTO) {
        return EmailFormat.builder()
                .salutation("Dear Sales Team,")
                .openingLine("I hope this message finds you well.")
                .body("Here is the sales performance update:\n" +
                        "Opportunities In Progress: " + reportResponseDTO.getDataPoints().get("opportunitiesInProgress") + "\n" +
                        "Total Estimated Value: ₹" + reportResponseDTO.getDataPoints().get("totalEstimatedValue") + "\n" +
                        "Opportunities Won: ₹" + reportResponseDTO.getDataPoints().get("opportunitiesWon")  + "\n" +
                        "Opportunities Lost: ₹" + reportResponseDTO.getDataPoints().get("opportunitiesLost") + ".")
                .conclusion("Let's focus on converting more opportunities into wins!")
                .closing("Best regards,\nYour CRM System")
                .build();
    }

    private EmailFormat generateEmailForCustomers(ReportResponseDTO reportResponseDTO) {
        List<Map<String, Object>> top5Customers = (List<Map<String, Object>>) reportResponseDTO.getDataPoints().getOrDefault("top5Customers", new ArrayList<>());

        String topCustomersList = top5Customers.stream()
                .map(customer -> customer.get("name") + " - Purchases: " + customer.get("purchaseCount"))
                .collect(Collectors.joining("\n", "", ""));




        return EmailFormat.builder()
                .salutation("Dear Customer Data Management Team,")
                .openingLine("I hope this message finds you well.")
                .body("Here is the customer performance update:\n" +
                        "Total Customers: " + reportResponseDTO.getDataPoints().get("totalCustomers") + "\n" +
                        "Inactive Customers: " + reportResponseDTO.getDataPoints().get("inactiveCustomers") + "\n" +
                        "Top 5 Customers:\n" + topCustomersList)
                .conclusion("Let's focus on engaging with our customers and building even stronger relationships!")
                .closing("Best regards,\nYour CRM System")
                .build();
    }

    private EmailFormat generateEmailForSupport(ReportResponseDTO reportResponseDTO) {
        Map<String, Integer> top5Customers = (Map<String, Integer>) reportResponseDTO.getDataPoints().getOrDefault("top5Customers", new HashMap<>());
        String topCustomersList = top5Customers.entrySet().stream()
                .map(entry -> "Customer ID: " + entry.getKey() + " - Tickets: " + entry.getValue())
                .collect(Collectors.joining("\n", "", ""));

        Map<String, Integer> ticketsPerAgent = (Map<String, Integer>) reportResponseDTO.getDataPoints().getOrDefault("ticketsPerAgent", new HashMap<>());
        String ticketsPerAgentList = ticketsPerAgent.entrySet().stream()
                .map(entry -> "Agent ID: " + entry.getKey() + " - Tickets: " + entry.getValue())
                .collect(Collectors.joining("\n", "", ""));

        return EmailFormat.builder()
                .salutation("Dear Support Team,")
                .openingLine("I hope this message finds you well.")
                .body("Here is the support report update:\n" +
                        "Total Tickets: " + reportResponseDTO.getDataPoints().get("totalTickets") + "\n" +
                        "Open Tickets: " + reportResponseDTO.getDataPoints().get("openTickets") + "\n" +
                        "Closed Tickets: " + reportResponseDTO.getDataPoints().get("closedTickets") + "\n" +
                        "Tickets Per Agent:\n" + ticketsPerAgentList + "\n" +
                        "Top 5 Customers:\n" + topCustomersList)
                .conclusion("Let's continue delivering exceptional support service to our customers!")
                .closing("Best regards,\nYour CRM System")
                .build();
    }

    private EmailFormat generateEmailForMarketing(ReportResponseDTO reportResponseDTO) {
        Map<String, Integer> campaignsByType = (Map<String, Integer>) reportResponseDTO.getDataPoints().getOrDefault("campaignsByType", new HashMap<>());
        String campaignsTypeList = campaignsByType.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n", "", ""));

        return EmailFormat.builder()
                .salutation("Dear Marketing Team,")
                .openingLine("I hope this message finds you well.")
                .body("Here is the marketing report update:\n" +
                        "Total Campaigns: " + reportResponseDTO.getDataPoints().get("totalCampaigns") + "\n" +
                        "Total Customer Interactions: " + reportResponseDTO.getDataPoints().get("totalCustomerInteractions") + "\n" +
                        "Top Campaign: " + reportResponseDTO.getDataPoints().get("topCampaign") + "\n" +
                        "Completed Campaigns: " + reportResponseDTO.getDataPoints().get("completedCampaigns") + "\n" +
                        "Active Campaigns: " + reportResponseDTO.getDataPoints().get("activeCampaigns") + "\n" +
                        "Zero Interaction Campaigns: " + reportResponseDTO.getDataPoints().get("zeroInteractionCampaigns") + "\n" +
                        "Campaigns by Type:\n" + campaignsTypeList)
                .conclusion("Let's focus on maximizing customer engagement through our campaigns!")
                .closing("Best regards,\nYour CRM System")
                .build();
    }

    private NotificationDTO createAndSendNotification(String subject, String employeeID, EmailFormat emailFormat) {
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .type(Type.EMAIL)
                .subject(subject)
                .employeeID(employeeID)
                .emailFor("employee")
                .body(emailFormat.toString())
                .build();

        log.info("Sending Notification -> {}", subject);
        return proxy.sendNotificatonDummy(notificationDTO);
    }
}
