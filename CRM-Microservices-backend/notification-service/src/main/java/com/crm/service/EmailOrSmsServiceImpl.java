package com.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.EmployeeDTO;
import com.crm.dto.NotificationDTO;
import com.crm.exception.NotificationNotFoundException;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Service implementation for sending emails and SMS notifications.
 * This service handles sending notifications to customers and employees via email and SMS.
 */
@Service
public class EmailOrSmsServiceImpl implements EmailOrSmsService {
    private static final Logger logger = LoggerFactory.getLogger(EmailOrSmsServiceImpl.class);
    private static final String EMAIL_SEND_ERROR_MESSAGE = "Error sending email to {}: {}";

    /**
     * Sender email address, configured via application properties.
     */
    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender mailSender;

    /**
     * Twilio account SID, configured via application properties.
     */
    @Value("${twilio.account_sid}")
    private String accountSid;

    /**
     * Twilio auth token, configured via application properties.
     */
    @Value("${twilio.auth_token}")
    private String authToken;

    /**
     * Twilio phone number, configured via application properties.
     */
    @Value("${twilio.phone_number}")
    private String twilioPhoneNumber;

    /**
     * Constructor for EmailOrSmsServiceImpl.
     *
     * @param mailSender The JavaMailSender used for sending emails.
     */
    public EmailOrSmsServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Initializes Twilio with the account SID and auth token.
     * This method is called after dependency injection is done.
     */
    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    /**
     * Sends an email to a customer.
     *
     * @param customer        The customer profile containing the email address.
     * @param notificationDTO The notification details including subject and body.
     */
    @Override
    public void sendEmail(CustomerProfileDTO customer, NotificationDTO notificationDTO) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(notificationDTO.getSubject());
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(customer.getEmailId());
            mimeMessageHelper.setText(notificationDTO.getBody());
            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error(EMAIL_SEND_ERROR_MESSAGE, customer.getEmailId(), e.getMessage(), e);
        }
    }

    /**
     * Sends an email to an employee.
     *
     * @param notificationDTO The notification details including subject and body.
     * @param employeeDTO     The employee profile containing the email address.
     */
    @Override
    public void sendEmailToEmployee(NotificationDTO notificationDTO, EmployeeDTO employeeDTO) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(notificationDTO.getSubject());
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(notificationDTO.getEmployeeID());
            mimeMessageHelper.setText(notificationDTO.getBody());
            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error(EMAIL_SEND_ERROR_MESSAGE, employeeDTO.getEmployeeEmail(), e.getMessage(), e);
        }
    }

    /**
     * Sends an SMS to a customer.
     *
     * @param customer        The customer profile containing the phone number.
     * @param notificationDTO The notification details including the message body.
     * @throws NotificationNotFoundException if an error occurs while sending the SMS.
     */
    @Override
    public boolean sendSms(CustomerProfileDTO customer, NotificationDTO notificationDTO) {
        if (customer.getPhoneNumber() == null) {
            logger.error("Customer phone number is null.");
            return false;
        }

        try {
            Message message = Message.creator(
                    new PhoneNumber(customer.getPhoneNumber()),
                    new PhoneNumber(twilioPhoneNumber),
                    notificationDTO.getSubject())
                    .create();
            logger.info("SMS sent with SID: {}, recipient: {}", message.getSid(), customer.getPhoneNumber());
            return true;
        } catch (ApiException e) {
            logger.error("Error sending SMS to: {}", customer.getPhoneNumber(), e);
        }
        return false;
    }

    /**
     * Sends an SMS to an employee.
     *
     * @param notificationDTO The notification details including the message body.
     * @param employeeDTO     The employee profile containing the phone number.
     * @throws NotificationNotFoundException if an error occurs while sending the SMS.
     */
    @Override
    public boolean sendSmsToEmployee(NotificationDTO notificationDTO, EmployeeDTO employeeDTO) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(employeeDTO.getPhoneNumber()),
                    new PhoneNumber(twilioPhoneNumber),
                    notificationDTO.getSubject())
                    .create();
            System.out.println( message.getSid());
            logger.info("SMS sent with SID: {}, recipient: {}", message.getSid(), employeeDTO.getPhoneNumber());
            return true;
        } catch (NotificationNotFoundException e) {
            logger.error("Error sending SMS to: {}", employeeDTO.getPhoneNumber(), e);
        }
        return false;
    }
}