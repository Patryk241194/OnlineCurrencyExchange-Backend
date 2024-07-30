package com.kodilla.onlinecurrencyexchangebackend.service.domain;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyDisplayDto;
import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateDto;
import com.kodilla.onlinecurrencyexchangebackend.error.currency.CurrencyNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserNotLoggedInException;
import com.kodilla.onlinecurrencyexchangebackend.mapper.CurrencyMapper;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.UserRepository;
import com.kodilla.onlinecurrencyexchangebackend.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrencyService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final JwtService jwtService;
    private final CurrencyMapper currencyMapper;

    public List<CurrencyDisplayDto> getAllCurrencyDisplayDtos() {
        List<Currency> currencies = currencyRepository.findAll();
        return currencies.stream()
                .map(currencyMapper::mapToCurrencyDisplayDto)
                .collect(Collectors.toList());
    }

    public void subscribeUserToCurrency(String currencyCode, String token) {
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotLoggedInException::new);
        Currency currency = currencyRepository.findByCode(currencyCode).orElseThrow(CurrencyNotFoundException::new);
        if (!user.getSubscribedCurrencies().contains(currency)) {
            user.getSubscribedCurrencies().add(currency);
            currency.getSubscribedUsers().add(user);
            userRepository.save(user);
            currencyRepository.save(currency);
        }
    }

    public void unsubscribeUserFromCurrency(String currencyCode, String token) {
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotLoggedInException::new);
        Currency currency = currencyRepository.findByCode(currencyCode).orElseThrow(CurrencyNotFoundException::new);
        if (user.getSubscribedCurrencies().contains(currency)) {
            user.getSubscribedCurrencies().remove(currency);
            currency.getSubscribedUsers().remove(user);
            userRepository.save(user);
            currencyRepository.save(currency);
        }
    }

    public Currency getOrCreateCurrency(RateDto ratesC) {
        Optional<Currency> existingCurrency = currencyRepository.findByCode(ratesC.getCode());

        if (existingCurrency.isPresent()) {
            return existingCurrency.get();
        } else {
            Currency currency = new Currency();
            currency.setCode(ratesC.getCode());
            currency.setName(ratesC.getCurrency());
            currencyRepository.save(currency);
            return currency;
        }
    }

    public List<Currency> getCurrenciesBySubscribedUsersId(Long userId) {
        return currencyRepository.findCurrenciesBySubscribedUsersId(userId);
    }

    public Currency getCurrencyById(final Long currencyId) {
        return currencyRepository.findById(currencyId).orElseThrow(CurrencyNotFoundException::new);
    }

}
