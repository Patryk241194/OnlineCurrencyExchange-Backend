package com.kodilla.onlinecurrencyexchangebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OnlineCurrencyExchangeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineCurrencyExchangeBackendApplication.class, args);
    }

}
