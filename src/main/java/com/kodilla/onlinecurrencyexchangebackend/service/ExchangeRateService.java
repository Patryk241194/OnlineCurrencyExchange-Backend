package com.kodilla.onlinecurrencyexchangebackend.service;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.dto.ExchangeRateDto;
import com.kodilla.onlinecurrencyexchangebackend.error.exchangerate.ExchangeRateNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.repository.ExchangeRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public List<ExchangeRate> getExchangeRatesByCurrencyId(final Long currencyId) {
        return exchangeRateRepository.findExchangeRatesByCurrency_Id(currencyId);
    }

    public ExchangeRate getExchangeRateById(final Long exchangeRateId) {
        return exchangeRateRepository.findById(exchangeRateId).orElseThrow(ExchangeRateNotFoundException::new);
    }

    public ExchangeRate getExchangeRateByCurrencyAndDate(final Currency currency, final LocalDate effectiveDate) {
        return exchangeRateRepository.findByCurrencyAndEffectiveDate(currency, effectiveDate)
                .orElseThrow(ExchangeRateNotFoundException::new);
    }

    public ExchangeRate saveExchangeRate(final ExchangeRate exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }

    public void deleteExchangeRateById(final Long exchangeRateId) {
        if (!exchangeRateRepository.existsById(exchangeRateId)) {
            throw new ExchangeRateNotFoundException();
        }
        exchangeRateRepository.deleteById(exchangeRateId);
    }

    public ExchangeRate updateExchangeRate(final Long exchangeRateId, final ExchangeRateDto exchangeRateDto) {
        ExchangeRate exchangeRate = exchangeRateRepository.findById(exchangeRateId)
                .orElseThrow(ExchangeRateNotFoundException::new);

        exchangeRate.setSellingRate(exchangeRateDto.getSellingRate());
        exchangeRate.setBuyingRate(exchangeRateDto.getBuyingRate());
        exchangeRate.setAverageRate(exchangeRateDto.getAverageRate());
        exchangeRate.setEffectiveDate(exchangeRateDto.getEffectiveDate());

        return exchangeRateRepository.save(exchangeRate);
    }
}
