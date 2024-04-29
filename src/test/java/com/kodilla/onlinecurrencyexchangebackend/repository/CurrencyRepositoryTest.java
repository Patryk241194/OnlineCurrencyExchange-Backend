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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    private Currency currency;

    @BeforeEach
    void setUp() {
        currency = Currency.builder()
                .code("USD")
                .name("dolar amerykański")
                .build();
        currencyRepository.save(currency);
    }

    @Test
    void currencyRepositoryCreateTest() {
        // When
        currencyRepository.save(currency);

        // Then
        Currency foundCurrency = currencyRepository.findById(currency.getId()).orElse(null);
        assertEquals(currency, foundCurrency);
    }

    @Test
    void currencyRepositoryUpdateTest() {
        // Given
        String newCode = "EUR";
        String newName = "Euro";

        // When
        currency.setCode(newCode);
        currency.setName(newName);
        currencyRepository.save(currency);

        // Then
        Currency updatedCurrency = currencyRepository.findById(currency.getId()).orElse(null);
        assertNotNull(updatedCurrency);
        assertEquals(newName, updatedCurrency.getName());
        assertEquals(newCode, updatedCurrency.getCode());
    }

    @Test
    void currencyRepositoryDeleteTest() {
        // When
        Long id = currency.getId();
        currencyRepository.deleteById(currency.getId());

        // Then
        assertFalse(currencyRepository.existsById(id));
    }

    @Test
    void oneToManyRelationBetweenCurrencyAndExchangeRateTest() {
        // Given
        ExchangeRate exchangeRate1 = ExchangeRate.builder()
                .sellingRate(BigDecimal.valueOf(4.0149))
                .buyingRate(BigDecimal.valueOf(3.9353))
                .averageRate(BigDecimal.valueOf(3.9850))
                .effectiveDate(LocalDate.now())
                .currency(currency)
                .build();
        exchangeRateRepository.save(exchangeRate1);

        ExchangeRate exchangeRate2 = ExchangeRate.builder()
                .sellingRate(BigDecimal.valueOf(4.0286))
                .buyingRate(BigDecimal.valueOf(3.9488))
                .averageRate(BigDecimal.valueOf(3.9684))
                .effectiveDate(LocalDate.now().minusDays(1))
                .currency(currency)
                .build();
        exchangeRateRepository.save(exchangeRate2);

        // When
        currency.getExchangeRates().add(exchangeRate1);
        currency.getExchangeRates().add(exchangeRate2);

        // Then
        assertEquals(2, currency.getExchangeRates().size());
        assertEquals(currency.getCode(), exchangeRate1.getCurrency().getCode());
        assertEquals(currency.getCode(), exchangeRate2.getCurrency().getCode());
    }

    @Test
    void findByCodeTest() {
        // When
        Optional<Currency> foundCurrencyOptional = currencyRepository.findByCode(currency.getCode());

        // Then
        assertTrue(foundCurrencyOptional.isPresent());
        Currency foundCurrency = foundCurrencyOptional.get();
        assertEquals(currency.getCode(), foundCurrency.getCode());
        assertEquals(currency.getName(), foundCurrency.getName());
    }

    @Test
    void findFirstByCodeTest() {
        // When
        Optional<Currency> foundCurrencyOptional = currencyRepository.findFirstByCode(currency.getCode());

        // Then
        assertTrue(foundCurrencyOptional.isPresent());
        Currency foundCurrency = foundCurrencyOptional.get();
        assertEquals(currency.getCode(), foundCurrency.getCode());
        assertEquals(currency.getName(), foundCurrency.getName());
    }


}