package com.kodilla.onlinecurrencyexchangebackend.service.domain;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import com.kodilla.onlinecurrencyexchangebackend.nbp.config.DBConfig;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class CurrencyExchangeService {

    @Autowired
    private DBConfig dbConfig;

    private CurrencyExchangeDto fromResultSet(ResultSet rs) throws SQLException {
        CurrencyExchangeDto dto = new CurrencyExchangeDto();
        dto.setCurrencyCode(rs.getString("CURRENCY_CODE"));
        dto.setCurrencyName(rs.getString("CURRENCY_NAME"));
        dto.setSellingRate(rs.getBigDecimal("SELLING_RATE"));
        dto.setBuyingRate(rs.getBigDecimal("BUYING_RATE"));
        dto.setAverageRate(rs.getBigDecimal("AVERAGE_RATE"));
        dto.setEffectiveDate(rs.getDate("EFFECTIVE_DATE").toLocalDate());
        return dto;
    }

    public List<CurrencyExchangeDto> fetchExchangeRatesFromDatabase(ResultSet resultSet) throws SQLException {
        List<CurrencyExchangeDto> currencyExchangeList = new ArrayList<>();

        while (resultSet.next()) {
            currencyExchangeList.add(fromResultSet(resultSet));
        }

        return currencyExchangeList;
    }

    public List<CurrencyExchangeDto> getExchangeRatesFromDatabase() {
        try (Connection conn = DriverManager.getConnection(dbConfig.getJdbcUrl(), dbConfig.getUsername(), dbConfig.getPassword());
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("CALL listRates()");

            return fetchExchangeRatesFromDatabase(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error while fetching exchange rates from the database", e);
            return Collections.emptyList();
        }
    }

    public List<CurrencyExchangeDto> getExchangeRatesByCodeAndDate(String currencyCode, LocalDate effectiveDate) {
        try (Connection conn = DriverManager.getConnection(dbConfig.getJdbcUrl(), dbConfig.getUsername(), dbConfig.getPassword());
             CallableStatement stmt = conn.prepareCall("{CALL listRatesByCodeAndDate(?, ?)}")) {
            stmt.setString(1, currencyCode);
            stmt.setDate(2, Date.valueOf(effectiveDate));

            ResultSet rs = stmt.executeQuery();

            return fetchExchangeRatesFromDatabase(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error while fetching exchange rates by code and date from the database", e);
            return Collections.emptyList();
        }
    }

    public List<CurrencyExchangeDto> getExchangeRatesByCodeFromDateToDate(String currencyCode, LocalDate startingDate, LocalDate endingDate) {
        try (Connection conn = DriverManager.getConnection(dbConfig.getJdbcUrl(), dbConfig.getUsername(), dbConfig.getPassword());
             CallableStatement stmt = conn.prepareCall("{CALL listRatesByCodeFromDateToDate(?, ?, ?)}")) {
            stmt.setString(1, currencyCode);
            stmt.setDate(2, Date.valueOf(startingDate));
            stmt.setDate(3, Date.valueOf(endingDate));

            ResultSet rs = stmt.executeQuery();

            return fetchExchangeRatesFromDatabase(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error while fetching exchange rates by code and date range from the database", e);
            return Collections.emptyList();
        }
    }

    public List<CurrencyExchangeDto> getExchangeRatesByDate(LocalDate effectiveDate) {
        try (Connection conn = DriverManager.getConnection(dbConfig.getJdbcUrl(),
                dbConfig.getUsername(), dbConfig.getPassword());
             CallableStatement stmt = conn.prepareCall("{CALL listRatesByDate(?)}")) {
            stmt.setDate(1, Date.valueOf(effectiveDate));

            ResultSet rs = stmt.executeQuery();

            return fetchExchangeRatesFromDatabase(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error while fetching exchange rates by date from the database", e);
            return Collections.emptyList();
        }
    }

}


