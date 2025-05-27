package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.EmployeeDTO;
import com.crm.dto.NotificationDTO;

public interface EmailOrSmsService  {
	public void sendEmail(CustomerProfileDTO customer, NotificationDTO notificationDTO);
	public void sendEmailToEmployee( NotificationDTO notificationDTO,EmployeeDTO employeeDTO);
	public boolean sendSms(CustomerProfileDTO customer,NotificationDTO notificationDTO);
	public boolean sendSmsToEmployee(NotificationDTO notificationDTO, EmployeeDTO employeeDTO);
}
