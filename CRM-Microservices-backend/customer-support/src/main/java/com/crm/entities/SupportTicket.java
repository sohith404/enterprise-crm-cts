package com.crm.entities;

import com.crm.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "support_ticket")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false)
    private Long ticketID;

    //Foreign Key
    @Column(name = "customer_id")
    private Long customerID;

    @Column(name = "issue_description",nullable = false)
    private String issueDescription;

    @Column(name = "assigned_agent")
    private Long assignedAgent;

    @Enumerated(EnumType.STRING)
    private Status status;

}