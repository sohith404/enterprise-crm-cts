package com.crm.controller;

import com.crm.dto.NotificationDTO;
import com.crm.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling notification-related requests.
 */
@RestController
public class NotificationControllerImpl implements NotificationController {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationControllerImpl.class);
    private final NotificationService service;
    
    /**
     * Constructs a new NotificationControllerImpl with the specified NotificationService.
     *
     * @param service The NotificationService to use for sending notifications.
     */
    
    public NotificationControllerImpl(NotificationService service) {
        this.service = service;
    }
    
    /**
     * Sends a notification based on the provided NotificationDTO.
     *
     * @param notificationDTO The NotificationDTO containing the notification details.
     * @return ResponseEntity with a success message or a BAD_REQUEST status if an error occurs.
     */
    
   @Override
    public ResponseEntity<NotificationDTO> sendNotification(@RequestBody NotificationDTO notificationDTO) {
	   	 if (notificationDTO == null) {
             return ResponseEntity.badRequest().build();
         }
        try {
            NotificationDTO savedNotification = service.sendNotification(notificationDTO);
            logger.info("Notification sent successfully: {}", savedNotification);
            return new ResponseEntity<>(savedNotification, HttpStatus.OK);
        } catch (Exception e) {
        	logger.error("Error occurred", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
	
	
}
