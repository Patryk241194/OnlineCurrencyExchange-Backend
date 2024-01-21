package com.kodilla.onlinecurrencyexchangebackend.error;

import com.kodilla.onlinecurrencyexchangebackend.error.currency.CurrencyNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.exchangerate.ExchangeRateNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.goldprice.GoldPriceNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>("User with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<Object> handleCurrencyNotFoundException(CurrencyNotFoundException currencyNotFoundException) {
        return new ResponseEntity<>("Currency with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExchangeRateNotFoundException.class)
    public ResponseEntity<Object> handleExchangeRateNotFoundException(ExchangeRateNotFoundException exchangeRateNotFoundException) {
        return new ResponseEntity<>("ExchangeRate with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GoldPriceNotFoundException.class)
    public ResponseEntity<Object> handleGoldPriceNotFoundException(GoldPriceNotFoundException goldPriceNotFoundException) {
        return new ResponseEntity<>("GoldPrice with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }
}
