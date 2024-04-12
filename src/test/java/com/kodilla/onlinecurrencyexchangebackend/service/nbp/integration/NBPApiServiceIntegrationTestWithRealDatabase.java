package com.kodilla.onlinecurrencyexchangebackend.service.nbp.integration;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.ExchangeRateRepository;
import com.kodilla.onlinecurrencyexchangebackend.service.nbp.NBPApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class NBPApiServiceIntegrationTestWithRealDatabase {

    @Autowired
    private NBPApiService nbpApiService;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void shouldSaveRatesToDatabase() {
        // Given
        nbpApiService.updateCurrencyRates();

        // When
        List<Currency> listOfCurrencies = currencyRepository.findAll();
        List<ExchangeRate> listOfExchangeRates = exchangeRateRepository.findAll();
        System.out.println(listOfCurrencies);
        System.out.println(listOfExchangeRates);

        // Then
        assertEquals(13, listOfCurrencies.size());
        assertEquals(52, listOfExchangeRates.size());
    }

    @Test
    void shouldSaveRatesToDatabaseDuringBusinessDay() {
        // Given
        LocalDate effectiveDate = LocalDate.of(2024, 01, 23);
        nbpApiService.updateCurrencyRatesWithDate(effectiveDate);

        // When
        List<Currency> listOfCurrencies = currencyRepository.findAll();
        List<ExchangeRate> listOfExchangeRates = exchangeRateRepository.findAll();
        System.out.println(listOfCurrencies);
        System.out.println(listOfExchangeRates);

        // Then
        assertEquals(13, listOfCurrencies.size());
        assertEquals(39, listOfExchangeRates.size());
    }

    @Test
    void shouldNotSaveRatesToDatabaseWithDuringWeekendDay() {
        // Given
        LocalDate effectiveDate = LocalDate.of(2024, 01, 21);
        nbpApiService.updateCurrencyRatesWithDate(effectiveDate);

        // When
        List<Currency> listOfCurrencies = currencyRepository.findAll();
        List<ExchangeRate> listOfExchangeRates = exchangeRateRepository.findAll();
        System.out.println(listOfCurrencies);
        System.out.println(listOfExchangeRates);

        // Then
        assertEquals(13, listOfCurrencies.size());
        assertEquals(39, listOfExchangeRates.size());
    }
}
