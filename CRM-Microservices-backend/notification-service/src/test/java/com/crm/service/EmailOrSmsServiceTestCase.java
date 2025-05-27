package com.crm.service;
import com.twilio.type.PhoneNumber;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.EmployeeDTO;
import com.crm.dto.NotificationDTO;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailOrSmsServiceTestCase {

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private MessageCreator messageCreator;
    @InjectMocks
    private EmailOrSmsServiceImpl emailOrSmsService;

    private CustomerProfileDTO customer;
    private NotificationDTO notification;
    private MimeMessage mimeMessage;
    private EmployeeDTO employee=new EmployeeDTO();

    @BeforeEach
    void setUpGeneral() {
        customer = CustomerProfileDTO.builder()
                .emailId("test@example.com")
                .phoneNumber("+917330783299")
                .build();

        EmployeeDTO employeeDTO=new EmployeeDTO();
        employeeDTO.setEmployeeEmail("sohithkalavakuri71@gmail.com");
        employeeDTO.setPhoneNumber("+917330783299");

        notification = NotificationDTO.builder()
        		.employeeID(employeeDTO.getEmployeeEmail())
                .subject("Test Subject")
                .body("Test Body")
                .build();

        ReflectionTestUtils.setField(emailOrSmsService, "sender", "sender@example.com");
        ReflectionTestUtils.setField(emailOrSmsService, "twilioPhoneNumber", "+13049028604");
        ReflectionTestUtils.setField(emailOrSmsService, "accountSid", "AC468c7d383893fe5b90a39b451a4907c1");
        ReflectionTestUtils.setField(emailOrSmsService, "authToken", "af3143ba8a50a26a8edacacf4f312028");
        emailOrSmsService.initTwilio();
    }

    void setUpEmail() {
        mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendEmail_Success() {
        setUpEmail();
        emailOrSmsService.sendEmail(customer, notification);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmailToEmployee_Success(){
        setUpEmail();
        emailOrSmsService.sendEmailToEmployee(notification, employee);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmail_Failure() {
        setUpEmail();
        doAnswer(invocation -> {
            throw new MessagingException("Email failed");
        }).when(mailSender).send(mimeMessage);

        emailOrSmsService.sendEmail(customer, notification);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmailToEmployee_Failure() {
    	setUpEmail();
        doAnswer(invocation -> {
            throw new MessagingException("Email failed");
        }).when(mailSender).send(mimeMessage);
        emailOrSmsService.sendEmailToEmployee(notification, employee);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendSms_Success() {
        try (MockedStatic<Message> mockedStatic = Mockito.mockStatic(Message.class)) {
            MessageCreator mockCreator = Mockito.mock(MessageCreator.class);
            Message message = Mockito.mock(Message.class);
            when(message.getSid()).thenReturn("SM12345");
            when(mockCreator.create()).thenReturn(message);
            mockedStatic.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())).thenReturn(mockCreator);

            assertTrue(emailOrSmsService.sendSms(customer, notification));
            verify(mockCreator, times(1)).create();
        }
    }

    @Test
    void testSendSms_Failure_ApiException() {
        try (MockedStatic<Message> mockedStatic = Mockito.mockStatic(Message.class)) {
            MessageCreator mockCreator = Mockito.mock(MessageCreator.class);
            when(mockCreator.create()).thenThrow(new ApiException("Twilio API error"));
            mockedStatic.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())).thenReturn(mockCreator);

            assertFalse(emailOrSmsService.sendSms(customer, notification));
            verify(mockCreator, times(1)).create();
        }
    }

    @Test
    void testSendSms_Failure_NullPhoneNumber() {
        CustomerProfileDTO customerWithNullPhone = CustomerProfileDTO.builder()
                .emailId("test@example.com")
                .phoneNumber(null)
                .build();

        assertFalse(emailOrSmsService.sendSms(customerWithNullPhone, notification));
    }
    @Test
    void testSendSmsToEmployee_Success() {
        try (MockedStatic<Message> mockedStatic = Mockito.mockStatic(Message.class)) {
            MessageCreator mockCreator = Mockito.mock(MessageCreator.class);
            Message message = Mockito.mock(Message.class);
            when(message.getSid()).thenReturn("SM12345");
            when(mockCreator.create()).thenReturn(message);
            mockedStatic.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())).thenReturn(mockCreator);

            assertTrue(emailOrSmsService.sendSmsToEmployee(notification, employee));
            verify(mockCreator, times(1)).create();
        }
    }

    
}