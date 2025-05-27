package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.EmployeeDTO;
import com.crm.dto.NotificationDTO;
import com.crm.enums.Status;
import com.crm.enums.Type;
import com.crm.exception.NotificationNotFoundException;
import com.crm.feign.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for handling notifications.
 * This service is responsible for sending email and SMS notifications to both customers and employees.
 */
@Service
public class NotificationServiceImpl implements NotificationService {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
	private static final String EMAIL_SEND_ERROR_MESSAGE ="Error While Sending the message: {}";
	private final Proxy proxy;
    private final EmailOrSmsService emailOrSmsService;
    private EmployeeDTO employeeDTO=new EmployeeDTO();
    /** 
     * Constructor for NotificationServiceImpl.
     *
     * @param proxy Service to fetch customer profiles from an external source.
     * @param emailOrSmsService      Service to send emails and SMS notifications.
     */
    public NotificationServiceImpl(Proxy proxy, EmailOrSmsService emailOrSmsService) {
    	this.proxy = proxy;
		this.emailOrSmsService = emailOrSmsService;
    }
    /**
     * Sends a notification to either employees or customers based on the provided {@link NotificationDTO}. 
     * 
     * @param  notificationDTO The notification details including recipient type, message, and notification type. 
     * @return The processed {@link NotificationDTO} with updated status. 
     * @throws NotificationNotFoundException if notification data is invalid.
     */
	@Override
	public NotificationDTO sendNotification(NotificationDTO notificationDTO) {
	    validateNotificationDTO(notificationDTO);

	    if ("employee".equalsIgnoreCase(notificationDTO.getEmailFor())) {
	        processEmployeeNotification(notificationDTO);
	    } else {
	        processCustomerNotifications(notificationDTO);
	    }

	    return notificationDTO;
	}
	/**
     * Validates the given notification data.
     *
     * @param notificationDTO The notification data to be validated.
     * @throws NotificationNotFoundException if the notification body is null or invalid.
     */
	private void validateNotificationDTO(NotificationDTO notificationDTO) {
		if (notificationDTO == null || notificationDTO.getBody() == null) {
	        throw new NotificationNotFoundException("Invalid Notification Data");
	    }
	}
	/** 
	 * Processes and sends notifications to all customers. 
	 * 
	 * @param notificationDTO The notification data containing message details and type (EMAIL/SMS). 
	 * @throws NotificationNotFoundException if no customers are found or an invalid type is provided. 
	 */
	private void processCustomerNotifications(NotificationDTO notificationDTO) {
		ResponseEntity<List<CustomerProfileDTO>> response = proxy.getAllCustomerProfiles();
	    List<CustomerProfileDTO> allCustomers = response.getBody();

	    if (allCustomers == null || allCustomers.isEmpty()) {
	        throw new NotificationNotFoundException("No Customers found from external service");
	    }

	    for (CustomerProfileDTO customer:allCustomers) {
	        if (notificationDTO.getType() == Type.EMAIL) {
	        	try {
	    			emailOrSmsService.sendEmail(customer,notificationDTO);
	    			notificationDTO.setStatus(Status.SENT);
	    			
	    		}catch(Exception e) {
	    			notificationDTO.setStatus(Status.FAILED);
	    			logger.error(EMAIL_SEND_ERROR_MESSAGE, e.getMessage(), e);
	    		}
	        }
	        else if (notificationDTO.getType() == Type.SMS) {
	        	try {
	    			 boolean res=emailOrSmsService.sendSms(customer,notificationDTO);
	    			if(res) {
	    			notificationDTO.setStatus(Status.SENT);}
	    			
	    		}catch(NotificationNotFoundException e) {
	    			notificationDTO.setStatus(Status.FAILED);
	    			logger.error(EMAIL_SEND_ERROR_MESSAGE, e.getMessage(), e);
	    		}
	        } 
	        else {
	            throw new NotificationNotFoundException("Invalid Type of notification");
	        }
	    }
	}
	/** 
	 * Processes and sends a notification to an employee. 
	 * 
	 * @param notificationDTO The notification data containing message details and type (EMAIL/SMS). 
	 * @throws NotificationNotFoundException if an invalid type is provided. 
	 */
	private void processEmployeeNotification(NotificationDTO notificationDTO) {
	    if (notificationDTO.getEmployeeID()!= null) {
	    	employeeDTO.setEmployeeEmail(notificationDTO.getEmployeeID());
	        if (notificationDTO.getType() == Type.EMAIL) {
	        	try {
	    			emailOrSmsService.sendEmailToEmployee(notificationDTO,employeeDTO);
	    			notificationDTO.setStatus(Status.SENT);
	    		}catch(Exception e) {
	    			notificationDTO.setStatus(Status.FAILED);
	    			logger.error(EMAIL_SEND_ERROR_MESSAGE, e.getMessage(), e);
	    		}
	        } 
	        else if (notificationDTO.getType() == Type.SMS) {
	        	try {
	    			boolean res=emailOrSmsService.sendSmsToEmployee(notificationDTO,employeeDTO);
	    			if(res) {
	    			notificationDTO.setStatus(Status.SENT);
	    			}
	    		}catch(NotificationNotFoundException e) {
	    			notificationDTO.setStatus(Status.FAILED);
	    			logger.error(EMAIL_SEND_ERROR_MESSAGE, e.getMessage(), e);
	    		}
	        } 
	        else {
	            throw new NotificationNotFoundException("Invalid Type of notification");
	        }
	    }
		
	}
}
