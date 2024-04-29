package com.kodilla.onlinecurrencyexchangebackend.mapper;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyDto;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.ExchangeRateService;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrencyMapper implements Mapper<CurrencyDto, Currency> {

    private final ExchangeRateService exchangeRateService;
    private final UserService userService;

    @Override
    public CurrencyDto mapToDto(Currency currency) {
        List<Long> exchangeRateIds = (currency.getExchangeRates() != null)
                ? currency.getExchangeRates().stream().map(ExchangeRate::getId).collect(Collectors.toList()) : Collections.emptyList();
        List<Long> subscribedUsersIds = (currency.getSubscribedUsers() != null)
                ? currency.getSubscribedUsers().stream().map(User::getId).collect(Collectors.toList()) : Collections.emptyList();

        return new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getName(),
                exchangeRateIds,
                subscribedUsersIds
        );
    }

    @Override
    public Currency mapToEntity(CurrencyDto currencyDto) {
        return new Currency(
                currencyDto.getId(),
                currencyDto.getCode(),
                currencyDto.getName(),
                (currencyDto.getExchangeRateIds() != null) ? exchangeRateService.getExchangeRatesByCurrencyId(currencyDto.getId()) : null,
                (currencyDto.getSubscribedUsersIds() != null) ? userService.getUsersBySubscribedCurrenciesId(currencyDto.getId()) : null
        );
    }
}
