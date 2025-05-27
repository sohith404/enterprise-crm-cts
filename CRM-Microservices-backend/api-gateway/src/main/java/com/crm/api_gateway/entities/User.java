package com.crm.api_gateway.entities;

import com.crm.api_gateway.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "your_entity_seq")
    @SequenceGenerator(name = "EmployeeId", sequenceName = "EmployeeIdSequence", initialValue = 15432, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private Role role;
    @Column(unique = true)
    private String email;
    private String password;
    private String img;
}