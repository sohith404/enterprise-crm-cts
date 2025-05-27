package com.crm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.crm.dto.NotificationDTO;
import com.crm.service.NotificationService;
@ExtendWith(MockitoExtension.class)
class NotificationControllerTestCase {
	
	
	@Mock
    private NotificationService service;

    @InjectMocks
    private NotificationControllerImpl controller;

    private NotificationDTO notificationDTO;
   
    

    @BeforeEach
    void setUp() {
        notificationDTO = new NotificationDTO();
        notificationDTO.setBody("Test Notification");
        }

    @Test
    void sendNotification_success() {
    	when(service.sendNotification(notificationDTO)).thenReturn(notificationDTO);
        ResponseEntity<NotificationDTO> response = controller.sendNotification(notificationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notificationDTO, response.getBody());
        verify(service, times(1)).sendNotification(notificationDTO);
    }

    @Test
    void sendNotification_serviceException_badRequest() {

    	when(service.sendNotification(notificationDTO)).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<NotificationDTO> response = controller.sendNotification(notificationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(service, times(1)).sendNotification(notificationDTO);
    } 
    @Test
    void sendNotification_nullNotificationDto_badRequest() {
    	ResponseEntity<NotificationDTO> response = controller.sendNotification(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(service, never()).sendNotification(any());
    }
    
    
}
