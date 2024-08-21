package com.kodilla.onlinecurrencyexchangebackend.nbp.validator;

import com.kodilla.onlinecurrencyexchangebackend.nbp.config.NBPConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;

import static com.kodilla.onlinecurrencyexchangebackend.security.log.LogMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class NBPApiDateValidator {

    private final RestTemplate restTemplate;
    private final NBPConfig nbpConfig;

    public boolean isValidDate(LocalDate date, String tableType) {
        String url = nbpConfig.getNbpApiEndpoint() + nbpConfig.getExchangeRates() + nbpConfig.getTables() + tableType + "/" + date + nbpConfig.getFormatJson();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        HttpStatus statusCode = null;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            statusCode = response.getStatusCode();
        } catch (HttpClientErrorException e) {
            statusCode = e.getStatusCode();
            log.error(HTTP_CLIENT_ERROR, e.getStatusCode(), e.getMessage());
        } catch (HttpServerErrorException e) {
            statusCode = e.getStatusCode();
            log.error(HTTP_SERVER_ERROR, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            log.error(UNEXPECTED_ERROR_VALIDATING_DATE, date, e.getMessage(), e);
        }
        if (statusCode == HttpStatus.OK) {
            return true;
        } else if (statusCode == HttpStatus.NOT_FOUND) {
            log.info(DATE_IS_HOLIDAY_OR_WEEKEND, date);
            return false;
        } else {
            log.warn(UNEXPECTED_RESPONSE_STATUS, statusCode, date);
            return false;
        }
    }
}
