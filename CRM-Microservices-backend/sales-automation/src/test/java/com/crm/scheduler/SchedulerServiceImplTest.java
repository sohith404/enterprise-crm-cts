package com.crm.scheduler;

import com.crm.dto.NotificationDTO;
import com.crm.entities.EmailFormat;
import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import com.crm.feign.Proxy;
import com.crm.mapper.SalesOpportunityMapper;
import com.crm.repository.SalesOpportunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceImplTest {

    @Mock
    private SalesOpportunityRepository repository;

    @Mock
    private SalesOpportunityMapper mapper;

    @Mock
    private Proxy proxy;


    @InjectMocks
    private SchedulerServiceImpl service;

    private static List<SalesOpportunity> list;


    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of(
                SalesOpportunity.builder()
                        .opportunityID(1L)
                        .customerID(1L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2025, Month.MAY, 18))
                        .followUpReminder(LocalDate.now())
                        .build(),
                SalesOpportunity.builder()
                        .opportunityID(2L)
                        .customerID(2L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2025, Month.MAY, 18))
                        .followUpReminder(LocalDate.now())
                        .build(),
                SalesOpportunity.builder()
                        .opportunityID(3L)
                        .customerID(3L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2025, Month.MAY, 18))
                        .followUpReminder(LocalDate.now())
                        .build()
        ));
    }


    @Test
    void sendNotificationsShouldReturnListOfSentFollowUpReminderWhenFollowUpRemindersExist() {
        // Create EmailFormat and NotificationDTO objects for testing
        EmailFormat email1 = EmailFormat.builder()
                .salutation("Dear employee,")
                .openingLine("I hope this message finds you well.")
                .body("This is to inform you that Lead #" + 1L + " is closing on " + list.getFirst().getClosingDate() + ".")
                .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                .closing("SALES-AUTOMATION SERVICE \n CRM")
                .build();
        NotificationDTO obj1 = NotificationDTO.builder()
                .subject("Follow-up Reminder for Sales Lead with ID " + 1L)
                .body(email1.toString())
                .emailFor("employee")
                .employeeID("saharshraj10@gmail.com")
                .build();
        obj1.setStatus("SENT");

        EmailFormat email2 = EmailFormat.builder()
                .salutation("Dear employee,")
                .openingLine("I hope this message finds you well.")
                .body("This is to inform you that Lead #" + 2L + " is closing on " + list.get(1).getClosingDate() + ".")
                .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                .closing("SALES-AUTOMATION SERVICE \n CRM")
                .build();
        NotificationDTO obj2 = NotificationDTO.builder()
                .subject("Follow-up Reminder for Sales Lead with ID " + 2L)
                .body(email2.toString())
                .emailFor("employee")
                .employeeID("saharshraj10@gmail.com")
                .build();
        obj2.setStatus("SENT");

        EmailFormat email3 = EmailFormat.builder()
                .salutation("Dear employee,")
                .openingLine("I hope this message finds you well.")
                .body("This is to inform you that Lead #" + 3L + " is closing on " + list.get(2).getClosingDate() + ".")
                .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                .closing("SALES-AUTOMATION SERVICE \n CRM")
                .build();
        NotificationDTO obj3 = NotificationDTO.builder()
                .subject("Follow-up Reminder for Sales Lead with ID " + 3L)
                .body(email3.toString())
                .emailFor("employee")
                .employeeID("saharshraj10@gmail.com")
                .build();
        obj3.setStatus("SENT");

        List<NotificationDTO> expected = Arrays.asList(obj1, obj2, obj3);

        // Stubbing the repository and dummyClass methods
        when(repository.findByFollowUpReminder(LocalDate.now())).thenReturn(list);
        when(proxy.sendNotificaton(any(NotificationDTO.class))).thenAnswer(invocation -> {
            NotificationDTO arg = invocation.getArgument(0);
            arg.setStatus("SENT");
            return arg;
        });

        // Call the service method
        List<NotificationDTO> actual = service.sendFollowUpReminder();

        // Verify the results
        assertEquals(expected, actual);
        verify(proxy).sendNotificaton(argThat(dto -> dto.getSubject().equals(obj1.getSubject())));
        verify(proxy).sendNotificaton(argThat(dto -> dto.getSubject().equals(obj2.getSubject())));
        verify(proxy).sendNotificaton(argThat(dto -> dto.getSubject().equals(obj3.getSubject())));
    }


    @Test
    void closeLeadsShouldReturnListOfSentFollowUpReminderWhenLeadsExist() {
        // Create EmailFormat and NotificationDTO objects for testing
        EmailFormat email1 = EmailFormat.builder()
                .salutation("Dear employee,")
                .openingLine("I hope this message finds you well.")
                .body("This is to inform you that Lead #" + 1L + " is closed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ".")
                .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                .closing("SALES-AUTOMATION SERVICE \n CRM")
                .build();
        NotificationDTO obj1 = NotificationDTO.builder()
                .subject("Status updated for Sales Lead with ID " + 1L)
                .body(email1.toString())
                .emailFor("employee")
                .employeeID("saharshraj10@gmail.com")
                .build();
        obj1.setStatus("SENT");

        EmailFormat email2 = EmailFormat.builder()
                .salutation("Dear employee,")
                .openingLine("I hope this message finds you well.")
                .body("This is to inform you that Lead #" + 2L + " is closed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ".")
                .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                .closing("SALES-AUTOMATION SERVICE \n CRM")
                .build();
        NotificationDTO obj2 = NotificationDTO.builder()
                .subject("Status updated for Sales Lead with ID " + 2L)
                .body(email2.toString())
                .emailFor("employee")
                .employeeID("saharshraj10@gmail.com")
                .build();
        obj2.setStatus("SENT");

        EmailFormat email3 = EmailFormat.builder()
                .salutation("Dear employee,")
                .openingLine("I hope this message finds you well.")
                .body("This is to inform you that Lead #" + 3L + " is closed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ".")
                .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                .closing("SALES-AUTOMATION SERVICE \n CRM")
                .build();
        NotificationDTO obj3 = NotificationDTO.builder()
                .subject("Status updated for Sales Lead with ID " + 3L)
                .body(email3.toString())
                .emailFor("employee")
                .employeeID("saharshraj10@gmail.com")
                .build();
        obj3.setStatus("SENT");

        List<NotificationDTO> expected = Arrays.asList(obj1, obj2, obj3);

        // Stubbing the repository and dummyClass methods
        when(repository.findByClosingDate(LocalDate.now())).thenReturn(list);
        when(proxy.sendNotificaton(any(NotificationDTO.class))).thenAnswer(invocation -> {
            NotificationDTO arg = invocation.getArgument(0);
            arg.setStatus("SENT");
            return arg;
        });

        // Call the service method
        List<NotificationDTO> actual = service.sendClosingNotification();

        // Verify the results
        assertEquals(expected, actual);
        verify(proxy).sendNotificaton(argThat(dto -> dto.getSubject().equals(obj1.getSubject())));
        verify(proxy).sendNotificaton(argThat(dto -> dto.getSubject().equals(obj2.getSubject())));
        verify(proxy).sendNotificaton(argThat(dto -> dto.getSubject().equals(obj3.getSubject())));
    }
}