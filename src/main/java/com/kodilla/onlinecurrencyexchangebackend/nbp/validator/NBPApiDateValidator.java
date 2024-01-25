package com.kodilla.onlinecurrencyexchangebackend.nbp.validator;

import com.kodilla.onlinecurrencyexchangebackend.nbp.tables.NBPTableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class NBPApiDateValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NBPApiDateValidator.class);

    public boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public boolean isValidDate(LocalDate date, String tableType) {
        if (isWeekend(date)) {
            if (NBPTableType.A.getCode().equals(tableType)) {
                LOGGER.warn(NBPTableType.A.getUpdateTime());
            } else if (NBPTableType.C.getCode().equals(tableType)) {
                LOGGER.warn(NBPTableType.C.getUpdateTime());
            } else {
                LOGGER.warn("Unsupported table type.");
            }
            return false;
        }
        return true;
    }
}
