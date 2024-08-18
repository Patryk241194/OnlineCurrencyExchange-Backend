package com.kodilla.onlinecurrencyexchangebackend.service.domain;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import com.kodilla.onlinecurrencyexchangebackend.nbp.config.DBConfig;
import com.kodilla.onlinecurrencyexchangebackend.nbp.scheduler.NBPScheduler;
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
import java.util.NoSuchElementException;

import static com.kodilla.onlinecurrencyexchangebackend.nbp.scheduler.NBPScheduler.*;
import static com.kodilla.onlinecurrencyexchangebackend.security.log.LogMessages.*;

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
            log.error(ERROR_FETCHING_RATES, e);
            return Collections.emptyList();
        }
    }

    public List<CurrencyExchangeDto> getExchangeRatesByCode(String currencyCode) {
        try (Connection conn = DriverManager.getConnection(dbConfig.getJdbcUrl(), dbConfig.getUsername(), dbConfig.getPassword());
             CallableStatement stmt = conn.prepareCall("{CALL listRatesByCode(?)}")) {
            stmt.setString(1, currencyCode);

            ResultSet rs = stmt.executeQuery();

            return fetchExchangeRatesFromDatabase(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(ERROR_FETCHING_RATES_BY_CODE, e);
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
            log.error(ERROR_FETCHING_RATES_BY_CODE_AND_DATE, e);
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
            log.error(ERROR_FETCHING_RATES_BY_CODE_AND_DATE_RANGE, e);
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
            log.error(ERROR_FETCHING_RATES_BY_DATE, e);
            return Collections.emptyList();
        }
    }

    public List<CurrencyExchangeDto> getExchangeRatesFromDateToDate(LocalDate startingDate, LocalDate endingDate) {
        try (Connection conn = DriverManager.getConnection(dbConfig.getJdbcUrl(), dbConfig.getUsername(), dbConfig.getPassword());
             CallableStatement stmt = conn.prepareCall("{CALL listRatesFromDateToDate(?, ?)}")) {
            stmt.setDate(1, Date.valueOf(startingDate));
            stmt.setDate(2, Date.valueOf(endingDate));

            ResultSet rs = stmt.executeQuery();

            return fetchExchangeRatesFromDatabase(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(ERROR_FETCHING_RATES_BY_DATE_RANGE, e);
            return Collections.emptyList();
        }
    }

    public double getLatestRate(String currencyCode) {
        LocalDate effectiveDate = getEffectiveDate();
        List<CurrencyExchangeDto> rates = getExchangeRatesByCodeAndDate(currencyCode, effectiveDate);
        if (rates.isEmpty()) {
            log.error(String.format(NO_RATES_FOUND, currencyCode, effectiveDate));
        }
        return getAverageRate(rates);
    }

    private double getAverageRate(List<CurrencyExchangeDto> rates) {
        return rates.stream()
                .mapToDouble(dto -> dto.getAverageRate().doubleValue())
                .average()
                .orElseThrow(NoSuchElementException::new);
    }

}


