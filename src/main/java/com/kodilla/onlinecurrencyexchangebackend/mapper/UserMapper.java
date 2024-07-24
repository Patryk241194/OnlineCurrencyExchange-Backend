package com.kodilla.onlinecurrencyexchangebackend.mapper;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.RoleStatus;
import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.UserDto;
import com.kodilla.onlinecurrencyexchangebackend.mapper.validator.RoleStatusValidator;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserMapper implements Mapper<UserDto, User> {

    private final CurrencyService currencyService;

    @Override
    public UserDto mapToDto(User user) {
        List<Long> subscribedCurrenciesIds = (user.getSubscribedCurrencies() != null)
                ? user.getSubscribedCurrencies().stream().map(Currency::getId).collect(Collectors.toList()) : Collections.emptyList();
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                (user.getRole() != null) ? user.getRole().toString() : null,
                subscribedCurrenciesIds
        );
    }

    @Override
    public User mapToEntity(UserDto userDto) {
        RoleStatus roleStatus = RoleStatusValidator.isValidRole(userDto.getRole()) ?
                RoleStatus.valueOf(userDto.getRole().toUpperCase()) : null;
        return new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                null,
                roleStatus,
                (userDto.getSubscribedCurrenciesIds() != null) ? currencyService.getCurrenciesBySubscribedUsersId(userDto.getId()) : null
        );
    }
}
