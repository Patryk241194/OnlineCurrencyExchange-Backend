package com.kodilla.onlinecurrencyexchangebackend.service.nbp.integration;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.nbp.validator.NBPApiDateValidator;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.ExchangeRateRepository;
import com.kodilla.onlinecurrencyexchangebackend.service.nbp.NBPApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
@Transactional
class NBPApiServiceIntegrationTestWithTestContainers {

    @Autowired
    private NBPApiService nbpApiService;
    @Autowired
    private NBPApiDateValidator validator;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"));

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

//    @Test
//    void shouldSaveRatesToDatabase() {
//        // Given
//        nbpApiService.updateCurrencyRates();
//
//        // When
//        List<Currency> listOfCurrencies = currencyRepository.findAll();
//        List<ExchangeRate> listOfExchangeRates = exchangeRateRepository.findAll();
//        System.out.println(listOfCurrencies);
//        System.out.println(listOfExchangeRates);
//
//        // Then
//        if (validator.isWeekend(LocalDate.now())) {
//            assertEquals(0, listOfCurrencies.size());
//            assertEquals(0, listOfExchangeRates.size());
//        } else {
//            assertEquals(13, listOfCurrencies.size());
//            assertEquals(13, listOfExchangeRates.size());
//        }
//    }

    @Test
    void shouldSaveRatesToDatabaseWithSpecificDate() {
        // Given
        nbpApiService.updateCurrencyRatesWithDate(LocalDate.of(2024, 04, 04));

        // When
        List<Currency> listOfCurrencies = currencyRepository.findAll();
        List<ExchangeRate> listOfExchangeRates = exchangeRateRepository.findAll();
        System.out.println(listOfCurrencies);
        System.out.println(listOfExchangeRates);

        // Then
        // Poprawić metody hashCode
        listOfCurrencies.forEach(System.out::println);
        listOfExchangeRates.forEach(System.out::println);
        assertEquals(13, listOfCurrencies.size());
        assertEquals(13, listOfExchangeRates.size());
    }
}
