package com.kodilla.onlinecurrencyexchangebackend.observer;

import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserObserver implements Observer {

    private final String email;
    private final String username;
    private final String currencyCode;
    private final double threshold;
    private final boolean aboveThreshold;
    private final User userEntity;

    public boolean shouldNotify(String currencyCode, double currentRate) {
        return this.currencyCode.equals(currencyCode) &&
                (aboveThreshold ? currentRate > threshold : currentRate < threshold);
    }
}
