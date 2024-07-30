package com.kodilla.onlinecurrencyexchangebackend.dto.integration;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import com.kodilla.onlinecurrencyexchangebackend.nbp.config.DBConfig;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyExchangeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyExchangeDtoIntegrationTestWithRealDatabase {

    @Autowired
    private CurrencyExchangeService currencyExchangeService;
    @Autowired
    private DBConfig dbConfig;

    @Test
    void testDatabaseConnection() {
        // Given
        String jdbcUrl = dbConfig.getJdbcUrl();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();

        // When & Then
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            assertNotNull(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to establish a database connection.");
        }
    }

    @Test
    void shouldGetExchangeRatesFromDatabaseTest() throws SQLException {
        // When
        List<CurrencyExchangeDto> currencyExchangeList = currencyExchangeService.getExchangeRatesFromDatabase();

        // Then
        assertNotNull(currencyExchangeList);
        assertFalse(currencyExchangeList.isEmpty());
        currencyExchangeList.forEach(System.out::println);
    }

    @Test
    void shouldGetExchangeRatesByCodeAndDateFromDatabaseTest() throws SQLException {
        // When
        List<CurrencyExchangeDto> currencyExchangeList = currencyExchangeService.getExchangeRatesByCodeAndDate("USD", LocalDate.of(2024, 01, 25));

        // Then
        assertNotNull(currencyExchangeList);
        assertFalse(currencyExchangeList.isEmpty());
        currencyExchangeList.forEach(System.out::println);

    }

    @Test
    void testGetExchangeRatesByCodeFromDateToDate() {
        // Given
        String currencyCode = "EUR";
        LocalDate startingDate = LocalDate.of(2024, 1, 1);
        LocalDate endingDate = LocalDate.of(2024, 1, 31);

        // When
        List<CurrencyExchangeDto> currencyExchangeList = currencyExchangeService.getExchangeRatesByCodeFromDateToDate(currencyCode, startingDate, endingDate);

        // Then
        assertNotNull(currencyExchangeList);
        assertFalse(currencyExchangeList.isEmpty());
        currencyExchangeList.forEach(System.out::println);
    }

    @Test
    void testGetExchangeRatesByDate() {
        // Given
        LocalDate effectiveDate = LocalDate.of(2024, 1, 23);

        // When
        List<CurrencyExchangeDto> currencyExchangeList = currencyExchangeService.getExchangeRatesByDate(effectiveDate);

        // Then
        assertNotNull(currencyExchangeList);
        assertFalse(currencyExchangeList.isEmpty());
        currencyExchangeList.forEach(System.out::println);
    }
}