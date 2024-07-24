package com.kodilla.onlinecurrencyexchangebackend.security.dto.registration;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}
