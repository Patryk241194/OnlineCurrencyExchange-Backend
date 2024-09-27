package com.kodilla.onlinecurrencyexchangebackend.service.email;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import com.kodilla.onlinecurrencyexchangebackend.observer.UserObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
public class MailCreatorService {

    @Value("${app.domain.url}")
    private String domainUrl;
    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildDailyCurrencyEmail(String username, List<CurrencyExchangeDto> userRates, String token) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("userRates", userRates);
        context.setVariable("domainUrl",domainUrl);
        context.setVariable("unsubscribeToken", token);

        return templateEngine.process("mail/daily-currency-rate-mail", context);
    }

    public String buildCurrencyAlertEmail(UserObserver observer, double currentRate, String token) {
        Context context = new Context();
        context.setVariable("username", observer.getUsername());
        context.setVariable("currencyCode", observer.getCurrencyCode());
        context.setVariable("currentRate", currentRate);
        context.setVariable("unsubscribeToken", token);
        context.setVariable("domainUrl", domainUrl);

        return templateEngine.process("mail/currency-alert-mail", context);
    }
}
