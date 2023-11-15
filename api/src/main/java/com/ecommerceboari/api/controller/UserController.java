package com.ecommerceboari.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getUser() {
        UserResponseDTO user = userService.findUserAuthenticated();
        LOGGER.info("Received request to fetch logged in user details");
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(Pageable pageable) {
        Page<UserResponseDTO> allUsers = userService.findAll(pageable);
        LOGGER.info("Received request to fetch all users paginated");
        return ResponseEntity.ok().body(allUsers);
    }
}
