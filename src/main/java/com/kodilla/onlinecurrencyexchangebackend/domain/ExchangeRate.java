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
@Table(name = "EXCHANGE_RATES")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXCHANGE_RATE_ID", unique = true)
    private Long id;
    @Column(name = "SELLING_RATE", precision = 8, scale = 4)
    private BigDecimal sellingRate;
    @Column(name = "BUYING_RATE", precision = 8, scale = 4)
    private BigDecimal buyingRate;
    @Column(name = "AVERAGE_RATE", precision = 8, scale = 4)
    private BigDecimal averageRate;
    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;
    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID")
    private Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRate that)) return false;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getSellingRate() != null ? !getSellingRate().equals(that.getSellingRate()) : that.getSellingRate() != null)
            return false;
        if (getBuyingRate() != null ? !getBuyingRate().equals(that.getBuyingRate()) : that.getBuyingRate() != null)
            return false;
        if (getAverageRate() != null ? !getAverageRate().equals(that.getAverageRate()) : that.getAverageRate() != null)
            return false;
        if (getEffectiveDate() != null ? !getEffectiveDate().equals(that.getEffectiveDate()) : that.getEffectiveDate() != null)
            return false;
        return getCurrency() != null ? getCurrency().equals(that.getCurrency()) : that.getCurrency() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getSellingRate() != null ? getSellingRate().hashCode() : 0);
        result = 31 * result + (getBuyingRate() != null ? getBuyingRate().hashCode() : 0);
        result = 31 * result + (getAverageRate() != null ? getAverageRate().hashCode() : 0);
        result = 31 * result + (getEffectiveDate() != null ? getEffectiveDate().hashCode() : 0);
        result = 31 * result + (getCurrency() != null ? getCurrency().hashCode() : 0);
        return result;
    }
}
