package com.ecommerceboari.api.auth;

import com.ecommerceboari.api.dto.AuthenticationDTO;
import com.ecommerceboari.api.dto.UserLoginDTO;
import com.ecommerceboari.api.dto.UserRegisterDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.exception.UserForbiddenRequestException;
import com.ecommerceboari.api.exception.UserUnauthorizedRequestException;
import com.ecommerceboari.api.jwt.JwtService;
import com.ecommerceboari.api.model.Role;
import com.ecommerceboari.api.model.User;
import com.ecommerceboari.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationDTO register(UserRegisterDTO request) {
        // check if the email already exists
        boolean emailExist = userRepository.findByEmail(request.getEmail()).isPresent();
        if (emailExist) throw new DataIntegrityViolationException("A user already exists with the same email!");

        var user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationDTO.builder().token(jwtToken).build();
    }

    public AuthenticationDTO login(UserLoginDTO request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadRequestException("Email does not exist!"));

        // verify user credentials with username and password
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            request.getPassword())
            );

        } catch (BadCredentialsException e) {
            throw new BadRequestException("Email or password is incorrect!");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationDTO.builder().token(jwtToken).build();
    }

    public User returnUserAuthenticated() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new UserUnauthorizedRequestException("User not authenticated!"));
    }

    public void isAdminOrSelf(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (!user.getId().equals(userId) && !hasRole(user, Role.ADMIN))
            throw new UserForbiddenRequestException("You don't have permission to access this resource");
    }

    public boolean hasRole(User user, Role role) {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role.name())) return true;
        }
        return false;
    }
}