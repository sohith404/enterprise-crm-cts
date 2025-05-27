package com.crm.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.crm.entities.CustomerProfile;

@DataJpaTest
@ActiveProfiles("test")
class CustomerProfileRepositoryTestCase {

	private CustomerProfile customerProfile;

	@Autowired
	private CustomerProfileRepository customerProfileRepository;

	@BeforeEach
	void setUp() throws Exception {
		customerProfile = CustomerProfile.builder().name("John Doe")
				.phoneNumber("9998887777").emailId("john.doe@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		customerProfileRepository.deleteAll();
		customerProfile = null;
	}

	@Test
	@DisplayName("Test adding customer profile - Positive case")
	void testAddCustomerProfile_positive() {
		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		assertTrue(savedCustomerProfile.getCustomerID() > 0, "Customer ID should be generated");
	}

	@Test
	@DisplayName("Test adding customer profile - Negative case")
	void testAddCustomerProfile_Negative() {
		assertThrows(Exception.class, () -> customerProfileRepository.save(null));
	}

	@Test
	@DisplayName("Test finding customer profile by ID - Positive case")
	void testFindCustomerProfileById_Positive() {
		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findById(savedCustomerProfile.getCustomerID());
		assertTrue(optionalOfCustomerProfile.isPresent(), "Customer should be found by ID");
	}

	@Test
	@DisplayName("Test finding customer profile by ID - Negative case")
	void testFindCustomerProfileById_Negative() {
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository.findById(1L);
		assertTrue(optionalOfCustomerProfile.isEmpty(), "Customer with ID 1 should not exist");
	}

	@Test
	@DisplayName("Test finding all customer profiles - Positive case")
	void testFindAllCustomerProfile_Positive() {
		customerProfileRepository.save(customerProfile);
		List<CustomerProfile> listOfCustomerProfile = customerProfileRepository.findAll();
		assertFalse(listOfCustomerProfile.isEmpty(), "List of customers should not be empty");
	}

	@Test
	@DisplayName("Test finding all customer profiles - Negative case")
	void testFindAllCustomerProfile_Negative() {
		List<CustomerProfile> listOfCustomerProfile = customerProfileRepository.findAll();
		assertTrue(listOfCustomerProfile.isEmpty(), "List of customers should be empty");
	}

	@Test
	@DisplayName("Test deleting customer profile by ID - Positive case")
	void testDeleteCustomerProfileById_Positive() {
		CustomerProfile savedCustomerProfile = customerProfileRepository.save(customerProfile);
		customerProfileRepository.deleteById(savedCustomerProfile.getCustomerID());
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findById(savedCustomerProfile.getCustomerID());
		assertTrue(optionalOfCustomerProfile.isEmpty(), "Customer should be deleted");
	}

	@Test
	@DisplayName("Test deleting customer profile by ID - Negative case")
	void testDeleteCustomerProfileById_Negative() {
		assertThrows(Exception.class, () -> customerProfileRepository.deleteById(null));
	}

	@Test
	@DisplayName("Test finding customer by email - Positive case")
	void testFindByEmail_Positive() {
		customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByEmail(customerProfile.getEmailId());
		assertTrue(optionalOfCustomerProfile.isPresent(), "Customer should be found by email");
	}

	@Test
	@DisplayName("Test finding customer by email - Negative case")
	void testFindByEmail_Negative() {
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByEmail("nonexistent@example.com");
		assertFalse(optionalOfCustomerProfile.isPresent(), "Customer should not be found by email");
	}

	@Test
	@DisplayName("Test finding customer by contact number - Positive case")
	void testFindByContactNumber_Positive() {
		customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByContactNumber(customerProfile.getPhoneNumber());
		assertTrue(optionalOfCustomerProfile.isPresent(), "Customer should be found by phone number");
	}

	@Test
	@DisplayName("Test finding customer by contact number - Negative case")
	void testFindByContactNumber_Negative() {
		Optional<CustomerProfile> optionalOfCustomerProfile = customerProfileRepository
				.findByContactNumber("0000000000");
		assertFalse(optionalOfCustomerProfile.isPresent(), "Customer should not be found by phone number");
	}

	@Test
	@DisplayName("Test finding customer by name containing - Positive case")
	void testFindByName_Positive() {
		customerProfileRepository.save(customerProfile);
		Optional<CustomerProfile> customerProfiles = customerProfileRepository.findByNameContaining("Doe");
		assertFalse(customerProfiles.isEmpty(), "Customer should be found by name containing");
		assertTrue(customerProfiles.stream().anyMatch(c -> c.getName().equals("John Doe")), "Customer name should match");
	}

	@Test
	@DisplayName("Test finding customer by name containing - Negative case")
	void testFindByName_Negative() {
		Optional<CustomerProfile> customerProfiles = customerProfileRepository.findByNameContaining("Smith");
		assertTrue(customerProfiles.isEmpty(), "Customer should not be found by name containing");
	}

	@Test
	@DisplayName("Test finding all customers by email - Positive case")
	void testFindAllByEmail_Positive() {
		customerProfileRepository.save(customerProfile);
		CustomerProfile customer2 = CustomerProfile.builder().name("SuvaLakshmi")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		customerProfileRepository.save(customer2);
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("2223334445").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}")
				.build();
		customerProfileRepository.save(customer3);
		List<CustomerProfile> customerList = customerProfileRepository.findAllByEmailId("Suva2@example.com");
		assertEquals(2, customerList.size(), "Two customers should be found by email");
	}

	@Test
	@DisplayName("Test finding all customers by email - Negative case")
	void testFindAllByEmail_Negative() {
		List<CustomerProfile> customerList = customerProfileRepository.findAllByEmailId("Suva2@example.com");
		assertEquals(0, customerList.size(), "No customers should be found by email");
	}

	@Test
	@DisplayName("Test finding all customers by phone number - Positive case")
	void testFindAllPhoneNumber_Positive() {
		customerProfileRepository.save(customerProfile);
		CustomerProfile customer2 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Thamz@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}").build();
		customerProfileRepository.save(customer2);
		CustomerProfile customer3 = CustomerProfile.builder().name("Suvalakshimi")
				.phoneNumber("7776665553").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}").build();
		customerProfileRepository.save(customer3);
		List<CustomerProfile> customerList = customerProfileRepository.findAllByPhoneNumber("7776665552");
		assertEquals( 1,customerList.size());
	}

	@Test
	@DisplayName("Test finding all customers by phone number - Negative case")
	void testFindAllPhoneNumber_Negative() {
		List<CustomerProfile> CustomerList = customerProfileRepository.findAllByPhoneNumber("7776665552");
		assertEquals(CustomerList.size(), 0);
	}

	@Test
	@DisplayName("Test finding all customers by name - Positive case")
	void testFindAllByName_Positive() {
		customerProfileRepository.save(customerProfile);
		CustomerProfile customer2 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}").build();
		customerProfileRepository.save(customer2);
		CustomerProfile customer3 = CustomerProfile.builder().name("Thamizhini")
				.phoneNumber("7776665552").emailId("Suva2@example.com")
				.purchaseHistory(Arrays.asList("Item1", "Item2"))
				.segmentationData("{\"segmentationData\": {\"Region\": \"NORTH\", \"Interest\": \"SPORTS\", \"PurchasingHabits\": \"NEW\"}}").build();
		customerProfileRepository.save(customer3);
		List<CustomerProfile> CustomerList = customerProfileRepository.findAllByNames("Thamizhini");
		assertEquals(CustomerList.size(), 2);
	}

	@Test
	@DisplayName("Test finding all customers by name - Negative case")
	void testFindAllByName_Negative() {
		List<CustomerProfile> CustomerList = customerProfileRepository.findAllByNames("Thamizhini");
		assertEquals(CustomerList.size(), 0);
	}


}
