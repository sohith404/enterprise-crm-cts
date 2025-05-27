package com.crm.api_gateway.controller;

import com.crm.api_gateway.dto.*;
import com.crm.api_gateway.exception.InvalidCredentialsException;
import com.crm.api_gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class UserControllerImpl implements UserController{
    /**
     * @param getUserDTO
     * @return
     */
    @Autowired
    private UserService service;

    /**
     * @return
     */
    @Override
    public ResponseEntity<List<?>> getAll() {
        return new ResponseEntity<>(service.getAllUser(),HttpStatus.OK);
    }
    @Override
    public ResponseEntity<UserResponseDTO> getById(Long id) throws NoSuchElementException {
        return new ResponseEntity<>(service.getUserById(id),HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getUser(GetUserDTO getUserDTO){
        try {
            return new ResponseEntity<>(service.getUser(getUserDTO.getEmail(), getUserDTO.getPassword()), HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                    .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                    .timestamp(LocalDateTime.now())
                    .message(e.getMessage())
                    .build();

            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * @param userRequestDTO
     * @return
     */
    @Override
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(service.createUser(userRequestDTO), HttpStatus.OK);
    }

    /**
     * @param id
     * @param newPassword
     * @return
     */
    @Override
    public ResponseEntity<?> updatePassword(Long id, String newPassword) {
        try {
            return new ResponseEntity<>(service.changePassword(id, newPassword), HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                    .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                    .timestamp(LocalDateTime.now())
                    .message(e.getMessage())
                    .build();

            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<MessageDTO> deleteUser(Long id) {
        return new ResponseEntity<>(service.deleteUser(id), HttpStatus.OK);
    }
}
