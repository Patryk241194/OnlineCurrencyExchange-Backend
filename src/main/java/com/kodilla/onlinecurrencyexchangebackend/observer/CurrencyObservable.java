package com.kodilla.onlinecurrencyexchangebackend.observer;

import com.kodilla.onlinecurrencyexchangebackend.service.nbp.NBPEmailService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CurrencyObservable implements Observable {

    private final List<Observer> observers = new ArrayList<>();
    private final String currencyCode;
    private double currentRate;
    private NBPEmailService nbpEmailService;


    public CurrencyObservable(String currencyCode, double initialRate) {
        this.currencyCode = currencyCode;
        this.currentRate = initialRate;
    }

    public void setRate(double newRate) {
        this.currentRate = newRate;
        notifyObservers(currencyCode, newRate);
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String currencyCode, double currentRate) {
        for (Observer observer : observers) {
            if (observer.shouldNotify(currencyCode, currentRate)) {
                nbpEmailService.notifyObserver((UserObserver) observer, currentRate);
            }
        }
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
