package com.kodilla.onlinecurrencyexchangebackend.controller;

import com.kodilla.onlinecurrencyexchangebackend.security.authorization.AuthService;
import com.kodilla.onlinecurrencyexchangebackend.security.dto.registration.AuthRequest;
import com.kodilla.onlinecurrencyexchangebackend.security.dto.registration.AuthResponse;
import com.kodilla.onlinecurrencyexchangebackend.security.dto.registration.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws Exception {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verifyEmail(@RequestParam("token") String token) {
        return authService.verifyEmail(token) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }

}
