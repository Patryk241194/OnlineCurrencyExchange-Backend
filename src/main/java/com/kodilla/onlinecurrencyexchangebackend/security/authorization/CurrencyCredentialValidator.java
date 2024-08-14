package com.kodilla.onlinecurrencyexchangebackend.security.authorization;

public class CurrencyCredentialValidator {

    public static boolean isThresholdValid(Double threshold) {
        return threshold != null && threshold > 0;
    }

}
