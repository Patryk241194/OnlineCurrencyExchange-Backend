package com.kodilla.onlinecurrencyexchangebackend.dto.integration;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CurrencyExchangeDtoIntegrationTest {

    @Autowired
    private CurrencyExchangeDto currencyExchangeDto;

    @Test
    void shouldGetExchangeRatesFromDatabaseTest() throws SQLException {
        // When
        List<CurrencyExchangeDto> currencyExchangeList = currencyExchangeDto.getExchangeRatesFromDatabase();
        for (int i = 0; i <= 23; i++) {
            System.out.println(currencyExchangeList.get(i));
        }
        // Then
        assertEquals(26, currencyExchangeList.size());
//        // 1st record
//        assertEquals("USD", resultDto1.getCurrencyCode());
//        assertEquals("US Dollar", resultDto1.getCurrencyName());
//        assertEquals(new BigDecimal("4.0"), resultDto1.getSellingRate());
//        assertEquals(new BigDecimal("3.8"), resultDto1.getBuyingRate());
//        assertEquals(new BigDecimal("3.9"), resultDto1.getAverageRate());
//        assertEquals(LocalDate.of(2024, 01, 23), resultDto1.getEffectiveDate());
//        // 2nd record
//        assertEquals("EUR", resultDto2.getCurrencyCode());
//        assertEquals("Euro", resultDto2.getCurrencyName());
//        assertEquals(new BigDecimal("4.2"), resultDto2.getSellingRate());
//        assertEquals(new BigDecimal("4.0"), resultDto2.getBuyingRate());
//        assertEquals(new BigDecimal("4.1"), resultDto2.getAverageRate());
//        assertEquals(LocalDate.of(2024, 01, 24), resultDto2.getEffectiveDate());
    }
}