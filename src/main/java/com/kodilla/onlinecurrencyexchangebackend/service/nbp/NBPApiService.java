package com.kodilla.onlinecurrencyexchangebackend.service.nbp;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateDto;
import com.kodilla.onlinecurrencyexchangebackend.nbp.client.NBPApiClient;
import com.kodilla.onlinecurrencyexchangebackend.nbp.tables.NBPTableType;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.ExchangeRateRepository;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyService;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NBPApiService {

    private final NBPApiClient nbpApiClient;
    private final ExchangeRateService exchangeRateService;
    private final CurrencyService currencyService;

    private void saveRatesToDatabase(List<RateDto> ratesC, List<RateDto> ratesA, LocalDate effectiveDate) {
        for (RateDto rateC : ratesC) {
            Currency currency = currencyService.getOrCreateCurrency(rateC);
            if (exchangeRateService.getOrCreateExchangeRate(ratesA, effectiveDate, rateC, currency)) return;
        }
    }

    public void updateCurrencyRates() {
        LocalDate effectiveDate = LocalDate.now();
        List<RateDto> ratesC = nbpApiClient.fetchRatesFromTable(NBPTableType.C.getCode(), effectiveDate);
        List<RateDto> ratesA = nbpApiClient.fetchRatesFromTable(NBPTableType.A.getCode(), effectiveDate);
        saveRatesToDatabase(ratesC, ratesA, effectiveDate);
    }

    public void updateCurrencyRatesWithDate(LocalDate effectiveDate) {
        List<RateDto> ratesC = nbpApiClient.fetchRatesFromTable(NBPTableType.C.getCode(), effectiveDate);
        List<RateDto> ratesA = nbpApiClient.fetchRatesFromTable(NBPTableType.A.getCode(), effectiveDate);
        saveRatesToDatabase(ratesC, ratesA, effectiveDate);
    }
}
