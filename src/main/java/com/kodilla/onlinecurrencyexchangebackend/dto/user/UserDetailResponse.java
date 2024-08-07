package com.kodilla.onlinecurrencyexchangebackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    private String username;
    private String email;
    private String role;
    private List<String> subscribedCurrencies;
}
