package com.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main class for the Customer Data Management application.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CustomerDataManagementApplication {

	/**
	 * Main method to run the Spring Boot application.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CustomerDataManagementApplication.class, args);
	}
}