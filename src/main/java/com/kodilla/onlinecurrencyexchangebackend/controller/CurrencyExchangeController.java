package com.kodilla.onlinecurrencyexchangebackend.controller;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @GetMapping("/rates")
    public ResponseEntity<List<CurrencyExchangeDto>> getExchangeRates() {
        return ResponseEntity.ok(currencyExchangeService.getExchangeRatesFromDatabase());
    }

    @GetMapping("/rates/date/today")
    public ResponseEntity<List<CurrencyExchangeDto>> getExchangeRatesByToday() {
        LocalDate today = LocalDate.now();
        return ResponseEntity.ok(currencyExchangeService.getExchangeRatesByDate(today));
    }

    @GetMapping("/rates/date/{date}")
    public ResponseEntity<List<CurrencyExchangeDto>> getExchangeRatesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(currencyExchangeService.getExchangeRatesByDate(date));
    }

    @GetMapping("/rates/{code}/{date}")
    public ResponseEntity<List<CurrencyExchangeDto>> getExchangeRatesByCodeAndDate(
            @PathVariable String code,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(currencyExchangeService.getExchangeRatesByCodeAndDate(code, date));
    }

    @GetMapping("/rates/dateToDate/{code}")
    public ResponseEntity<List<CurrencyExchangeDto>> getExchangeRatesByCodeFromDateToDate(
            @PathVariable String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startingDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endingDate) {
        return ResponseEntity.ok(currencyExchangeService.getExchangeRatesByCodeFromDateToDate(code, startingDate, endingDate));
    }


}
