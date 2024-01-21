package com.kodilla.onlinecurrencyexchangebackend.repository;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ExchangeRateRepositoryTest {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    private Currency currency;
    private ExchangeRate exchangeRate;

    @BeforeEach
    void setUp() {
        currency = Currency.builder()
                .code("USD")
                .name("US Dollar")
                .build();
        currencyRepository.save(currency);

        exchangeRate = ExchangeRate.builder()
                .sellingRate(BigDecimal.valueOf(4.0149))
                .buyingRate(BigDecimal.valueOf(3.9353))
                .averageRate(BigDecimal.valueOf(3.9850))
                .effectiveDate(LocalDate.now())
                .currency(currency)
                .build();
        exchangeRateRepository.save(exchangeRate);
        currency.getExchangeRates().add(exchangeRate);
        currencyRepository.save(currency);
    }

    @Test
    void exchangeRateRepositoryCreateTest() {
        // When
        ExchangeRate foundExchangeRate = exchangeRateRepository.findById(exchangeRate.getId()).orElse(null);

        // Then
        assertEquals(exchangeRate, foundExchangeRate);
    }

    @Test
    void exchangeRateRepositoryUpdateTest() {
        // Given
        BigDecimal newSellingRate = BigDecimal.valueOf(4.0286);

        // When
        exchangeRate.setSellingRate(newSellingRate);
        exchangeRateRepository.save(exchangeRate);

        // Then
        ExchangeRate updatedExchangeRate = exchangeRateRepository.findById(exchangeRate.getId()).orElse(null);
        assertNotNull(updatedExchangeRate);
        assertEquals(newSellingRate, updatedExchangeRate.getSellingRate());
    }

    @Test
    void exchangeRateRepositoryDeleteTest() {
        // When
        Long id = exchangeRate.getId();
        exchangeRateRepository.deleteById(exchangeRate.getId());

        // Then
        assertFalse(exchangeRateRepository.existsById(id));
    }
}