package com.crm.api_gateway.mapper;

import com.crm.api_gateway.dto.UserRequestDTO;
import com.crm.api_gateway.dto.UserResponseDTO;
import com.crm.api_gateway.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO convertToDTO(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .img(user.getImg())
                .build();
    }

    public User convertToUser(UserRequestDTO userRequestDTO){
        return User.builder()
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .role(userRequestDTO.getRole())
                .img(userRequestDTO.getImg())
                .build();
    }
}
