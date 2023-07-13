package com.ecommerceboari.api.auth;

import com.ecommerceboari.api.dto.AuthenticationDTO;
import com.ecommerceboari.api.dto.user.UserLoginDTO;
import com.ecommerceboari.api.dto.user.UserRegisterDTO;
import com.ecommerceboari.api.dto.user.UserRegisterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterDTO request){
        UserRegisterResponseDTO user = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDTO> login(@RequestBody UserLoginDTO request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
}