package com.kodilla.onlinecurrencyexchangebackend.controller;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyDisplayDto;
import com.kodilla.onlinecurrencyexchangebackend.error.auth.InvalidAuthorizationHeaderException;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/subscribe/{currencyCode}")
    public ResponseEntity<Void> subscribeUserToCurrency(
            @PathVariable String currencyCode,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        currencyService.subscribeUserToCurrency(currencyCode, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/unsubscribe/{currencyCode}")
    public ResponseEntity<Void> unsubscribeUserFromCurrencyViaWebsite(
            @PathVariable String currencyCode,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        currencyService.unsubscribeUserFromCurrencyViaWebsite(currencyCode, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribeViaEmail/{currencyCode}")
    public ResponseEntity<Void> unsubscribeUserFromCurrencyViaEmail(
            @PathVariable String currencyCode,
            @RequestParam("token") String token) {
        currencyService.unsubscribeUserFromCurrencyViaEmail(currencyCode, token);
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
    public ResponseEntity<Void> unsubscribeObserverFromCurrencyViaWebsite(
            @PathVariable String currencyCode,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        currencyService.unsubscribeObserverFromCurrencyViaWebsite(currencyCode, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unobserveViaEmail/{currencyCode}")
    public ResponseEntity<Void> unsubscribeObserverFromCurrencyViaEmail(
            @PathVariable String currencyCode,
            @RequestParam("token") String token) {
        currencyService.unsubscribeObserverFromCurrencyViaEmail(currencyCode, token);
        return ResponseEntity.ok().build();
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidAuthorizationHeaderException();
        }
        return authHeader.substring(7);
    }

}
