package com.kodilla.onlinecurrencyexchangebackend.service.nbp;

import com.kodilla.onlinecurrencyexchangebackend.dto.nbp.RateDto;
import com.kodilla.onlinecurrencyexchangebackend.nbp.client.NBPApiClient;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NBPApiServiceTest {

    @Mock
    private NBPApiClient nbpApiClient;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private NBPApiService nbpApiService;

    @Test
    void updateCurrencyRates_shouldSaveDataInDatabase() {
        // Given
        List<RateDto> ratesC = Collections.singletonList(createRateDto("USD", new BigDecimal(4.0), new BigDecimal(3.8), null));
        List<RateDto> ratesA = Collections.singletonList(createRateDto("USD", null, null, new BigDecimal(3.9)));

        // When
        when(nbpApiClient.fetchRatesFromTable("C")).thenReturn(ratesC);
        when(nbpApiClient.fetchRatesFromTable("A")).thenReturn(ratesA);
        nbpApiService.updateCurrencyRates();

        // Then
        verify(currencyRepository, times(1)).save(any());
        verify(exchangeRateRepository, times(1)).save(any());

    }

    private RateDto createRateDto(String code, BigDecimal sellingRate, BigDecimal buyingRate, BigDecimal averageRate) {
        RateDto rateDto = new RateDto();
        rateDto.setCode(code);
        rateDto.setSellingRate(sellingRate);
        rateDto.setBuyingRate(buyingRate);
        rateDto.setAverageRate(averageRate);
        return rateDto;
    }
}