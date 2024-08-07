package com.kodilla.onlinecurrencyexchangebackend.observer;

public interface Observer {
    boolean shouldNotify(String currencyCode, double currentRate);
}