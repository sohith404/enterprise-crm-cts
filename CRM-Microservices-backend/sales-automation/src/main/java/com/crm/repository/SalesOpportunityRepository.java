package com.crm.repository;

import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SalesOpportunityRepository extends JpaRepository<SalesOpportunity, Long> {
    List<SalesOpportunity> findByCustomerID(long customerID);

    List<SalesOpportunity> findBySalesStage(SalesStage salesStage);

    List<SalesOpportunity> findByEstimatedValue(BigDecimal estimatedValue);

    List<SalesOpportunity> findByClosingDate(LocalDate closingDate);

    List<SalesOpportunity> findByFollowUpReminder(LocalDate followUpReminder);
}