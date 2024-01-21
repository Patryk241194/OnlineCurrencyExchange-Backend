package com.kodilla.onlinecurrencyexchangebackend.repository;

import com.kodilla.onlinecurrencyexchangebackend.domain.GoldPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
class GoldPriceRepositoryTest {

    @Autowired
    private GoldPriceRepository goldPriceRepository;
    private GoldPrice goldPrice;

    @BeforeEach
    void setUp() {
        goldPrice = GoldPrice.builder()
                .effectiveDate(LocalDate.now())
                .price(BigDecimal.valueOf(263.45))
                .build();
        goldPriceRepository.save(goldPrice);
    }

    @Test
    void goldPriceRepositoryCreateTest() {
        // When
        GoldPrice foundGoldPrice = goldPriceRepository.findById(goldPrice.getId()).orElse(null);

        // Then
        assertEquals(goldPrice, foundGoldPrice);
    }

    @Test
    void goldPriceRepositoryUpdateTest() {
        // Given
        BigDecimal newPrice = BigDecimal.valueOf(260.21);

        // When
        goldPrice.setPrice(newPrice);
        goldPriceRepository.save(goldPrice);

        // Then
        GoldPrice updatedGoldPrice = goldPriceRepository.findById(goldPrice.getId()).orElse(null);
        assert updatedGoldPrice != null;
        assertEquals(newPrice, updatedGoldPrice.getPrice());
    }

    @Test
    void goldPriceRepositoryDeleteTest() {
        // When
        Long id = goldPrice.getId();
        goldPriceRepository.deleteById(goldPrice.getId());

        // Then
        assertFalse(goldPriceRepository.existsById(id));
    }

}