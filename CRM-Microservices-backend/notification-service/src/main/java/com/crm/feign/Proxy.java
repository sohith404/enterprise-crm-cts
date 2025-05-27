package com.crm.feign;

import com.crm.dto.CustomerProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

/**
 * Feign client interface for accessing customer data management services.
 * This interface defines methods to interact with the customer data management API
 * through the API gateway.
 */
@FeignClient("api-gateway")
public interface Proxy {
    /**
     * Retrieves a list of all customer profiles.
     *
     * <p>This method makes a GET request to the "api/customers" endpoint through the
     * "api-gateway" Feign client.</p>
     *
     * @return A {@link ResponseEntity} containing a list of {@link CustomerProfileDTO} objects.
     * The response may be empty if no customers are found.
     */
    @GetMapping("api/customers")
    ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles();
}