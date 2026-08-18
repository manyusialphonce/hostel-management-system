package com.hostelms.controller;

import com.hostelms.dto.AuthResponse;
import com.hostelms.dto.LoginRequest;
import com.hostelms.model.AppUser;
import com.hostelms.security.JwtService;
import com.hostelms.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserService appUserService;
    private final JwtService jwtService;

    public AuthController(AppUserService appUserService, JwtService jwtService) {
        this.appUserService = appUserService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<AppUser> maybeUser = appUserService.findByUsername(request.getUsername());

        if (maybeUser.isEmpty() || !appUserService.passwordMatches(maybeUser.get(), request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }

        AppUser user = maybeUser.get();
        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "This account has been disabled"));
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getFullName(), user.getRole().name()));
    }
}
