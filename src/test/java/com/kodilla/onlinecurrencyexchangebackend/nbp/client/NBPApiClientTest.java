package com.kodilla.onlinecurrencyexchangebackend.nbp.client;

import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateDto;
import com.kodilla.onlinecurrencyexchangebackend.nbp.tables.NBPTableType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NBPApiClientTest {

    @Autowired
    private NBPApiClient nbpApiClient;

    @Test
    void shouldFetchCurrencyRates() {
        // Given & When
        List<RateDto> ratesC = nbpApiClient.fetchRatesFromTable(NBPTableType.C.getCode(), LocalDate.now());
        List<RateDto> ratesA = nbpApiClient.fetchRatesFromTable(NBPTableType.A.getCode(), LocalDate.now());

        // Then
        assertEquals(13, ratesC.size());
        assertEquals(33, ratesA.size());

    }
}