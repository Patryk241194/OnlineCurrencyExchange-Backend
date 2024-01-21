package com.kodilla.onlinecurrencyexchangebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class ExchangeRateDto {
    private Long id;
    private BigDecimal sellingRate;
    private BigDecimal buyingRate;
    private BigDecimal averageRate;
    private LocalDate effectiveDate;
    private Long currencyId;
}
