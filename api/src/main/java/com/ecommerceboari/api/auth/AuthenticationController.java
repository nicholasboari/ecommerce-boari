package com.ecommerceboari.api.auth;

import com.ecommerceboari.api.dto.AuthenticationDTO;
import com.ecommerceboari.api.dto.user.UserLoginRequestDTO;
import com.ecommerceboari.api.dto.user.UserRegisterRequestDTO;
import com.ecommerceboari.api.dto.user.UserRegisterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterRequestDTO request){
        UserRegisterResponseDTO user = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDTO> login(@RequestBody UserLoginRequestDTO request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
}