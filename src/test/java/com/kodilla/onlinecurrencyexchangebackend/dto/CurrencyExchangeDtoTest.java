package com.kodilla.onlinecurrencyexchangebackend.dto;

import com.kodilla.onlinecurrencyexchangebackend.nbp.config.DBConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeDtoTest {

    private static ResultSet resultSet;
    CurrencyExchangeDto dto;
    private DBConfig dbConfig;

    @BeforeAll
    static void setUp() throws SQLException {
        // Given
        resultSet = mock(ResultSet.class);
        when(resultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(resultSet.getString("CURRENCY_CODE")).thenReturn("USD").thenReturn("EUR");
        when(resultSet.getString("CURRENCY_NAME")).thenReturn("US Dollar").thenReturn("Euro");
        when(resultSet.getBigDecimal("SELLING_RATE")).thenReturn(new BigDecimal("4.0")).thenReturn(new BigDecimal("4.2"));
        when(resultSet.getBigDecimal("BUYING_RATE")).thenReturn(new BigDecimal("3.8")).thenReturn(new BigDecimal("4.0"));
        when(resultSet.getBigDecimal("AVERAGE_RATE")).thenReturn(new BigDecimal("3.9")).thenReturn(new BigDecimal("4.1"));
        when(resultSet.getDate("EFFECTIVE_DATE")).thenReturn(Date.valueOf(LocalDate.of(2024, 01, 23))).thenReturn(Date.valueOf(LocalDate.of(2024, 01, 24)));
    }

    @BeforeEach
    void init() {
        dto = new CurrencyExchangeDto();
    }

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
    @DisplayName("Test fetching exchange rates from mock ResultSet")
    void getExchangeRatesFromMockTest() throws SQLException {
        // When
//        List<CurrencyExchangeDto> currencyExchangeList = dto.getExchangeRatesFromDatabase();
        List<CurrencyExchangeDto> currencyExchangeList = dto.fetchExchangeRatesFromDatabase(resultSet);
        CurrencyExchangeDto resultDto1 = currencyExchangeList.get(0);
        CurrencyExchangeDto resultDto2 = currencyExchangeList.get(1);
        System.out.println(resultDto1);
        System.out.println(resultDto2);

        // Then
        assertEquals(2, currencyExchangeList.size());
        // 1st record
        assertEquals("USD", resultDto1.getCurrencyCode());
        assertEquals("US Dollar", resultDto1.getCurrencyName());
        assertEquals(new BigDecimal("4.0"), resultDto1.getSellingRate());
        assertEquals(new BigDecimal("3.8"), resultDto1.getBuyingRate());
        assertEquals(new BigDecimal("3.9"), resultDto1.getAverageRate());
        assertEquals(LocalDate.of(2024, 01, 23), resultDto1.getEffectiveDate());
        // 2nd record
        assertEquals("EUR", resultDto2.getCurrencyCode());
        assertEquals("Euro", resultDto2.getCurrencyName());
        assertEquals(new BigDecimal("4.2"), resultDto2.getSellingRate());
        assertEquals(new BigDecimal("4.0"), resultDto2.getBuyingRate());
        assertEquals(new BigDecimal("4.1"), resultDto2.getAverageRate());
        assertEquals(LocalDate.of(2024, 01, 24), resultDto2.getEffectiveDate());
    }
}