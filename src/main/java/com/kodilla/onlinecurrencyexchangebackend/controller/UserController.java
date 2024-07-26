package com.kodilla.onlinecurrencyexchangebackend.controller;

import com.kodilla.onlinecurrencyexchangebackend.dto.user.EditUserPasswordRequest;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.EditUserRequest;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.EditUserResponse;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.UserDetailResponse;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDetailResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<UserDetailResponse> findUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/promote")
    public ResponseEntity<Void> promoteUserToAdmin(@RequestBody EditUserRequest request, @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        userService.promoteUserToAdmin(request, token);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/demote")
    public ResponseEntity<Void> demoteAdminToUser(@RequestBody EditUserRequest request, @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        userService.demoteAdminToUser(request, token);
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