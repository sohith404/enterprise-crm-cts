package com.crm.repository;

import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SalesOpportunityRepositoryTest {

    @Autowired
    private SalesOpportunityRepository repository;

    @Test
    @DisplayName("save() - Positive")
    void saveSalesOpportunityShouldPersistDataSuccessfully() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("10000.0"))
                .salesStage(SalesStage.PROSPECTING)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity savedOpportunity = repository.save(opportunity);
        assertTrue(savedOpportunity.getOpportunityID() > 0, "Opportunity ID should be greater than 0");
    }

    @Test
    @DisplayName("findById() - Positive")
    void findSalesOpportunityByIdShouldReturnOpportunityWhenIdExists() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity savedOpportunity = repository.save(opportunity);
        Optional<SalesOpportunity> foundOpportunity = repository.findById(savedOpportunity.getOpportunityID());
        assertTrue(foundOpportunity.isPresent(), "SalesOpportunity not found");
        assertEquals(savedOpportunity.getOpportunityID(), foundOpportunity.get().getOpportunityID());
    }

    @Test
    @DisplayName("findById() - Negative")
    void findSalesOpportunityByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Optional<SalesOpportunity> savedOpportunity = repository.findById(999L); // Use an ID that doesn't exist
        assertFalse(savedOpportunity.isPresent(), "SalesOpportunity should not be found");
    }

    @Test
    @DisplayName("findAll() - Positive")
    void findAllSalesOpportunitiesShouldReturnListOfOpportunitiesWhenDataExists() {
        SalesOpportunity opportunity1 = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("30000.0"))
                .salesStage(SalesStage.CLOSED_WON)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity opportunity2 = SalesOpportunity.builder()
                .customerID(2L)
                .estimatedValue(new BigDecimal("40000.0"))
                .salesStage(SalesStage.CLOSED_LOST)
                .closingDate(LocalDate.now())
                .build();

        repository.save(opportunity1);
        repository.save(opportunity2);

        List<SalesOpportunity> allOpportunities = repository.findAll();
        assertFalse(allOpportunities.isEmpty(), "SalesOpportunity list should not be empty");
        assertEquals(2, allOpportunities.size(), "SalesOpportunity list should contain 2 entries");
    }

    @Test
    @DisplayName("findAll() - Negative")
    void findAllSalesOpportunitiesShouldReturnEmptyListWhenNoDataExists() {
        List<SalesOpportunity> allOpportunities = repository.findAll();
        assertTrue(allOpportunities.isEmpty(), "SalesOpportunity list should be empty");
    }

    @Test
    @DisplayName("update() - Positive")
    void updateSalesOpportunityShouldModifyExistingOpportunity() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity saved = repository.save(opportunity);

        SalesOpportunity salesOpportunity = repository.findById(saved.getOpportunityID()).get();
        salesOpportunity.setSalesStage(SalesStage.CLOSED_WON);
        SalesOpportunity newSalesOpportunity = repository.save(salesOpportunity);
        assertEquals(SalesStage.CLOSED_WON, newSalesOpportunity.getSalesStage());

    }

    @Test
    @DisplayName("findByCustomerID() - Positive")
    void findSalesOpportunitiesByCustomerIdShouldReturnListOfOpportunitiesWhenCustomerExists() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findByCustomerID(1L);
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByCustomerID() - Negative")
    void findSalesOpportunitiesByCustomerIdShouldReturnEmptyListWhenCustomerDoesNotExist() {

        List<SalesOpportunity> salesOpportunityList = repository.findByCustomerID(2L);
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findBySalesStage() - Positive")
    void findSalesOpportunitiesBySalesStageShouldReturnListOfOpportunitiesWhenSalesStageExists() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findBySalesStage(SalesStage.QUALIFICATION);
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findBySalesStage() - Negative")
    void findSalesOpportunitiesBySalesStageShouldReturnEmptyListWhenSalesStageDoesNotExist() {

        List<SalesOpportunity> salesOpportunityList = repository.findBySalesStage(SalesStage.PROSPECTING);
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByEstimatedValue() - Positive")
    void findSalesOpportunitiesByEstimatedValueShouldReturnListOfOpportunitiesWhenEstimatedValueExists() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findByEstimatedValue(BigDecimal.valueOf(20000.0));
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByEstimatedValue() - Negative")
    void findSalesOpportunitiesByEstimatedValueShouldReturnEmptyListWhenEstimatedValueDoesNotExist() {

        List<SalesOpportunity> salesOpportunityList = repository.findByEstimatedValue(BigDecimal.valueOf(100000.0));
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByClosingDate() - Positive")
    void findSalesOpportunitiesByClosingDateShouldReturnListOfOpportunitiesWhenClosingDateExists() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .build();

        SalesOpportunity save = repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findByClosingDate(save.getClosingDate());
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("findByClosingDate() - Negative")
    void findSalesOpportunitiesByClosingDateShouldReturnEmptyListWhenClosingDateDoesNotExist() {

        List<SalesOpportunity> salesOpportunityList = repository.findByClosingDate(LocalDate.now());
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }


    @Test
    @DisplayName("findByFollowUpReminder() - Positive")
    void findSalesOpportunitiesByFollowUpReminderShouldReturnListOfOpportunitiesWhenFollowUpReminderExists() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .followUpReminder(LocalDate.of(2020, Month.JANUARY, 18))
                .build();

        repository.save(opportunity);
        List<SalesOpportunity> salesOpportunityList = repository.findByFollowUpReminder(LocalDate.of(2020, Month.JANUARY, 18));
        assertFalse(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }


    @Test
    @DisplayName("findByFollowUpReminder() - Negative")
    void findSalesOpportunityfindSalesOpportunitiesByFollowUpReminderShouldReturnEmptyListWhenFollowUpReminderDoesNotExistByFollowUpReminder_Negative() {

        List<SalesOpportunity> salesOpportunityList = repository.findByFollowUpReminder(LocalDate.of(2020, Month.JANUARY, 18));
        assertTrue(salesOpportunityList.isEmpty(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("delete() - Positive")
    void deleteSalesOpportunityShouldRemoveOpportunityFromRepository() {
        SalesOpportunity opportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(new BigDecimal("20000.0"))
                .salesStage(SalesStage.QUALIFICATION)
                .closingDate(LocalDate.now())
                .followUpReminder(LocalDate.of(2020, Month.JANUARY, 18))
                .build();

        SalesOpportunity saved = repository.save(opportunity);
        repository.delete(opportunity);
        Optional<SalesOpportunity> salesOpportunity = repository.findById(saved.getOpportunityID());
        assertFalse(salesOpportunity.isPresent(), "SalesOpportunity not found");
    }

    @Test
    @DisplayName("delete() - Negative")
    void deleteSalesOpportunityShouldThrowExceptionWhenOpportunityIsNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> repository.delete(null));
    }

}
