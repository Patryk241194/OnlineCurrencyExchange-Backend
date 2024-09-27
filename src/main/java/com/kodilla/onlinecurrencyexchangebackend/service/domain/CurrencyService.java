package com.kodilla.onlinecurrencyexchangebackend.service.domain;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyDisplayDto;
import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateDto;
import com.kodilla.onlinecurrencyexchangebackend.error.auth.InvalidTokenException;
import com.kodilla.onlinecurrencyexchangebackend.error.currency.CurrencyNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.currency.CurrencyObservableNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserNotLoggedInException;
import com.kodilla.onlinecurrencyexchangebackend.mapper.CurrencyMapper;
import com.kodilla.onlinecurrencyexchangebackend.observer.CurrencyObservable;
import com.kodilla.onlinecurrencyexchangebackend.observer.Observer;
import com.kodilla.onlinecurrencyexchangebackend.observer.UserObserver;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.UserRepository;
import com.kodilla.onlinecurrencyexchangebackend.security.jwt.JwtService;
import com.kodilla.onlinecurrencyexchangebackend.service.nbp.NBPEmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kodilla.onlinecurrencyexchangebackend.nbp.scheduler.NBPScheduler.getEffectiveDate;
import static com.kodilla.onlinecurrencyexchangebackend.security.authorization.CurrencyCredentialValidator.isThresholdValid;
import static com.kodilla.onlinecurrencyexchangebackend.security.log.LogMessages.*;

@Slf4j
@Service
@AllArgsConstructor
@EnableScheduling
public class CurrencyService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final JwtService jwtService;
    private final CurrencyMapper currencyMapper;
    private final Map<String, CurrencyObservable> currencyObservables;
    private final CurrencyExchangeService currencyExchangeService;
    private final NBPEmailService nbpEmailService;

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
            log.info(USER_SUBSCRIBED_TO_CURRENCY, username, currencyCode);
        } else {
            log.info(USER_ALREADY_SUBSCRIBED_TO_CURRENCY, username, currencyCode);
        }
    }

    public void unsubscribeUserFromCurrencyViaWebsite(String currencyCode, String token) {
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotLoggedInException::new);
        unsubscribeFromCurrency(currencyCode, user);
    }

    public void unsubscribeUserFromCurrencyViaEmail(String currencyCode, String token) {
        if (!jwtService.isTokenValidForOperation(token, "unsubscribe")) {
            throw new InvalidTokenException();
        }
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        unsubscribeFromCurrency(currencyCode, user);
    }

    private void unsubscribeFromCurrency(String currencyCode, User user) {

        Currency currency = currencyRepository.findByCode(currencyCode).orElseThrow(CurrencyNotFoundException::new);
        if (user.getSubscribedCurrencies().contains(currency)) {
            user.getSubscribedCurrencies().remove(currency);
            currency.getSubscribedUsers().remove(user);
            userRepository.save(user);
            currencyRepository.save(currency);
            log.info(USER_UNSUBSCRIBED_FROM_CURRENCY, user.getUsername(), currencyCode);
        }
    }

    public void subscribeObserverToCurrency(String currencyCode, double threshold, boolean aboveThreshold, String token) {
        if (!isThresholdValid(threshold)) {
            throw new IllegalArgumentException("Threshold must be higher than 0 and cannot be null.");
        }
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotLoggedInException::new);
        UserObserver observer = new UserObserver(user.getEmail(), user.getUsername(), currencyCode, threshold, aboveThreshold, user);
        double currentRate = currencyExchangeService.getLatestRate(currencyCode);
        CurrencyObservable currencyObservable = currencyObservables.computeIfAbsent(currencyCode, code -> new CurrencyObservable(code, currentRate));

        currencyObservable.registerObserver(observer);
        if (observer.shouldNotify(currencyCode, currentRate)) {
            nbpEmailService.notifyObserver(observer, currentRate);
        }
        log.info(CURRENCY_OBSERVER_SUBSCRIBED, user.getEmail(), currencyCode);
    }


    public void unsubscribeObserverFromCurrencyViaWebsite(String currencyCode, String token) {
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotLoggedInException::new);
        unsubscribeObserverFromCurrency(currencyCode, user);
    }

    public void unsubscribeObserverFromCurrencyViaEmail(String currencyCode, String token) {
        if (!jwtService.isTokenValidForOperation(token, "unsubscribe")) {
            throw new InvalidTokenException();
        }
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        unsubscribeObserverFromCurrency(currencyCode, user);
    }

    private void unsubscribeObserverFromCurrency(String currencyCode, User user) {
        CurrencyObservable currencyObservable = currencyObservables.get(currencyCode);
        if (currencyObservable == null) {
            throw new CurrencyObservableNotFoundException(currencyCode);
        }
        Observer observerToRemove = currencyObservable.getObservers().stream()
                .filter(observer -> observer instanceof UserObserver)
                .map(observer -> (UserObserver) observer)
                .filter(observer -> observer.getEmail().equals(user.getEmail()))
                .filter(observer -> observer.getCurrencyCode().equals(currencyCode))
                .findFirst()
                .orElse(null);

        if (observerToRemove != null) {
            currencyObservable.removeObserver(observerToRemove);
            log.info(CURRENCY_OBSERVER_UNSUBSCRIBED, user.getEmail(), currencyCode);
        } else {
            log.warn(CURRENCY_OBSERVER_NOTFOUND, user.getUsername(), currencyCode);
        }
    }

    @Scheduled(cron = "0 10 13 ? * MON-FRI")
    public void sendCurrencyUpdateToObservers() {
        log.info(CURRENCY_UPDATE_STARTED);
        List<CurrencyExchangeDto> rates = currencyExchangeService.getExchangeRatesByDate(getEffectiveDate());

        for (CurrencyObservable observable : currencyObservables.values()) {
            String currencyCode = observable.getCurrencyCode();
            double currentRate = getCurrentRate(rates, currencyCode);

            if (currentRate != 0.0) {
                observable.setRate(currentRate);
            }
        }
        log.info(CURRENCY_UPDATE_COMPLETED);
    }

    private double getCurrentRate(List<CurrencyExchangeDto> rates, String currencyCode) {
        return rates.stream()
                .filter(rate -> rate.getCurrencyCode().equals(currencyCode))
                .mapToDouble(rate -> rate.getAverageRate().doubleValue())
                .average()
                .orElse(0.0);
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

    public Set<Currency> getCurrenciesBySubscribedUsersId(Long userId) {
        return currencyRepository.findCurrenciesBySubscribedUsersId(userId);
    }

    public Currency getCurrencyById(final Long currencyId) {
        return currencyRepository.findById(currencyId).orElseThrow(CurrencyNotFoundException::new);
    }

}
