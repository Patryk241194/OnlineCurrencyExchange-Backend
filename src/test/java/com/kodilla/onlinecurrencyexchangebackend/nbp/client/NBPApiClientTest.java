package com.kodilla.onlinecurrencyexchangebackend.nbp.client;

import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NBPApiClientTest {

    @Autowired
    private NBPApiClient nbpApiClient;

    @Test
    void shouldFetchCurrencyRates() {
        // Given & When
        List<RateDto> ratesC = nbpApiClient.fetchRatesFromTable("C");
        List<RateDto> ratesA = nbpApiClient.fetchRatesFromTable("A");

        // Then
        assertEquals(13, ratesC.size());
        assertEquals(33, ratesA.size());

    }
}