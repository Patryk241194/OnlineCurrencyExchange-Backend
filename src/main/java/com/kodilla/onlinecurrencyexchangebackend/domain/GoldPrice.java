package com.kodilla.onlinecurrencyexchangebackend.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "GOLD_PRICES")
public class GoldPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GOLD_ID", unique = true)
    private Long id;
    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;
    @Column(name = "GOLD_PRICE", scale = 4)
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoldPrice goldPrice)) return false;

        if (getId() != null ? !getId().equals(goldPrice.getId()) : goldPrice.getId() != null) return false;
        if (getEffectiveDate() != null ? !getEffectiveDate().equals(goldPrice.getEffectiveDate()) : goldPrice.getEffectiveDate() != null)
            return false;
        return getPrice() != null ? getPrice().equals(goldPrice.getPrice()) : goldPrice.getPrice() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getEffectiveDate() != null ? getEffectiveDate().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        return result;
    }
}
