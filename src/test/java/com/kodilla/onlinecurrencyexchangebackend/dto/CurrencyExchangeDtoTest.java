//package com.kodilla.onlinecurrencyexchangebackend.dto;
//
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.sql.*;
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class CurrencyExchangeDtoTest {
//
//    @Test
//    void getExchangeRatesFromMockTest() throws SQLException {
//        // Given
//        ResultSet resultSet = mock(ResultSet.class);
//        when(resultSet.next()).thenReturn(true, false);
//        when(resultSet.getString("currencyCode")).thenReturn("USD");
//        when(resultSet.getString("currencyName")).thenReturn("US Dollar");
//        when(resultSet.getBigDecimal("sellingRate")).thenReturn(new BigDecimal("4.0"));
//        when(resultSet.getBigDecimal("buyingRate")).thenReturn(new BigDecimal("3.8"));
//        when(resultSet.getBigDecimal("averageRate")).thenReturn(new BigDecimal("3.9"));
//        when(resultSet.getDate("effectiveDate")).thenReturn(Date.valueOf(LocalDate.now()));
//
//        CurrencyExchangeDto dto = new CurrencyExchangeDto();
//
//        // When
//        List<CurrencyExchangeDto> currencyExchangeList = dto.fetchExchangeRatesFromDatabase(resultSet);
//
//        // Then
//        assertEquals(1, currencyExchangeList.size());
//        CurrencyExchangeDto resultDto = currencyExchangeList.get(0);
//        assertEquals("USD", resultDto.getCurrencyCode());
//        assertEquals("US Dollar", resultDto.getCurrencyName());
//        assertEquals(new BigDecimal("4.0"), resultDto.getSellingRate());
//        assertEquals(new BigDecimal("3.8"), resultDto.getBuyingRate());
//        assertEquals(new BigDecimal("3.9"), resultDto.getAverageRate());
//        assertEquals(LocalDate.now(), resultDto.getEffectiveDate());
//    }
//
//    @Test
//    void testDatabaseConnection() {
//        // Given
//        String jdbcUrl = "jdbc:mysql://localhost:3306/currency_exchange_project?serverTimezone=Europe/Warsaw&useSSL=False&allowPublicKeyRetrieval=true";
//        String username = "oce_admin";
//        String password = "oce_password";
//
//        // When
//        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
//            // Then
//            assertNotNull(conn);
//        } catch (SQLException e) {
//            e.printStackTrace(); // Logger
//            fail("Failed to establish a database connection.");
//        }
//    }
//}