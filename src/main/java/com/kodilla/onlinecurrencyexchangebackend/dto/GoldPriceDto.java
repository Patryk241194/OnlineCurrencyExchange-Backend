package com.kodilla.onlinecurrencyexchangebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GoldPriceDto {
    private Long id;
    private LocalDate effectiveDate;
    private BigDecimal price;
}
