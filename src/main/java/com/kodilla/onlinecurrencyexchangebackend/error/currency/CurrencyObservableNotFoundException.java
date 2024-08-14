package com.kodilla.onlinecurrencyexchangebackend.error.currency;

public class CurrencyObservableNotFoundException extends RuntimeException {

    private final String currencyCode;

    public CurrencyObservableNotFoundException(String currencyCode) {
        super("Currency observable not found for currency code: " + currencyCode);
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}