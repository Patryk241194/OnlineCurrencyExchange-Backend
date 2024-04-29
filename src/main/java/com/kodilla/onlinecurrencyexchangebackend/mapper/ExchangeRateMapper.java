package com.kodilla.onlinecurrencyexchangebackend.mapper;

import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.dto.ExchangeRateDto;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExchangeRateMapper implements Mapper<ExchangeRateDto, ExchangeRate> {

    private final CurrencyService currencyService;

    @Override
    public ExchangeRateDto mapToDto(ExchangeRate exchangeRate) {
        Long currencyId = (exchangeRate.getCurrency() != null) ? exchangeRate.getCurrency().getId() : null;

        return new ExchangeRateDto(
                exchangeRate.getId(),
                exchangeRate.getSellingRate(),
                exchangeRate.getBuyingRate(),
                exchangeRate.getAverageRate(),
                exchangeRate.getEffectiveDate(),
                currencyId
        );
    }

    @Override
    public ExchangeRate mapToEntity(ExchangeRateDto exchangeRateDto) {
        return new ExchangeRate(
                exchangeRateDto.getId(),
                exchangeRateDto.getSellingRate(),
                exchangeRateDto.getBuyingRate(),
                exchangeRateDto.getAverageRate(),
                exchangeRateDto.getEffectiveDate(),
                (exchangeRateDto.getCurrencyId() != null) ? currencyService.getCurrencyById(exchangeRateDto.getCurrencyId()) : null
        );
    }
}
