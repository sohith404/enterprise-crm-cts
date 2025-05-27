package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.EnumValueNotFoundException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.mapper.CustomerProfileMapper;
import com.crm.repository.CustomerProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service implementation for managing customer profiles.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerProfileRepository customerProfileRepository;
	private final CustomerProfileMapper customerProfileMapper;
	private final ObjectMapper objectMapper;

	private static final String PURCHASING_HABITS = "Purchasing Habits";
	private static final String SEGMENTATION_DATA = "segmentationData";
	private static final String ERROR_MSG = "There are no Customers";
	private static final String ERROR_MSG_ID = "Customer not found with id: ";
	

	@Autowired
	public CustomerServiceImpl(CustomerProfileRepository customerProfileRepository,
							   CustomerProfileMapper customerProfileMapper,
							   ObjectMapper objectMapper) {
		this.customerProfileRepository = customerProfileRepository;
		this.customerProfileMapper = customerProfileMapper;
		this.objectMapper = objectMapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegion(Region region) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException(ERROR_MSG);
		}
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c -> getRegionFromSegmentation(c) == region)
				.map(customerProfileMapper::toDTO)
				.toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("No Customers found in the region");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnInterest(Interest interest) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException(ERROR_MSG);
		}
		List<CustomerProfileDTO> list =  customerProfiles.stream()
				.filter(c -> getInterestFromSegmentation(c) == interest)
				.map(customerProfileMapper::toDTO)
				.toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("No Customer Found with this interest");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnPurchasingHabit(PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException(ERROR_MSG);
		}
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c -> getPurchasingHabitsFromSegmentation(c) == purchasingHabits)
				.map(customerProfileMapper::toDTO)
				.toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("No Customer with this Purchasing Habit");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndInterest(Region region, Interest interest) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException(ERROR_MSG);
		}
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c -> getRegionFromSegmentation(c) == region && getInterestFromSegmentation(c) == interest)
				.map(customerProfileMapper::toDTO)
				.toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("No customers found in this region with this interest");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndPurchasingHabit(Region region, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException(ERROR_MSG);
		}
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c -> getRegionFromSegmentation(c) == region && getPurchasingHabitsFromSegmentation(c) == purchasingHabits)
				.map(customerProfileMapper::toDTO)
				.toList();
		if (list.isEmpty()) {
			throw new ResourceNotFoundException("There are no customers from this region with this purchasinghabit");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnInterestAndPurchasingHabit(Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException(ERROR_MSG);
		}
		List<CustomerProfileDTO> list =  customerProfiles.stream()
				.filter(c -> getInterestFromSegmentation(c) == interest && getPurchasingHabitsFromSegmentation(c) == purchasingHabits)
				.map(customerProfileMapper::toDTO)
				.toList();
		if(list.isEmpty()){
			throw  new ResourceNotFoundException("No Customers found with this interest and purchasinghabit");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addCustomerProfile(CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
		if (customerProfileDTO == null) {
			throw new ResourceNotFoundException("Enter valid Customer Profile Details");
		}
		CustomerProfile customerProfile = customerProfileMapper.toEntity(customerProfileDTO);
		CustomerProfile savedCustomer = customerProfileRepository.save(customerProfile);
		return customerProfileMapper.toDTO(savedCustomer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> retrieveAllProfiles() throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found");
		}
		return customerProfiles.stream().map(customerProfileMapper::toDTO).toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO getCustomerById(Long customerId) throws ResourceNotFoundException {
		CustomerProfile customerProfile = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(ERROR_MSG_ID + customerId));
		return customerProfileMapper.toDTO(customerProfile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO updateCustomer(Long customerId, CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(ERROR_MSG_ID + customerId));
		CustomerProfile updatedCustomerProfile = customerProfileMapper.toEntity(customerProfileDTO);
		updatedCustomerProfile.setCustomerID(existingCustomer.getCustomerID());
		updatedCustomerProfile = customerProfileRepository.save(updatedCustomerProfile);
		return customerProfileMapper.toDTO(updatedCustomerProfile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteCustomer(Long customerId) throws ResourceNotFoundException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(ERROR_MSG_ID + customerId));
		customerProfileRepository.deleteById(existingCustomer.getCustomerID());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnEmailId(String email) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByEmailId(email);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given email");
		}
		return customerProfiles.stream().map(customerProfileMapper::toDTO).toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnName(String name) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByNames(name);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given name");
		}
		return customerProfiles.stream().map(customerProfileMapper::toDTO).toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnPhoneNumber(String phoneNumber) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAllByPhoneNumber(phoneNumber);
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException("No customers found with the given phonenumber");
		}
		return customerProfiles.stream().map(customerProfileMapper::toDTO).toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomerProfileDTO> searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(Region region, Interest interest, PurchasingHabits purchasingHabits) throws ResourceNotFoundException {
		List<CustomerProfile> customerProfiles = customerProfileRepository.findAll();
		if (customerProfiles.isEmpty()) {
			throw new ResourceNotFoundException(ERROR_MSG);
		}
		List<CustomerProfileDTO> list = customerProfiles.stream()
				.filter(c -> getInterestFromSegmentation(c) == interest && getRegionFromSegmentation(c) == region && getPurchasingHabitsFromSegmentation(c) == purchasingHabits)
				.map(customerProfileMapper::toDTO)
				.toList();
		if(list.isEmpty()){
			throw new ResourceNotFoundException("No Customers found in this region with specified interest and purchasinghabits");
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addToPurchaseHistory(Long customerId, String purchase) throws ResourceNotFoundException, JsonProcessingException {
		Map<String, String> jsonObject = objectMapper.readValue(purchase, Map.class);
		String purchaseHistory = jsonObject.get("purchaseHistory");
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(ERROR_MSG_ID + customerId));
		existingCustomer.getPurchaseHistory().add(purchaseHistory); // or purchase, if that is the intended behavior
		CustomerProfile updatedCustomer = customerProfileRepository.save(existingCustomer);

		return customerProfileMapper.toDTO(updatedCustomer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO addMultiplePurchasesToPurchaseHistory(Long customerId, String jsonBody) throws ResourceNotFoundException, JsonProcessingException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(ERROR_MSG_ID + customerId));

		Map<String, List<String>> jsonObject = objectMapper.readValue(jsonBody, Map.class);

		List<String> newPurchases = jsonObject.get("purchaseHistory");

		List<String> purchaseHistory = new ArrayList<>(existingCustomer.getPurchaseHistory());
		purchaseHistory.addAll(newPurchases);
		existingCustomer.setPurchaseHistory(purchaseHistory);
		CustomerProfile updatedCustomer = customerProfileRepository.save(existingCustomer);
		return customerProfileMapper.toDTO(updatedCustomer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerProfileDTO updatePurchasingHabit(Long customerId) throws ResourceNotFoundException, JsonProcessingException {
		CustomerProfile existingCustomer = customerProfileRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(ERROR_MSG_ID + customerId));

		int numberOfPurchases = existingCustomer.getPurchaseHistory().size();
		PurchasingHabits newPurchasingHabit;

		if (numberOfPurchases <= 3) {
			newPurchasingHabit = PurchasingHabits.NEW;
		} else if (numberOfPurchases < 10) {
			newPurchasingHabit = PurchasingHabits.SPARSE;
		} else if (numberOfPurchases < 25) {
			newPurchasingHabit = PurchasingHabits.REGULAR;
		} else {
			newPurchasingHabit = PurchasingHabits.FREQUENT;
		}


		String segmentationDataString = existingCustomer.getSegmentationData();

		if (segmentationDataString == null || segmentationDataString.isEmpty()) {
			Map<String, Map<String, String>> newSegmentationMap = new HashMap<>();
			Map<String, String> segmentationData = new HashMap<>();
			segmentationData.put("Interest", null);
			segmentationData.put("Region", null);
			segmentationData.put(PURCHASING_HABITS, newPurchasingHabit.name());
			newSegmentationMap.put(SEGMENTATION_DATA, segmentationData);

			existingCustomer.setSegmentationData(objectMapper.writeValueAsString(newSegmentationMap));
		} else {
			Map<String, Map<String, String>> segmentationMap = objectMapper.readValue(segmentationDataString, Map.class);

			Map<String, String> segmentationData = segmentationMap.get(SEGMENTATION_DATA);
			if (segmentationData != null) {
				segmentationData.put(PURCHASING_HABITS, newPurchasingHabit.name());
				existingCustomer.setSegmentationData(objectMapper.writeValueAsString(segmentationMap));
			} else {
				throw new ResourceNotFoundException("Segmentation data is missing or invalid.");
			}
		}

		CustomerProfile updatedCustomer = customerProfileRepository.save(existingCustomer);
		return customerProfileMapper.toDTO(updatedCustomer);

	}

	/**
	 * Retrieves the Region enum value from the customer's segmentation data.
	 *
	 * @param customerProfile the customer profile containing segmentation data
	 * @return the Region enum value, or null if not found
	 */
	private Region getRegionFromSegmentation(CustomerProfile customerProfile) {
		return getEnumFromSegmentation(customerProfile, "Region", Region.class);
	}

	/**
	 * Retrieves the Interest enum value from the customer's segmentation data.
	 *
	 * @param customerProfile the customer profile containing segmentation data
	 * @return the Interest enum value, or null if not found
	 */
	private Interest getInterestFromSegmentation(CustomerProfile customerProfile) {
		return getEnumFromSegmentation(customerProfile, "Interest", Interest.class);
	}

	/**
	 * Retrieves the PurchasingHabits enum value from the customer's segmentation data.
	 *
	 * @param customerProfile the customer profile containing segmentation data
	 * @return the PurchasingHabits enum value, or null if not found
	 */
	private PurchasingHabits getPurchasingHabitsFromSegmentation(CustomerProfile customerProfile) {
		return getEnumFromSegmentation(customerProfile, PURCHASING_HABITS, PurchasingHabits.class);
	}

	/**
	 * Retrieves an enum value from the customer's segmentation data based on the specified field name and enum type.
	 *
	 * @param <T>             the type of the enum
	 * @param customerProfile the customer profile containing segmentation data
	 * @param fieldName       the name of the field to retrieve the enum value for
	 * @param enumType        the class of the enum type
	 * @return the enum value, or null if not found
	 * @throws EnumValueNotFoundException if the enum value is null
	 */
	public <T extends Enum<T>> T getEnumFromSegmentation(CustomerProfile customerProfile, String fieldName, Class<T> enumType) {
		if (customerProfile.getSegmentationData() == null || customerProfile.getSegmentationData().isEmpty()) {
			return null;
		}

		try {
			Map<String, Map<String, String>> segmentationMap = objectMapper.readValue(customerProfile.getSegmentationData(), Map.class);

			Map<String, String> segmentationData = segmentationMap.get(SEGMENTATION_DATA);
			if (segmentationData == null) {
				return null;
			}

			String enumValue = segmentationData.get(fieldName);
			if (enumValue != null) {
				return Enum.valueOf(enumType, enumValue);
			} else {
				throw new EnumValueNotFoundException("enum is null");
			}
		} catch (Exception e) {
			throw new EnumValueNotFoundException("enum is null");
		}
	}
}