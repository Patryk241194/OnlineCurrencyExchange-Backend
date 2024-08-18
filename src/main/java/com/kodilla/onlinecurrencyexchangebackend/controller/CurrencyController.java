package com.kodilla.onlinecurrencyexchangebackend.controller;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyDisplayDto;
import com.kodilla.onlinecurrencyexchangebackend.error.auth.InvalidAuthorizationHeaderException;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/unsubscribe/{currencyCode}")
    public ResponseEntity<Void> unsubscribeUserFromCurrency(
            @PathVariable String currencyCode,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        currencyService.unsubscribeUserFromCurrency(currencyCode, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/observe/{currencyCode}")
    public ResponseEntity<Void> subscribeObserverToCurrency(
            @PathVariable String currencyCode,
            @RequestParam double threshold,
            @RequestParam boolean aboveThreshold,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        currencyService.subscribeObserverToCurrency(currencyCode, threshold, aboveThreshold, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/unobserve/{currencyCode}")
    public ResponseEntity<Void> unsubscribeObserverFromCurrency(
            @PathVariable String currencyCode,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        currencyService.unsubscribeObserverFromCurrency(currencyCode, token);
        return ResponseEntity.ok().build();
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidAuthorizationHeaderException();
        }
        return authHeader.substring(7);
    }

}
