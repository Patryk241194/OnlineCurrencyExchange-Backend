package com.kodilla.onlinecurrencyexchangebackend.service.domain;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyDto;
import com.kodilla.onlinecurrencyexchangebackend.error.currency.CurrencyNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public List<Currency> getCurrenciesBySubscribedUsersId(Long userId) {
        return currencyRepository.findCurrenciesBySubscribedUsersId(userId);
    }

    public Currency getCurrencyById(final Long currencyId) {
        return currencyRepository.findById(currencyId).orElseThrow(CurrencyNotFoundException::new);
    }

    public Currency getCurrencyByCode(final String code) {
        return currencyRepository.findByCode(code).orElseThrow(CurrencyNotFoundException::new);
    }

    public Currency saveCurrency(final Currency currency) {
        return currencyRepository.save(currency);
    }

    public void deleteCurrencyById(final Long currencyId) {
        if (!currencyRepository.existsById(currencyId)) {
            throw new CurrencyNotFoundException();
        }
        currencyRepository.deleteById(currencyId);
    }

    public Currency updateCurrency(final Long currencyId, final CurrencyDto currencyDto) {
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(CurrencyNotFoundException::new);

        currency.setCode(currencyDto.getCode());
        currency.setName(currencyDto.getName());

        return currencyRepository.save(currency);
    }
}
