package com.kodilla.onlinecurrencyexchangebackend.controller;

import com.kodilla.onlinecurrencyexchangebackend.dto.user.EditUserPasswordRequest;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.EditUserRequest;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.EditUserResponse;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/editUsername")
    public ResponseEntity<EditUserResponse> editUsername(@RequestBody EditUserRequest request, @RequestHeader("Authorization") String authHeader) throws Exception {
        String token = extractToken(authHeader);
        return ResponseEntity.ok(userService.changeUsername(request, token));
    }

    @PutMapping("/editPassword")
    public ResponseEntity<Void> editUsernamePassword(@RequestBody EditUserPasswordRequest request, @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        userService.changeUserPassword(request, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<Void> deleteAccount(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        userService.deleteUser(token);
        return ResponseEntity.ok().build();
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(7);
    }

}