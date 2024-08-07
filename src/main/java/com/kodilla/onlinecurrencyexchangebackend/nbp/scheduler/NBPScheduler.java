package com.kodilla.onlinecurrencyexchangebackend.nbp.scheduler;

import com.kodilla.onlinecurrencyexchangebackend.service.nbp.NBPApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class NBPScheduler {

    private final NBPApiService service;

    @Scheduled(cron = "0 0 13 ? * MON-FRI")
    public void updateCurrencyRatesDaily() {
        if (LocalDateTime.now().getDayOfWeek().compareTo(DayOfWeek.MONDAY) >= 0 &&
                LocalDateTime.now().getDayOfWeek().compareTo(DayOfWeek.FRIDAY) <= 0) {
            service.updateCurrencyRates();
        }
    }

    public static LocalDate getEffectiveDate() {
        LocalDate now = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isBefore(LocalTime.of(13, 0))) {
            now = now.minusDays(1);
        }
        while (now.getDayOfWeek() == DayOfWeek.SATURDAY || now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            now = now.minusDays(1);
        }
        return now;
    }
}
