package com.kodilla.onlinecurrencyexchangebackend.observer;

public interface Observable {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(String currencyCode, double currentRate);
}
