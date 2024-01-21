package com.kodilla.onlinecurrencyexchangebackend.service.nbp;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateDto;
import com.kodilla.onlinecurrencyexchangebackend.nbp.client.NBPApiClient;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NBPApiService {

    private final NBPApiClient nbpApiClient;
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    private void saveRatesToDatabase(List<RateDto> ratesC, List<RateDto> ratesA) {
        LocalDate effectiveDate = LocalDate.now();

        for (RateDto rateC : ratesC) {
            Currency currency = new Currency();
            currency.setCode(rateC.getCode());
            currency.setName(rateC.getCurrency());
            currencyRepository.save(currency);

            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setCurrency(currency);
            exchangeRate.setEffectiveDate(effectiveDate);
            exchangeRate.setSellingRate(rateC.getSellingRate());
            exchangeRate.setBuyingRate(rateC.getBuyingRate());

            Optional<RateDto> correspondingRateA = ratesA.stream()
                    .filter(rateA -> rateA.getCode().equals(rateC.getCode()))
                    .findFirst();

            correspondingRateA.ifPresent(rateA -> exchangeRate.setAverageRate(rateA.getAverageRate()));

            exchangeRateRepository.save(exchangeRate);

        }
    }

    public void updateCurrencyRates() {
        List<RateDto> ratesC = nbpApiClient.fetchRatesFromTable("C");
        List<RateDto> ratesA = nbpApiClient.fetchRatesFromTable("A");
        saveRatesToDatabase(ratesC, ratesA);
    }
}
