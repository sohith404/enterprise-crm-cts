    package com.crm.api_gateway.controller;

    import com.crm.api_gateway.dto.GetUserDTO;
    import com.crm.api_gateway.dto.MessageDTO;
    import com.crm.api_gateway.dto.UserRequestDTO;
    import com.crm.api_gateway.dto.UserResponseDTO;
    import com.crm.api_gateway.exception.InvalidCredentialsException;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.NoSuchElementException;

    @RestController
    @RequestMapping("api/authentication")
    @CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.POST})
    public interface UserController {
        @GetMapping
        ResponseEntity<List<?>> getAll();

        @GetMapping("/{id}")
        ResponseEntity<?> getById(@PathVariable Long id);

        @PostMapping("user")
        ResponseEntity<?> getUser(@RequestBody GetUserDTO getUserDTO);
        @PostMapping
        ResponseEntity<?> createUser(@RequestBody UserRequestDTO userRequestDTO);
        @PatchMapping("{id}/{newPassword}")
        ResponseEntity<?> updatePassword(@PathVariable Long id, @PathVariable String newPassword);
        @DeleteMapping("{id}")
        ResponseEntity<MessageDTO> deleteUser(@PathVariable Long id);
    }
