package com.crm.controller;

import com.crm.dto.CustomerProfileDTO;
import com.crm.enums.Interest;
import com.crm.enums.PurchasingHabits;
import com.crm.enums.Region;
import com.crm.exception.ResourceNotFoundException;

import com.crm.service.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;


@WebMvcTest(CustomerControllerImpl.class)
class CustomerControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerServiceImpl service;

    private static CustomerProfileDTO customerProfileDTO;

    @BeforeEach
    void setUp() {
        customerProfileDTO = CustomerProfileDTO.builder()
                .customerID(1L)
                .name("Thamzhini")
                .emailId("Thamz@example.com")
                .phoneNumber("9008006005")
                .build();

    }

    @Test
    @DisplayName("Test retrieving all customer profiles - Positive case")

    void testGetAllCustomerProfiles_Positive() throws Exception {
        List<CustomerProfileDTO> customerProfileDTOList = new ArrayList<>();
        customerProfileDTOList.add(customerProfileDTO);
        when(service.retrieveAllProfiles()).thenReturn(customerProfileDTOList);
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());
    }
    @Test
    @DisplayName("Test retrieving all customer profiles - Negative case")
    void testGetAllCustomerProfiles_Negative() throws Exception {
        when(service.retrieveAllProfiles()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @DisplayName("Test retrieving customer profile by ID - Positive case")
    void testGetCustomerById_Positives() throws Exception {
        when(service.getCustomerById(anyLong())).thenReturn(customerProfileDTO);
        mockMvc.perform(get("/api/customers/{customerId}","1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerID").value(1))
                .andExpect(jsonPath("$.name").value("Thamzhini"));
    }

    @Test
    @DisplayName("Test retrieving customer profile by ID - Negative case")
    void testGetCustomerById_Negative() throws Exception {
        when(service.getCustomerById(anyLong())).thenThrow(new ResourceNotFoundException("No customers found"));
        mockMvc.perform(get("/api/customers/{customerId}", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No customers found"));
    }

    @Test
    @DisplayName("Test searching customer by email ID - Positive case")
    void testSearchCustomerBasedOnEmailId_Positive() throws Exception {
        when(service.searchCustomerBasedOnEmailId(anyString())).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/email/{emailId}","Thamz@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());
    }
    @Test
    @DisplayName("Test searching customer by email ID - Negative case")
    void testSearchCustomerBasedOnEmailId_Negative() throws Exception {
        when(service.searchCustomerBasedOnEmailId(anyString())).thenThrow(new ResourceNotFoundException("No customers found with the given email"));
        mockMvc.perform(get("/api/customers/email/{emailId}", "Thamz@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No customers found with the given email"));
    }

    @Test
    @DisplayName("Test searching customer by name - Positive case")
    void testSearchCustomerBasedOnName_Positive() throws Exception {
        when(service.searchCustomerBasedOnName(anyString())).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/name/{name}","Thamzhini"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("Test searching customer by name - Negative case")
    void testSearchCustomerBasedOnName_Negative() throws Exception {
        when(service.searchCustomerBasedOnName(anyString())).thenThrow(new ResourceNotFoundException("No customers found with the given name"));
        mockMvc.perform(get("/api/customers/name/{name}", "Thamzhini"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No customers found with the given name"));
    }

    @Test
    @DisplayName("Test searching customer by phone number - Positive case")
    void testSearchCustomerBasedOnPhoneNumber_Positive() throws Exception {
        when(service.searchCustomerBasedOnPhoneNumber(anyString())).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/phoneNumber/{phoneNumber}","1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("Test searching customer by phone number - Negative case")
    void testSearchCustomerBasedOnPhoneNumber_Negative() throws Exception {
        when(service.searchCustomerBasedOnPhoneNumber(anyString())).thenThrow(new ResourceNotFoundException("No customers found with the given phonenumber"));
        mockMvc.perform(get("/api/customers/phoneNumber/{phoneNumber}", "1234567890"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No customers found with the given phonenumber"));
    }

    @Test
    @DisplayName("Test searching customer by region - Positive case")
    void testSearchCustomerBasedOnRegion_Positive() throws Exception {
        when(service.searchCustomerBasedOnRegion(Region.NORTH)).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/region/{region}","NORTH"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

    }

    @Test
    @DisplayName("Test searching customer by region - Negative case")
    void testSearchCustomerBasedOnRegion_Negative() throws Exception {
        when(service.searchCustomerBasedOnRegion(Region.NORTH)).thenThrow(new ResourceNotFoundException("No Customer Profiles Found"));
        mockMvc.perform(get("/api/customers/region/{region}", "NORTH"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Customer Profiles Found"));
    }

    @Test
    @DisplayName("Test searching customer by region - Negative case with wrong input")
    void testSearchCustomerBasedOnRegion_Negative_wrongInput() throws Exception {
        mockMvc.perform(get("/api/customers/region/{region}", "NORH"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Expected : [NORTH_AMERICA, LATIN_AMERICA, ASIAN_PACIFIC, MIDDLE_EAST_AND_AFRICA, CENTRAL_AMERICA, EUROPE, NORTH, EASTERN_EUROPE, WESTERN_EUROPE, NORTHERN_EUROPE, SOUTHERN_EUROPE, SOUTHEAST_ASIA, SOUTH_ASIA, EAST_ASIA, OCEANIA, SUB_SAHARAN_AFRICA] , but Received : NORH"));
    }

    @Test
    @DisplayName("Test searching customer by interest - Positive case")
    void testSearchCustomerBasedOnInterest_Positive() throws Exception {
        when(service.searchCustomerBasedOnInterest(Interest.MUSIC)).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/interest/{interest}","MUSIC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

    }

    @Test
    @DisplayName("Test searching customer by interest - Negative case")
    void testSearchCustomerBasedOnInterest_Negative() throws Exception {
        when(service.searchCustomerBasedOnInterest(Interest.MUSIC)).thenThrow(new ResourceNotFoundException("No Customer Profiles Found"));
        mockMvc.perform(get("/api/customers/interest/{interest}", "MUSIC"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Customer Profiles Found"));
    }

    @Test
    @DisplayName("Test searching customer by purchasing habit - Positive case")
    void testSearchCustomerBasedOnPurchasingHabit_Positive() throws Exception {
        when(service.searchCustomerBasedOnPurchasingHabit(PurchasingHabits.NEW)).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/purchasingHabit/{purchasingHabits}","NEW"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

    }

    @Test
    @DisplayName("Test searching customer by purchasing habit - Negative case")
    void testSearchCustomerBasedOnPurchasingHabit_Negative() throws Exception {
        when(service.searchCustomerBasedOnPurchasingHabit(PurchasingHabits.NEW)).thenThrow(new ResourceNotFoundException("No Customer Profiles Found"));
        mockMvc.perform(get("/api/customers/purchasingHabit/{purchasingHabits}", "NEW"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Customer Profiles Found"));
    }
    @Test
    @DisplayName("Test searching customer by region and purchasing habit - Positive case")
    void testSearchCustomerBasedOnRegionAndPurchasingHabit_Positive() throws Exception {
        when(service.searchCustomerBasedOnRegionAndPurchasingHabit(Region.NORTH, PurchasingHabits.NEW)).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/region&purchasingHabits/{region}/{purchasingHabits}","NORTH","NEW"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

    }

    @Test
    @DisplayName("Test searching customer by region and purchasing habit - Negative case")
    void testSearchCustomerBasedOnRegionAndPurchasingHabit_Negative() throws Exception {
        when(service.searchCustomerBasedOnRegionAndPurchasingHabit(Region.NORTH, PurchasingHabits.NEW)).thenThrow(new ResourceNotFoundException("No Customer Profiles Found"));
        mockMvc.perform(get("/api/customers/region&purchasingHabits/{region}/{purchasingHabits}", "NORTH", "NEW"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Customer Profiles Found"));
    }

    @Test
    @DisplayName("Test searching customer by interest and purchasing habit - Positive case")
    void testSearchCustomerBasedOnInterestAndPurchasingHabit_Positive() throws Exception {
        when(service.searchCustomerBasedOnInterestAndPurchasingHabit(Interest.MUSIC, PurchasingHabits.NEW)).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/interest&purchasingHabits/{interest}/{purchasingHabits}","MUSIC","NEW"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

    }

    @Test
    @DisplayName("Test searching customer by interest and purchasing habit - Negative case")
    void testSearchCustomerBasedOnInterestAndPurchasingHabit_Negative() throws Exception {
        when(service.searchCustomerBasedOnInterestAndPurchasingHabit(Interest.MUSIC, PurchasingHabits.NEW)).thenThrow(new ResourceNotFoundException("No Customer Profiles Found"));
        mockMvc.perform(get("/api/customers/interest&purchasingHabits/{interest}/{purchasingHabits}", "MUSIC", "NEW"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Customer Profiles Found"));
    }

    @Test
    @DisplayName("Test searching customer by region and interest - Positive case")
    void testSearchCustomerBasedOnRegionAndInterest_Positive() throws Exception {
        when(service.searchCustomerBasedOnRegionAndInterest(Region.NORTH,Interest.MUSIC)).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/region&interest/{region}/{interest}","NORTH","MUSIC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

    }

    @Test
    @DisplayName("Test searching customer by region and interest - Negative case")
    void testSearchCustomerBasedOnRegionAndInterest_Negative() throws Exception {
        when(service.searchCustomerBasedOnRegionAndInterest(Region.NORTH, Interest.MUSIC)).thenThrow(new ResourceNotFoundException("No Customer Profiles Found"));
        mockMvc.perform(get("/api/customers/region&interest/{region}/{interest}", "NORTH", "MUSIC"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Customer Profiles Found"));
    }

    @Test
    @DisplayName("Test searching customer by region, interest, and purchasing habit - Positive case")
    void testSearchCustomerBasedOnRegionAndInterestAndPurchasingHabit_Positive() throws Exception {
        when(service.searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(Region.NORTH,Interest.MUSIC, PurchasingHabits.NEW)).thenReturn(Arrays.asList(customerProfileDTO));
        mockMvc.perform(get("/api/customers/demographics/{region}/{interest}/{purchasingHabits}","NORTH","MUSIC","NEW"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

    }

    @Test
    @DisplayName("Test searching customer by region, interest, and purchasing habit - Negative case")
    void testSearchCustomerBasedOnRegionAndInterestAndPurchasingHabit_Negative() throws Exception {
        when(service.searchCustomerBasedOnRegionAndInterestAndPurchasingHabit(Region.NORTH, Interest.MUSIC, PurchasingHabits.NEW)).thenThrow(new ResourceNotFoundException("No Customer Profiles Found"));
        mockMvc.perform(get("/api/customers/demographics/{region}/{interest}/{purchasingHabits}", "NORTH", "MUSIC", "NEW"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Customer Profiles Found"));
    }

    @Test
    @DisplayName("Test adding customer profile - Positive case")
    void testAddCustomerProfile_Positive() throws Exception {
        CustomerProfileDTO newCustomerProfile = customerProfileDTO;
        newCustomerProfile.setCustomerID(1L);
        when(service.addCustomerProfile(customerProfileDTO)).thenReturn(newCustomerProfile);
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerProfileDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerID").value(customerProfileDTO.getCustomerID()))
                .andExpect(jsonPath("$.name").value(customerProfileDTO.getName()))
                .andExpect(jsonPath("$.emailId").value(customerProfileDTO.getEmailId()));
    }

    @Test
    @DisplayName("Test adding customer profile - Negative case")
    void testAddCustomerProfile_Negative() throws Exception {
        when(service.addCustomerProfile(customerProfileDTO)).thenThrow(new ResourceNotFoundException("Enter valid Customer Profile Details"));
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerProfileDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enter valid Customer Profile Details"));
    }

    @Test
    @DisplayName("Test updating customer profile - Positive case")
    void testUpdateCustomer_Positive() throws Exception {

        when(service.updateCustomer(2L, customerProfileDTO)).thenReturn(customerProfileDTO);
        MvcResult result = mockMvc.perform(put("/api/customers/{customerId}", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerProfileDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals(new ObjectMapper().writeValueAsString(customerProfileDTO), responseBody);
    }

    @Test
    @DisplayName("Test updating customer profile - Negative case")
    void testUpdateCustomer_Negative() throws Exception {
        when(service.updateCustomer(2L, customerProfileDTO)).thenThrow(new ResourceNotFoundException("Customer not found with id: 2"));
        mockMvc.perform(put("/api/customers/{customerId}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerProfileDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found with id: 2"));
    }


    @Test
    @DisplayName("Test updating purchasing habit - Positive case")
    void testUpdatePurchasingHabit_Positive() throws Exception {
        customerProfileDTO.setSegmentationData(Map.of("Region", "NORTH", "Interest","MUSIC","Purchase Habits","FREQUENT"));
        when(service.updatePurchasingHabit(1L)).thenReturn(customerProfileDTO);

        mockMvc.perform(put("/api/customers/purchasingHabit/{customerId}",1))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Test updating purchasing habit - Negative case")
    void testUpdatePurchasingHabit_Negative() throws Exception {
        when(service.updatePurchasingHabit(1L)).thenThrow(new ResourceNotFoundException("Customer not found with id: 1"));

        mockMvc.perform(put("/api/customers/purchasingHabit/{customerId}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found with id: 1"));
    }

    @Test
    @DisplayName("Test deleting customer profile - Positive case")
    void testDeleteCustomer_Positive() throws Exception{

        mockMvc.perform(delete("/api/customers/{customerId}",1))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer Profile Deleted"));

    }

    @Test
    @DisplayName("Test deleting customer profile - Negative case")
    void testDeleteCustomer_Negative() throws Exception {
        doThrow(new ResourceNotFoundException("Delete failed - Customer Profile Not found"))
                .when(service).deleteCustomer(1L);
        mockMvc.perform(delete("/api/customers/{customerId}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Delete failed - Customer Profile Not found"));
    }

    @Test
    @DisplayName("Test adding to purchase history - Positive case")
    void testAddToPurchaseHistory_Positive() throws Exception {
        customerProfileDTO.setPurchaseHistory(List.of("purchase1", "purchase2", "purchase3"));

        when(service.addToPurchaseHistory(2L, "{\"purchaseHistory\":\"purchase3\"}")).thenReturn(customerProfileDTO);

        MvcResult result = mockMvc.perform(post("/api/customers/purchaseHistory/{customerId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"purchaseHistory\":\"purchase3\"}"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        assertEquals(new ObjectMapper().writeValueAsString(customerProfileDTO), responseBody);
    }

    @Test
    @DisplayName("Test adding to purchase history - Negative case")
    void testAddToPurchaseHistory_Negative() throws Exception {
        doThrow(new ResourceNotFoundException("Customer Profile Not found"))
                .when(service).addToPurchaseHistory(2L, "purchase3");
        mockMvc.perform(post("/api/customers/purchaseHistory/{customerId}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("purchase3"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer Profile Not found"));
    }

    @Test
    @DisplayName("Test adding multiple purchases to purchase history - Positive case")
    void testAddMultipleToPurchaseHistory_Positive() throws Exception {
        customerProfileDTO.setPurchaseHistory(List.of("purchase1", "purchase2", "purchase3"));

        when(service.addMultiplePurchasesToPurchaseHistory(2L, "{\n" +
                "    \"purchaseHistory\" : [\"newPurchase1\", \"newPurchase2\", \"newPurchase3\"]\n" +
                "}")).thenReturn(customerProfileDTO);

        MvcResult result = mockMvc.perform(post("/api/customers/purchaseHistory/multiple/{customerId}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"purchaseHistory\" : [\"newPurchase1\", \"newPurchase2\", \"newPurchase3\"]\n" +
                                "}"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        assertEquals(new ObjectMapper().writeValueAsString(customerProfileDTO), responseBody);
    }

    @Test
    @DisplayName("Test adding multiple purchases to purchase history - Negative case")
    void testAddMultipleToPurchaseHistory_Negative() throws Exception {
        doThrow(new ResourceNotFoundException("Customer Profile Not found"))
                .when(service).addMultiplePurchasesToPurchaseHistory(2L, "{\n" +
                        "    \"purchaseHistory\" : [\"newPurchase1\", \"newPurchase2\", \"newPurchase3\"]\n" +
                        "}");
        mockMvc.perform(post("/api/customers/purchaseHistory/multiple/{customerId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"purchaseHistory\" : [\"newPurchase1\", \"newPurchase2\", \"newPurchase3\"]\n" +
                                "}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer Profile Not found"));
    }
}


