package com.kodilla.onlinecurrencyexchangebackend.service.nbp.email;

import com.kodilla.onlinecurrencyexchangebackend.config.AdminConfig;
import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import com.kodilla.onlinecurrencyexchangebackend.domain.RoleStatus;
import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.ExchangeRateRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.UserRepository;
import com.kodilla.onlinecurrencyexchangebackend.service.nbp.NBPEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
//@Transactional
class NBPEmailServiceTest {

    @Autowired
    private NBPEmailService nbpEmailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private AdminConfig adminConfig;
    private User user;
    private Currency currency;
    private ExchangeRate exchangeRate;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("test_username2")
                .email(adminConfig.getAdminMail())
                .password("test_password")
                .role(RoleStatus.USER)
                .build();
        userRepository.save(user);

        currency = Currency.builder()
                .code("USD")
                .name("US Dollar")
                .build();
        currencyRepository.save(currency);

        exchangeRate = ExchangeRate.builder()
                .sellingRate(BigDecimal.valueOf(4.0149))
                .buyingRate(BigDecimal.valueOf(3.9353))
                .averageRate(BigDecimal.valueOf(3.9850))
                .effectiveDate(LocalDate.now())
                .currency(currency)
                .build();
        exchangeRateRepository.save(exchangeRate);

        currency.getExchangeRates().add(exchangeRate);
        currency.getSubscribedUsers().add(user);
        userRepository.save(user);
        currencyRepository.save(currency);
    }

    @Test
    void shouldSendEmailToAdmin() {
        // Given
        nbpEmailService.sendDailyCurrencyRates();
        // When
        // Then

    }
}