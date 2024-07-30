package com.kodilla.onlinecurrencyexchangebackend.controller;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyDisplayDto;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDisplayDto>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencyDisplayDtos());
    }

    @PutMapping("/subscribe/{currencyCode}")
    public ResponseEntity<Void> subscribeUserToCurrency(
            @PathVariable String currencyCode,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        currencyService.subscribeUserToCurrency(currencyCode, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unsubscribe/{currencyCode}")
    public ResponseEntity<Void> unsubscribeUserFromCurrency(
            @PathVariable String currencyCode,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        currencyService.unsubscribeUserFromCurrency(currencyCode, token);
        return ResponseEntity.ok().build();
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(7);
    }


}
