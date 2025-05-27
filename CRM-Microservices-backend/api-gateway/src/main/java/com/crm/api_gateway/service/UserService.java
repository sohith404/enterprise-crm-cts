package com.crm.api_gateway.service;

import com.crm.api_gateway.dto.MessageDTO;
import com.crm.api_gateway.dto.UserRequestDTO;
import com.crm.api_gateway.dto.UserResponseDTO;
import com.crm.api_gateway.exception.InvalidCredentialsException;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserService {
    List<UserResponseDTO> getAllUser();
    UserResponseDTO getUserById(long id) throws NoSuchElementException;
    UserResponseDTO createUser(UserRequestDTO userRequestDTO) ;
    UserResponseDTO changePassword(Long id, String password) throws InvalidCredentialsException;
    UserResponseDTO getUser(String email, String password) throws InvalidCredentialsException;
    MessageDTO deleteUser(Long id);
}
