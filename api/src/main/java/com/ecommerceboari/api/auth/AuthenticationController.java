package com.ecommerceboari.api.auth;

import com.ecommerceboari.api.dto.AuthenticationDTO;
import com.ecommerceboari.api.dto.UserLoginDTO;
import com.ecommerceboari.api.dto.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDTO> register(@RequestBody UserRegisterDTO request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDTO> login(@RequestBody UserLoginDTO request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
}