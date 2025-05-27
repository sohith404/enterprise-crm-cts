package com.crm.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a customer profile.
 */
@Entity
@Getter
@Setter
@Table(name = "customer_profile")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfile {

    /**
     * The unique ID of the customer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long customerID;

    /**
     * The name of the customer.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The emailID of the customer.
     */
    @Column(name = "emailId", nullable = false)
    private String emailId;

    /**
     * The phone number of the customer.
     */
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    /**
     * The purchase history of the customer.
     */
    @ElementCollection
    @CollectionTable(name = "purchase_history",joinColumns = @JoinColumn(name = "customer_id") )
    private List<String> purchaseHistory;

    /**
     * The segmentation data of the customer.
     */
    @Column(name = "segmentation_data")
    @Lob
    private String segmentationData;


}