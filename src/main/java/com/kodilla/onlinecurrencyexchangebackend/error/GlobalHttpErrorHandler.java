package com.kodilla.onlinecurrencyexchangebackend.error;

import com.kodilla.onlinecurrencyexchangebackend.error.auth.*;
import com.kodilla.onlinecurrencyexchangebackend.error.currency.CurrencyNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.currency.CurrencyObservableNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.exchangerate.ExchangeRateNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.goldprice.GoldPriceNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserAlreadyExistsException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserNotLoggedInException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>("User with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>("User with provided username or email already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<Object> handleCurrencyNotFoundException(CurrencyNotFoundException ex) {
        return new ResponseEntity<>("Currency with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyObservableNotFoundException.class)
    public ResponseEntity<Object> handleCurrencyObservableNotFoundException(CurrencyObservableNotFoundException ex) {
        String errorMessage = "Currency observable not found for currency code: " + ex.getCurrencyCode();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExchangeRateNotFoundException.class)
    public ResponseEntity<Object> handleExchangeRateNotFoundException(ExchangeRateNotFoundException ex) {
        return new ResponseEntity<>("ExchangeRate with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GoldPriceNotFoundException.class)
    public ResponseEntity<Object> handleGoldPriceNotFoundException(GoldPriceNotFoundException ex) {
        return new ResponseEntity<>("GoldPrice with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ResponseEntity<>("Given username or password are incorrect", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException ex) {
        return new ResponseEntity<>("Invalid new password", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Object> handleUnauthorizedActionException(UnauthorizedActionException ex) {
        return new ResponseEntity<>("User does not have sufficient privileges to perform this action", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<Object> handleUserNotLoggedInException(UserNotLoggedInException ex) {
        return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidVerificationTokenException.class)
    public ResponseEntity<Object> handleInvalidVerificationTokenException(InvalidVerificationTokenException ex) {
        return new ResponseEntity<>("Invalid verification token.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAuthorizationHeaderException.class)
    public ResponseEntity<Object> handleInvalidAuthorizationHeaderException(InvalidAuthorizationHeaderException ex) {
        return new ResponseEntity<>("Invalid or missing Authorization header.", HttpStatus.BAD_REQUEST);
    }
}
