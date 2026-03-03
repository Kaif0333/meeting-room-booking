package com.kaif.meetingroombooking.service;

import com.kaif.meetingroombooking.dto.AuthLoginRequest;
import com.kaif.meetingroombooking.dto.AuthRegisterRequest;
import com.kaif.meetingroombooking.dto.AuthResponse;
import com.kaif.meetingroombooking.model.User;
import com.kaif.meetingroombooking.model.UserRole;
import com.kaif.meetingroombooking.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    public AuthService(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AuditLogService auditLogService
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.auditLogService = auditLogService;
    }

    public AuthResponse register(AuthRegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.setRole(UserRole.USER);
        User savedUser = userService.createUser(user);
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(savedUser.getEmail())
                        .password(savedUser.getPasswordHash())
                        .roles(savedUser.getRole().name())
                        .build(),
                savedUser.getId(),
                savedUser.getRole().name()
        );
        auditLogService.log("USER_REGISTERED", "USER", savedUser.getId(), "Public signup");
        return new AuthResponse(token, savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole().name());
    }

    public AuthResponse login(AuthLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String email = auth.getName();
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPasswordHash())
                        .roles(user.getRole().name())
                        .build(),
                user.getId(),
                user.getRole().name()
        );
        auditLogService.log("USER_LOGIN", "USER", user.getId(), "User logged in");
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse me(String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new AuthResponse(null, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
