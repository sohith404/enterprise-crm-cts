package com.crm.controller;

import com.crm.dto.CustomerProfileDTO;
import com.crm.entities.CustomerProfile;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing customer profiles.
 */

@RequestMapping("api/customers")
public interface CustomerController {
    /**
     * GET /api/customers : Get all customer profiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customer profiles in body
     * @throws ResourceNotFoundException if no customer profiles are found
     */

    @GetMapping
    public ResponseEntity<?> getAllCustomerProfiles() throws ResourceNotFoundException;

    /**
     * GET /api/customers/{customerId} : Get the "customerId" customer profile.
     *
     * @param customerId the id of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable long customerId) throws ResourceNotFoundException;

    /**
     * GET /api/customers/email/{emailId} : Get the customer profile by email.
     *
     * @param emailId the email of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/email/{emailId}")
    public ResponseEntity<?> searchCustomerBasedOnEmailId(@PathVariable @Email(message = "Invalid email address") String emailId) throws ResourceNotFoundException;

    /**
     * GET /api/customers/name/{name} : Get the customer profile by name.
     *
     * @param name the name of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/name/{name}")
    public ResponseEntity<?> searchCustomerBasedOnName(@PathVariable @NotBlank String name) throws ResourceNotFoundException;

    /**
     * GET /api/customers/phoneNumber/{phoneNumber} : Get the customer profile by phone number.
     *
     * @param phoneNumber the phone number of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<?> searchCustomerBasedOnPhoneNumber(@PathVariable  @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits") String phoneNumber) throws ResourceNotFoundException;

    /**
     * GET /api/customers/region/{region} : Get the customer profile by region.
     *
     * @param region the region of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/region/{region}")
    public ResponseEntity<?> searchCustomerBasedOnRegion(@PathVariable String region) throws ResourceNotFoundException;

    /**
     * GET /api/customers/purchasingHabit/{purchasingHabits} : Get the customer profile by purchasing habits.
     *
     * @param purchasingHabits the purchasing habits of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/purchasingHabit/{purchasingHabits}")
    public ResponseEntity<?> searchCustomerBasedOnPurchasingHabit(@PathVariable String purchasingHabits) throws ResourceNotFoundException;

    /**
     * GET /api/customers/interest/{interest} : Get the customer profile by interest.
     *
     * @param interest the interest of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/interest/{interest}")
    public ResponseEntity<?> searchCustomerBasedOnInterest(@PathVariable String interest) throws ResourceNotFoundException;

    /**
     * GET /api/customers/region&interest/{region}/{interest} : Get the customer profile by region and interest.
     *
     * @param region the region of the customer profile to retrieve
     * @param interest the interest of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/region&interest/{region}/{interest}")
    public ResponseEntity<?> searchCustomerBasedOnRegionInterest(@PathVariable String region,@PathVariable String interest) throws ResourceNotFoundException;

    /**
     * GET /api/customers/interest&purchasingHabits/{interest}/{purchasingHabits} : Get the customer profile by interest and purchasing habits.
     *
     * @param interest the interest of the customer profile to retrieve
     * @param purchasingHabits the purchasing habits of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/interest&purchasingHabits/{interest}/{purchasingHabits}")
    public ResponseEntity<?> searchCustomerBasedOnInterestPurchasingHabits(@PathVariable String interest,@PathVariable String purchasingHabits) throws ResourceNotFoundException;

    /**
     * GET /api/customers/region&purchasingHabits/{region}/{purchasingHabits} : Get the customer profile by region and purchasing habits.
     *
     * @param region the region of the customer profile to retrieve
     * @param purchasingHabits the purchasing habits of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/region&purchasingHabits/{region}/{purchasingHabits}")
    public ResponseEntity<?> searchCustomerBasedOnRegionPurchasingHabits(@PathVariable String region,@PathVariable String purchasingHabits) throws ResourceNotFoundException;

    /**
     * GET /api/customers/demographics/{region}/{interest}/{purchasingHabits} : Get the customer profile by region, interest, and purchasing habits.
     *
     * @param region the region of the customer profile to retrieve
     * @param interest the interest of the customer profile to retrieve
     * @param purchasingHabits the purchasing habits of the customer profile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @GetMapping("/demographics/{region}/{interest}/{purchasingHabits}")
    public ResponseEntity<?> searchCustomerBasedOnRegionInterestPurchasingHabits(@PathVariable String region,@PathVariable String interest,@PathVariable String purchasingHabits) throws ResourceNotFoundException;

    /**
     * POST /api/customers : Add a new customer profile.
     *
     * @param customerProfileDTO the customer profile to add
     * @return the ResponseEntity with status 201 (Created) and with body the new customer profile, or with status 400 (Bad Request) if the customer profile is not valid
     * @throws ResourceNotFoundException if the customer profile cannot be added
     */

    @PostMapping
    public ResponseEntity<CustomerProfileDTO> addCustomerProfile(@RequestBody @Valid CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException;


    /**
     * PUT /api/customers/purchasingHabit/{customerId} : Update the purchasing habit of the "customerId" customer profile.
     *
     * @param customerId the id of the customer profile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @PutMapping("/purchasingHabit/{customerId}")
    public ResponseEntity<CustomerProfileDTO> updatePurchasingHabit(@PathVariable long customerId) throws ResourceNotFoundException, JsonProcessingException;

    /**
     * PUT /api/customers/{customerId} : Update the "customerId" customer profile.
     *
     * @param customerId the id of the customer profile to update
     * @param customerProfileDTO the customer profile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customer profile, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable long customerId, @RequestBody @Valid CustomerProfileDTO customerProfileDTO) throws ResourceNotFoundException;

    /**
     * POST /api/customers/purchaseHistory/{customerId} : Add a purchase to the purchase history of the "customerId" customer profile.
     *
     * @param customerId the id of the customer profile to update
     * @param purchase the purchase to add
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchase history, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @PostMapping("/purchaseHistory/{customerId}")
    public ResponseEntity<CustomerProfileDTO> addPurchaseToPurchaseHistory(@PathVariable long customerId, @RequestBody String purchase) throws ResourceNotFoundException, JsonProcessingException;

    /**
     * POST /api/customers/purchaseHistory/multiple/{customerId} : Add multiple purchases to the purchase history of the "customerId" customer profile.
     *
     * @param customerId the id of the customer profile to update
     * @param jsonBody the json containing list of purchases to add
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchase history, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @PostMapping("/purchaseHistory/multiple/{customerId}")
    public ResponseEntity<CustomerProfileDTO> addMultiplePurchaseToPurchaseHistory(@PathVariable long customerId, @RequestBody @NotEmpty String jsonBody) throws ResourceNotFoundException, JsonProcessingException;

    /**
     * DELETE /api/customers/{customerId} : Delete the "customerId" customer profile.
     *
     * @param customerId the id of the customer profile to delete
     * @return the ResponseEntity with status 200 (OK) and with body the confirmation message, or with status 404 (Not Found)
     * @throws ResourceNotFoundException if the customer profile is not found
     */

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable long customerId) throws ResourceNotFoundException;

}
