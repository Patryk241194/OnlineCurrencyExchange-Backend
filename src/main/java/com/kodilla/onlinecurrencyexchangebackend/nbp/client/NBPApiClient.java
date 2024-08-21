package com.kodilla.onlinecurrencyexchangebackend.nbp.client;

import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateDto;
import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateListDto;
import com.kodilla.onlinecurrencyexchangebackend.nbp.config.NBPConfig;
import com.kodilla.onlinecurrencyexchangebackend.nbp.validator.NBPApiDateValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kodilla.onlinecurrencyexchangebackend.security.log.LogMessages.*;

@Component
@RequiredArgsConstructor
public class NBPApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NBPApiClient.class);
    private final RestTemplate restTemplate;
    private final NBPConfig nbpConfig;
    private final NBPApiDateValidator nbpApiDateValidator;

    private List<RateDto> fetchData(URI apiUrl) {
        try {
            RateListDto[] rateLists = restTemplate.getForObject(apiUrl, RateListDto[].class);
            LOGGER.info(RECEIVED_DATA_FROM_API, Arrays.toString(rateLists));

            List<RateDto> rates = Optional.ofNullable(rateLists)
                    .map(Arrays::stream)
                    .orElseGet(Stream::empty)
                    .findFirst()
                    .map(RateListDto::getRates)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(rate -> Objects.nonNull(rate.getCurrency()) && Objects.nonNull(rate.getCode()))
                    .collect(Collectors.toList());

            rates.forEach(rate -> {
                LOGGER.info(CURRENCY_DETAILS, rate.getCurrency(), rate.getCode(), rate.getAverageRate(), rate.getBuyingRate(), rate.getSellingRate());
            });

            LOGGER.info(FETCHED_RATES_FROM_API, rates.size(), rates.isEmpty() ? "N/A" : rates.get(0));
            return rates;
        } catch (RestClientException e) {
            LOGGER.error(ERROR_FETCHING_DATA, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<RateDto> fetchRatesFromTable(String tableType, LocalDate effectiveDate) {
        if (!nbpApiDateValidator.isValidDate(effectiveDate, tableType)) {
            LOGGER.warn(INVALID_DATE_PROVIDED, tableType, effectiveDate);
            return Collections.emptyList();
        }

        URI apiUrl = URI.create(nbpConfig.getNbpApiEndpoint() + nbpConfig.getExchangeRates() + nbpConfig.getTables() + tableType + "/" + effectiveDate + nbpConfig.getFormatJson());
        LOGGER.info(FETCHING_DATA_FROM_API, apiUrl);
        List<RateDto> rates = fetchData(apiUrl);
        return rates;
    }
}
