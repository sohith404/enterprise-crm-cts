package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * Service interface for managing customer profiles.
 */
public interface CustomerService {

	/**
	 * Adds a new customer profile.
	 *
	 * @param customerProfileDTO the customer profile to add
	 * @return the added customer profile
	 * @throws ResourceNotFoundException if the customer profile cannot be added
	 */
	public CustomerProfileDTO addCustomerProfile(CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException;

	/**
	 * Retrieves all customer profiles.
	 *
	 * @return a list of all customer profiles
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> retrieveAllProfiles() throws ResourceNotFoundException;

	/**
	 * Retrieves a customer profile by ID.
	 *
	 * @param customerId the ID of the customer profile to retrieve
	 * @return the retrieved customer profile
	 * @throws ResourceNotFoundException if the customer profile is not found
	 */
	public CustomerProfileDTO getCustomerById(Long customerId) throws ResourceNotFoundException;

	/**
	 * Updates a customer profile.
	 *
	 * @param customerId the ID of the customer profile to update
	 * @param customerProfileDTO the updated customer profile
	 * @return the updated customer profile
	 * @throws ResourceNotFoundException if the customer profile is not found
	 */
	public CustomerProfileDTO updateCustomer(Long customerId, CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException;

	/**
	 * Deletes a customer profile.
	 *
	 * @param customerId the ID of the customer profile to delete
	 * @throws ResourceNotFoundException if the customer profile is not found
	 */
	public void deleteCustomer(Long customerId) throws ResourceNotFoundException;

	/**
	 * Updates the purchasing habit of a customer profile.
	 *
	 * @param customerId the ID of the customer profile to update
	 * @return the updated customer profile
	 * @throws ResourceNotFoundException if the customer profile is not found
	 */
	public CustomerProfileDTO updatePurchasingHabit(Long customerId) throws ResourceNotFoundException, JsonProcessingException;

	/**
	 * Searches customer profiles based on email ID.
	 *
	 * @param email the email ID to search for
	 * @return a list of customer profiles with the specified email ID
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnEmailId(String email) throws ResourceNotFoundException;

	/**
	 * Searches customer profiles based on name.
	 *
	 * @param name the name to search for
	 * @return a list of customer profiles with the specified name
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnName(String name) throws ResourceNotFoundException;

	/**
	 * Searches customer profiles based on phone number.
	 *
	 * @param phoneNumber the phone number to search for
	 * @return a list of customer profiles with the specified phone number
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnPhoneNumber(String phoneNumber) throws ResourceNotFoundException;

	/**
	 * Searches customer profiles based on region.
	 *
	 * @param region the region to search for
	 * @return a list of customer profiles with the specified region
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnRegion(Region region) throws ResourceNotFoundException;

	/**
	 * Searches customer profiles based on interest.
	 *
	 * @param interest the interest to search for
	 * @return a list of customer profiles with the specified interest
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnInterest(Interest interest) throws ResourceNotFoundException;

	/**
	 * Searches customer profiles based on purchasing habits.
	 *
	 * @param purchasingHabits the purchasing habits to search for
	 * @return a list of customer profiles with the specified purchasing habits
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnPurchasingHabit(PurchasingHabits purchasingHabits) throws ResourceNotFoundException;

	/**
	 * Searches customer profiles based on region and interest.
	 *
	 * @param region the region to search for
	 * @param interest the interest to search for
	 * @return a list of customer profiles with the specified region and interest
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndInterest(Region region, Interest interest) throws ResourceNotFoundException;

	/**
	 * Searches customer profiles based on region and purchasing habits.
	 *
	 * @param region the region to search for
	 * @param purchasingHabits the purchasing habits to search for
	 * @return a list of customer profiles with the specified region and purchasing habits
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndPurchasingHabit(Region region, PurchasingHabits purchasingHabits) throws ResourceNotFoundException;

	/**
	 * Searches customer profiles based on interest and purchasing habits.
	 *
	 * @param interest the interest to search for
	 * @param purchasingHabits the purchasing habits to search for
	 * @return a list of customer profiles with the specified interest and purchasing habits
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnInterestAndPurchasingHabit(Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException;

	/**
	 * Searches customer profiles based on region, interest, and purchasing habits.
	 *
	 * @param region the region to search for
	 * @param interest the interest to search for
	 * @param purchasingHabits the purchasing habits to search for
	 * @return a list of customer profiles with the specified region, interest, and purchasing habits
	 * @throws ResourceNotFoundException if no customer profiles are found
	 */
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(Region region, Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException;

	/**
	 * Adds a purchase to the purchase history of a customer profile.
	 *
	 * @param customerId the ID of the customer profile to update
	 * @param purchase the purchase to add
	 * @return the updated customer profile
	 * @throws ResourceNotFoundException if the customer profile is not found
	 */
	public CustomerProfileDTO addToPurchaseHistory(Long customerId, String purchase) throws ResourceNotFoundException, JsonProcessingException;

	/**
	 * Adds multiple purchases to the purchase history of a customer profile.
	 *
	 * @param customerId the ID of the customer profile to update
	 * @param jsonBody the list of purchases to add
	 * @return the updated customer profile
	 * @throws ResourceNotFoundException if the customer profile is not found
	 */
	public CustomerProfileDTO addMultiplePurchasesToPurchaseHistory(Long customerId, String jsonBody) throws ResourceNotFoundException, JsonProcessingException;
}