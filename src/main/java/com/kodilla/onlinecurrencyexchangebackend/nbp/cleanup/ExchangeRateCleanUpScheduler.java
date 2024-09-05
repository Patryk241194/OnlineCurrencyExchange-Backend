package com.kodilla.onlinecurrencyexchangebackend.nbp.cleanup;

import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExchangeRateCleanUpScheduler {

    private final ExchangeRateRepository exchangeRateRepository;

    @Scheduled(cron = "0 15 13 ? * MON-FRI")
    public void cleanUpOldExchangeRates() {
        List<String> currencyCodes = exchangeRateRepository.findAllCurrencyCodes();

        for (String currencyCode : currencyCodes) {
            List<ExchangeRate> rates = exchangeRateRepository.findAllByCurrencyCodeOrderByDateDesc(currencyCode);

            if (rates.size() > 100) {
                List<ExchangeRate> ratesToDelete = rates.subList(100, rates.size());
                exchangeRateRepository.deleteAll(ratesToDelete);
            }
        }
    }
}
