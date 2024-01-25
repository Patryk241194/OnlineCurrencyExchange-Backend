package com.kodilla.onlinecurrencyexchangebackend.nbp.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NBPConfig {

    @Value("${nbp.api.endpoint}")
    private String nbpApiEndpoint;

    @Value("${nbp.exchangeRates}")
    private String exchangeRates;

    @Value("${nbp.rates}")
    private String rates;

    @Value("${nbp.tables}")
    private String tables;

    @Value("${nbp.goldPrice}")
    private String goldPrice;

    @Value("/?format=json")
    private String formatJson;


}
