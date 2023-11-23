package com.ecommerceboari.api.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerceboari.api.dto.AuthenticationDTO;
import com.ecommerceboari.api.dto.user.UserLoginRequestDTO;
import com.ecommerceboari.api.dto.user.UserRegisterRequestDTO;
import com.ecommerceboari.api.dto.user.UserRegisterResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody @Valid UserRegisterRequestDTO request) {
        UserRegisterResponseDTO user = authenticationService.register(request);
        LOGGER.info("Received request to register a new user");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDTO> login(@RequestBody @Valid UserLoginRequestDTO request) {
        LOGGER.info("Received request to login a user");
        return ResponseEntity.ok(authenticationService.login(request));
    }
}