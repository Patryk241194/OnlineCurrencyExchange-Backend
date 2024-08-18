package com.kodilla.onlinecurrencyexchangebackend.facade;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyExchangeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CurrencyExchangeFacade {

    private final CurrencyExchangeService currencyExchangeService;

    public List<CurrencyExchangeDto> getExchangeRates(String code, LocalDate startingDate, LocalDate endingDate) {
        if (code == null) {
            if (startingDate == null && endingDate == null) {
                return currencyExchangeService.getExchangeRatesFromDatabase();
            } else if (startingDate != null && endingDate == null) {
                return currencyExchangeService.getExchangeRatesByDate(startingDate);
            } else if (startingDate == null) {
                return currencyExchangeService.getExchangeRatesByDate(endingDate);
            } else if (startingDate != null) {
                return currencyExchangeService.getExchangeRatesFromDateToDate(startingDate, endingDate);
            } else {
                throw new IllegalArgumentException("Invalid combination of parameters");
            }
        } else {
            if (startingDate == null && endingDate == null) {
                return currencyExchangeService.getExchangeRatesByCode(code);
            } else if (startingDate != null && endingDate != null) {
                return currencyExchangeService.getExchangeRatesByCodeFromDateToDate(code, startingDate, endingDate);
            } else if (startingDate == null) {
                return currencyExchangeService.getExchangeRatesByCodeAndDate(code, endingDate);
            } else if (startingDate != null) {
                return currencyExchangeService.getExchangeRatesByCodeAndDate(code, startingDate);
            } else {
                throw new IllegalArgumentException("Invalid combination of parameters");
            }
        }
    }
}
