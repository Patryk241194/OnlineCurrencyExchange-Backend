package com.kodilla.onlinecurrencyexchangebackend.mapper;

import com.kodilla.onlinecurrencyexchangebackend.domain.GoldPrice;
import com.kodilla.onlinecurrencyexchangebackend.dto.GoldPriceDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GoldPriceMapper implements Mapper<GoldPriceDto, GoldPrice> {

    @Override
    public GoldPriceDto mapToDto(GoldPrice goldPrice) {
        return new GoldPriceDto(
                goldPrice.getId(),
                goldPrice.getEffectiveDate(),
                goldPrice.getPrice()
        );
    }

    @Override
    public GoldPrice mapToEntity(GoldPriceDto goldPriceDto) {
        return new GoldPrice(
                goldPriceDto.getId(),
                goldPriceDto.getEffectiveDate(),
                goldPriceDto.getPrice()
        );
    }
}
