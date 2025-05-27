package com.crm.entities;

import com.crm.enums.SalesStage;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "sales_opportunity")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOpportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opportunity_id", nullable = false)
    private Long opportunityID;
    //Foreign Key
    @Column(name = "customer_id")
    private Long customerID;
    @Enumerated(EnumType.STRING)
    @Column(name = "sales_stage")
    private SalesStage salesStage;
    @Column(name = "estimated_value")
    private BigDecimal estimatedValue;
    @Column(name = "closing_date")
    private LocalDate closingDate;
    @Column(name = "follow_up_reminder")
    private LocalDate followUpReminder;

}