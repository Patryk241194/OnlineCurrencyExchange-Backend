package com.kodilla.onlinecurrencyexchangebackend.observer;

import lombok.Getter;


public class UserObserver implements Observer {

    private final String email;
    private final String currencyCode;
    private final double threshold;
    private final boolean aboveThreshold;

    public UserObserver(String email, String currencyCode, double threshold, boolean aboveThreshold) {
        this.email = email;
        this.currencyCode = currencyCode;
        this.threshold = threshold;
        this.aboveThreshold = aboveThreshold;
    }

    public boolean shouldNotify(String currencyCode, double currentRate) {
        return this.currencyCode.equals(currencyCode) &&
                (aboveThreshold ? currentRate > threshold : currentRate < threshold);
    }

    public String getEmail() {
        return email;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
