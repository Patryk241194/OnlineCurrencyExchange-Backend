package com.kodilla.onlinecurrencyexchangebackend.service;

import com.kodilla.onlinecurrencyexchangebackend.domain.GoldPrice;
import com.kodilla.onlinecurrencyexchangebackend.dto.GoldPriceDto;
import com.kodilla.onlinecurrencyexchangebackend.error.goldprice.GoldPriceNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.repository.GoldPriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GoldPriceService {

    private final GoldPriceRepository goldPriceRepository;

    public List<GoldPrice> getAllGoldPrices() {
        return goldPriceRepository.findAll();
    }

    public GoldPrice getGoldPriceById(final Long goldPriceId) {
        return goldPriceRepository.findById(goldPriceId).orElseThrow(GoldPriceNotFoundException::new);
    }

    public GoldPrice saveGoldPrice(final GoldPrice goldPrice) {
        return goldPriceRepository.save(goldPrice);
    }

    public void deleteGoldPriceById(final Long goldPriceId) {
        if (!goldPriceRepository.existsById(goldPriceId)) {
            throw new GoldPriceNotFoundException();
        }
        goldPriceRepository.deleteById(goldPriceId);
    }

    public GoldPrice updateGoldPrice(Long goldPriceId, GoldPriceDto goldPriceDto) {
        GoldPrice goldPrice = goldPriceRepository.findById(goldPriceId)
                .orElseThrow(GoldPriceNotFoundException::new);
        if (goldPriceDto.getEffectiveDate() != null) {
            goldPrice.setEffectiveDate(goldPriceDto.getEffectiveDate());
        }
        if (goldPriceDto.getPrice() != null) {
            goldPrice.setPrice(goldPriceDto.getPrice());
        }
        return goldPriceRepository.save(goldPrice);
    }

}