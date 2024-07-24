package com.kodilla.onlinecurrencyexchangebackend.security.dto.registration;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
}
