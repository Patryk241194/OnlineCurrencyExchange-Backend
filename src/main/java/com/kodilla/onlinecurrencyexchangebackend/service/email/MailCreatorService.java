package com.kodilla.onlinecurrencyexchangebackend.service.email;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
public class MailCreatorService {

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildDailyCurrencyEmail(String username, List<CurrencyExchangeDto> userRates) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("userRates", userRates);

        return templateEngine.process("mail/daily-currency-rate-mail", context);
    }
}
