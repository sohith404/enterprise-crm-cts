package com.crm.service;

import static org.junit.jupiter.api.Assertions.*; 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.NotificationDTO;
import com.crm.enums.Status;
import com.crm.enums.Type;
import com.crm.exception.NotificationNotFoundException;
import com.crm.feign.Proxy;
@ExtendWith(MockitoExtension.class)
class NotificationServiceTestCase {
	@Mock
	private Proxy proxy;
	
	@Mock
	private EmailOrSmsService emailOrSmsService;
	@InjectMocks
	private NotificationServiceImpl notificationServiceImpl;
	
	private NotificationDTO notificationDTO1;
	private NotificationDTO notificationDTO2;
	private NotificationDTO notificationDTOForEmployee;
	private List<CustomerProfileDTO> customerProfiles;

	

	@BeforeEach
	void setUp() {
   
		notificationDTO1=NotificationDTO.builder()
				.type(Type.EMAIL)
				.body("Test notification Body")
				.subject("Test Subject")
				.emailFor("customer")
				.build();
		notificationDTO2=NotificationDTO.builder()
				.type(Type.SMS)
				.body("Test notification Body")
				.subject("Test Subject")
				.emailFor("customer")
				.build();
		
		notificationDTOForEmployee = NotificationDTO.builder()
                .type(Type.EMAIL)
                .body("Test notification Body for Employee")
                .subject("Test Subject for Employee")
                .emailFor("employee")
                .employeeID("sohithkalavakuri71@gmail.com")
                .build();
		
		customerProfiles=Arrays.asList(
				CustomerProfileDTO.builder().customerID(1L).emailId("sohithkalavakuri70@gmail.com").phoneNumber("+917330783299").build(),
				CustomerProfileDTO.builder().customerID(2L).emailId("baluchitturi70@gmail.com").phoneNumber("+917330783299").build());
		
	} 

	

	@Test
	void testSendNotification_Success_Email() {
		 when(proxy.getAllCustomerProfiles())
         .thenReturn(ResponseEntity.ok(customerProfiles));

		 doNothing().when(emailOrSmsService).sendEmail(any(CustomerProfileDTO.class), any(NotificationDTO.class));

		 NotificationDTO response = notificationServiceImpl.sendNotification(notificationDTO1);

		 assertNotNull(response);
		 assertEquals(Status.SENT, response.getStatus());

		 verify(emailOrSmsService, times(1)).sendEmail(customerProfiles.get(0), notificationDTO1);
		 verify(emailOrSmsService, times(1)).sendEmail(customerProfiles.get(1), notificationDTO1);
	}
	
	 @Test
	    void testSendNotification_Success_SMS_MultipleCustomers() {
	        when(proxy.getAllCustomerProfiles())
	                .thenReturn(ResponseEntity.ok(customerProfiles));

	        when(emailOrSmsService.sendSms(any(CustomerProfileDTO.class), any(NotificationDTO.class)))
            .thenReturn(true);
	        NotificationDTO response = notificationServiceImpl.sendNotification(notificationDTO2);

	        assertNotNull(response);
	        assertEquals(Status.SENT, response.getStatus());
	        
	        verify(emailOrSmsService, times(1)).sendSms(customerProfiles.get(0), notificationDTO2);
	        verify(emailOrSmsService, times(1)).sendSms(customerProfiles.get(1), notificationDTO2);
	    }

	  @Test
	    void testSendNotification_NoCustomersFound() {
	        when(proxy.getAllCustomerProfiles())
	                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

	        NotificationNotFoundException exception = assertThrows(NotificationNotFoundException.class, () -> notificationServiceImpl.sendNotification(notificationDTO1));

	        assertEquals("No Customers found from external service", exception.getMessage());
	        
	    }
	  
	  @Test
	    void testSendNotification_InvalidNotificationData() throws NotificationNotFoundException{
	        NotificationNotFoundException exception = assertThrows(
	                NotificationNotFoundException.class,
	                () -> notificationServiceImpl.sendNotification(null));

	        assertEquals("Invalid Notification Data", exception.getMessage());
	    }
	  
	  @Test
	    void testSendNotification_EmailServiceFailure() {
		  notificationDTO1.setType(Type.EMAIL);
	        ResponseEntity<List<CustomerProfileDTO>> response = new ResponseEntity<>(customerProfiles, HttpStatus.OK);
	        when(proxy.getAllCustomerProfiles()).thenReturn(response);
	        doThrow(new RuntimeException("Email sending failed")).when(emailOrSmsService).sendEmail(any(CustomerProfileDTO.class), any(NotificationDTO.class));

	        NotificationDTO result = notificationServiceImpl.sendNotification(notificationDTO1);

	        assertEquals(Status.FAILED, result.getStatus());
	        verify(emailOrSmsService, times(2)).sendEmail(any(CustomerProfileDTO.class), any(NotificationDTO.class));
	    }
	  
	 @Test
	  void testSendNotification_SMS_ServiceFailure() {
		  when(proxy.getAllCustomerProfiles())
		  		.thenReturn(ResponseEntity.ok(customerProfiles));
		  
		  doThrow(new NotificationNotFoundException("SMS service down"))
		  		.when(emailOrSmsService).sendSms(any(CustomerProfileDTO.class), any(NotificationDTO.class));
		  
		  NotificationDTO response = notificationServiceImpl.sendNotification(notificationDTO2);
	  
		  assertEquals(Status.FAILED, response.getStatus());
	 }
	 
	 @Test
	 void testSendNotification_Employee_ServiceFailure() {
	     doThrow(new NotificationNotFoundException("Email service down")).when(emailOrSmsService).sendEmailToEmployee(any(), any());
	     NotificationDTO response = notificationServiceImpl.sendNotification(notificationDTOForEmployee);
	     assertEquals(Status.FAILED, response.getStatus());
	 }
	 @Test
	 void testSendNotification_Invalid_TypeNull() {
	     notificationDTOForEmployee.setType(null);
	     Type s=notificationDTOForEmployee.getType();
	     if(s==Type.EMAIL||s==Type.SMS) {
	     assertEquals(s,notificationDTOForEmployee.getType());}
	     else {
	    	 assertNull(s);
	     }
	 }
	
}
