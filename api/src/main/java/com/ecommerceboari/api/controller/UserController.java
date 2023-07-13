package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getUser(){
        UserResponseDTO user = userService.findUserAuthenticated();
        return ResponseEntity.ok().body(user);
    }
}
