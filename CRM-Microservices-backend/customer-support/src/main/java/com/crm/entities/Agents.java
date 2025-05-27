package com.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "agents")
public class Agents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="agent_id")
    private Long agentID;
    private String firstName;
    private String lastName;
}