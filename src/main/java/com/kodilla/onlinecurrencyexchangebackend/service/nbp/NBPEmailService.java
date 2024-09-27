package com.kodilla.onlinecurrencyexchangebackend.service.nbp;

import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.UserDetailResponse;
import com.kodilla.onlinecurrencyexchangebackend.observer.UserObserver;
import com.kodilla.onlinecurrencyexchangebackend.security.jwt.JwtService;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.CurrencyExchangeService;
import com.kodilla.onlinecurrencyexchangebackend.service.domain.UserService;
import com.kodilla.onlinecurrencyexchangebackend.service.email.EmailService;
import com.kodilla.onlinecurrencyexchangebackend.service.email.Mail;
import com.kodilla.onlinecurrencyexchangebackend.service.email.MailCreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.kodilla.onlinecurrencyexchangebackend.security.log.LogMessages.PREPARING_EMAIL_LOG;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class NBPEmailService {
    private final EmailService emailService;
    private final UserService userService;
    private final CurrencyExchangeService currencyExchangeService;
    private final MailCreatorService mailCreatorService;
    private final JwtService jwtService;

    @Scheduled(cron = "0 5 13 ? * MON-FRI")
    public void sendDailyCurrencyRates() {
        List<UserDetailResponse> users = userService.getAllUsers();
        List<CurrencyExchangeDto> allRates = currencyExchangeService.getExchangeRatesByDate(LocalDate.now());

        for (UserDetailResponse user : users) {
            List<CurrencyExchangeDto> userRates = filterRatesForUser(allRates, user.getSubscribedCurrencies());
            if (!userRates.isEmpty()) {
                var userEntity = userService.findUserEntityByUsername(user.getUsername());
                String unsubscribeToken = jwtService.generateUnsubscribeToken(userEntity);
                String subject = createEmailSubject(user.getSubscribedCurrencies());
                String emailContent = mailCreatorService.buildDailyCurrencyEmail(user.getUsername(), userRates, unsubscribeToken);
                Mail mail = Mail.builder()
                        .mailTo(user.getEmail())
                        .subject(subject)
                        .message(emailContent)
                        .build();
                log.info(PREPARING_EMAIL_LOG, user.getUsername());
                emailService.send(mail);
            }
        }
    }

    public void notifyObserver(UserObserver observer, double currentRate) {
        String subject = createEmailAlertSubject(observer);
        String unsubscribeToken = jwtService.generateUnsubscribeToken(observer.getUserEntity());
        String emailContent = mailCreatorService.buildCurrencyAlertEmail(observer, currentRate, unsubscribeToken);
        Mail mail = Mail.builder()
                .mailTo(observer.getEmail())
                .subject(subject)
                .message(emailContent)
                .build();
        log.info(PREPARING_EMAIL_LOG, observer.getUsername());
        emailService.send(mail);
    }

    private List<CurrencyExchangeDto> filterRatesForUser(List<CurrencyExchangeDto> allRates, List<String> subscribedCurrencies) {
        return allRates.stream()
                .filter(rate -> subscribedCurrencies.contains(rate.getCurrencyCode()))
                .collect(Collectors.toList());
    }

    private String createEmailSubject(List<String> subscribedCurrencies) {
        return "Daily Currency Exchange Rates for " + String.join(", ", subscribedCurrencies);
    }

    private static String createEmailAlertSubject(UserObserver observer) {
        return "Currency Alert: " + observer.getCurrencyCode();
    }

}








