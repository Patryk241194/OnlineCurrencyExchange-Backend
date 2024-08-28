package com.kodilla.onlinecurrencyexchangebackend.nbp.cleanup;

import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExchangeRateCleanUpSchedulerTest {

    @Autowired
    private ExchangeRateCleanUpScheduler exchangeRateCleanUpScheduler;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    void shouldCleanUp() {
        // Given & When
        exchangeRateCleanUpScheduler.cleanUpOldExchangeRates();
        List<ExchangeRate> listOfRates = exchangeRateRepository.findAll();

        //Then
        assertEquals(1300, listOfRates.size());
    }

}