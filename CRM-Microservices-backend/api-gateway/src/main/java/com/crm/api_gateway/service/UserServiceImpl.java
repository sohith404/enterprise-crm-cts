package com.crm.api_gateway.service;


import com.crm.api_gateway.dto.MessageDTO;
import com.crm.api_gateway.dto.UserRequestDTO;
import com.crm.api_gateway.dto.UserResponseDTO;
import com.crm.api_gateway.entities.User;
import com.crm.api_gateway.exception.InvalidCredentialsException;
import com.crm.api_gateway.mapper.UserMapper;
import com.crm.api_gateway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper userMapper;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * @return
     */
    @Override
    public List<UserResponseDTO> getAllUser() {
       return repository.findAll().stream()
               .map(userMapper::convertToDTO)
               .toList();
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.convertToUser(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        return userMapper.convertToDTO(repository.save(user));
    }

    /**
     * @param id
     * @param newPassword
     * @return UserResponseDTO if password changed successfully

     */
    @Override
    public UserResponseDTO changePassword(Long id, String newPassword) throws InvalidCredentialsException {
        User user = repository.findById(id)
                .orElseThrow(() -> new InvalidCredentialsException("User with " + id + " not found!"));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userMapper.convertToDTO(repository.save(user));
    }

    /**
     * @param email
     * @param password (encrypted)
     * @return UserResponseDTO if user found and password matches
     */
    @Override
    public UserResponseDTO getUser(String email, String password) throws InvalidCredentialsException {
        log.info("incoming pass -> {}",password);
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User with " + email + " not found!"));

        if(passwordEncoder.matches(password, user.getPassword())){
        return userMapper.convertToDTO(user);
        }
        else{
            throw new InvalidCredentialsException("Password is invalid!");
        }
    }

    @Override
    public UserResponseDTO getUserById(long id) throws NoSuchElementException {
        User user = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with " + id + " not found!"));

        return userMapper.convertToDTO(user);
    }

    /**
     * @param id
     * @return MessageDTO indicating success or failure of deletion.
     */
    @Override
    public MessageDTO deleteUser(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return MessageDTO.builder()
                    .message("User with ID " + id + " deleted successfully.")
                    .status("success")
                    .build();
        } else {
            return MessageDTO.builder()
                    .message("User with ID " + id + " not found.")
                    .status("failure")
                    .build();
        }
    }
}