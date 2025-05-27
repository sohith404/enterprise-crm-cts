package com.crm.api_gateway.dto;

import com.crm.api_gateway.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private Role role;
    private String email;
    private String password;
    private String img;
}
