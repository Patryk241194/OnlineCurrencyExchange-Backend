//package com.kodilla.onlinecurrencyexchangebackend.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Slf4j
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class CurrencyExchangeDto {
//
//    private String currencyCode;
//    private String currencyName;
//    private BigDecimal sellingRate;
//    private BigDecimal buyingRate;
//    private BigDecimal averageRate;
//    private LocalDate effectiveDate;
//
//
//    public static CurrencyExchangeDto fromResultSet(ResultSet rs) throws SQLException {
//        CurrencyExchangeDto dto = new CurrencyExchangeDto();
//        dto.setCurrencyCode(rs.getString("currencyCode"));
//        dto.setCurrencyName(rs.getString("currencyName"));
//        dto.setSellingRate(rs.getBigDecimal("sellingRate"));
//        dto.setBuyingRate(rs.getBigDecimal("buyingRate"));
//        dto.setAverageRate(rs.getBigDecimal("averageRate"));
//        dto.setEffectiveDate(rs.getDate("effectiveDate").toLocalDate());
//        return dto;
//    }
//
//    public List<CurrencyExchangeDto> fetchExchangeRatesFromDatabase(ResultSet resultSet) throws SQLException {
//        List<CurrencyExchangeDto> currencyExchangeList = new ArrayList<>();
//
//        while (resultSet.next()) {
//            currencyExchangeList.add(CurrencyExchangeDto.fromResultSet(resultSet));
//        }
//
//        return currencyExchangeList;
//    }
//
//    public List<CurrencyExchangeDto> getExchangeRatesFromDatabase() {
//        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/currency_exchange_project?serverTimezone=Europe/Warsaw&useSSL=False&allowPublicKeyRetrieval=true",
//                "oce_admin", "oce_password");
//             Statement stmt = conn.createStatement()) {
//            ResultSet rs = stmt.executeQuery("CALL listExchangeRates()");
//
//            return fetchExchangeRatesFromDatabase(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            log.error("Error while fetching exchange rates from the database", e);
//            return Collections.emptyList();
//        }
//    }
//}