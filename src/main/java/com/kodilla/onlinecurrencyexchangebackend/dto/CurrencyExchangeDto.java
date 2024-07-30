package com.kodilla.onlinecurrencyexchangebackend.dto;

import com.kodilla.onlinecurrencyexchangebackend.nbp.config.DBConfig;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CurrencyExchangeDto {

    private String currencyCode;
    private String currencyName;
    private BigDecimal sellingRate;
    private BigDecimal buyingRate;
    private BigDecimal averageRate;
    private LocalDate effectiveDate;

    @Override
    public String toString() {
        return "CurrencyExchangeDto{" +
                "currencyCode='" + currencyCode + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", sellingRate=" + sellingRate +
                ", buyingRate=" + buyingRate +
                ", averageRate=" + averageRate +
                ", effectiveDate=" + effectiveDate +
                '}';
    }
}