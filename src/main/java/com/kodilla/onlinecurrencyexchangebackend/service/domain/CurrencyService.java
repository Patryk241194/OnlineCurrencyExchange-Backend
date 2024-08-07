package com.kodilla.onlinecurrencyexchangebackend.service.domain;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyDisplayDto;
import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateDto;
import com.kodilla.onlinecurrencyexchangebackend.error.currency.CurrencyNotFoundException;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kodilla.onlinecurrencyexchangebackend.nbp.scheduler.NBPScheduler.getEffectiveDate;

@Service
@AllArgsConstructor
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

    public void subscribeObserverToCurrency(String currencyCode, double threshold, boolean aboveThreshold, String token) {
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotLoggedInException::new);
        UserObserver observer = new UserObserver(user.getEmail(), currencyCode, threshold, aboveThreshold);
        double currentRate = currencyExchangeService.getLatestRate(currencyCode);
        CurrencyObservable currencyObservable = currencyObservables.computeIfAbsent(currencyCode, code -> new CurrencyObservable(code, currentRate));

        currencyObservable.registerObserver(observer);
        if (observer.shouldNotify(currencyCode, currentRate)) {
            nbpEmailService.notifyObserver(observer, currentRate);
        }
    }

//    public void unsubscribeObserverFromCurrency(String currencyCode, String token) {
//        String username = jwtService.extractUsername(token);
//        var user = userRepository.findByUsername(username).orElseThrow(UserNotLoggedInException::new);
//
//        Optional<CurrencyObservable> currencyObservableOpt = currencyObservable.stream()
//                .filter(observable -> observable.getCurrencyCoe().equals(currencyCode))
//                .findFirst();
//
//        if (currencyObservableOpt.isPresent()) {
//            CurrencyObservable currencyObservable = currencyObservableOpt.get();
//            Optional<Observer> observerOptional = currencyObservable.getObservers().stream()
//                    .filter(o -> o instanceof UserObserver && ((UserObserver) o).getEmail().equals(user.getEmail()))
//                    .findFirst();
//            observerOptional.ifPresent(currencyObservable::removeObserver);
//        }
//
//        if (currencyObservable.getObservers().contains(currencyCode)) {
//            Optional<Observer> observerOptional = currencyObservable.getObservers().stream()
//                    .filter(o -> o instanceof UserObserver && ((UserObserver) o).getEmail().equals(user.getEmail()))
//                    .findFirst();
//            observerOptional.ifPresent(currencyObservable::removeObserver);
//        }
//    }

    @Scheduled(cron = "0 10 13 ? * MON-FRI")
    public void sendCurrencyUpdateToObservers() {
        LocalDate effectiveDate = getEffectiveDate();
        List<CurrencyExchangeDto> rates = currencyExchangeService.getExchangeRatesByDate(effectiveDate);

        for (CurrencyObservable observable : currencyObservables.values()) {
            String currencyCode = observable.getCurrencyCode();
            double currentRate = getCurrentRate(rates, currencyCode);

            if (currentRate != 0.0) {
                observable.setRate(currentRate);
            }
        }
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
