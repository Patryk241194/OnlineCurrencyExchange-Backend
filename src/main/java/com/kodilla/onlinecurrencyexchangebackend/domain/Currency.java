package com.kodilla.onlinecurrencyexchangebackend.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CURRENCIES")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CURRENCY_ID", unique = true)
    private Long id;
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;

    @OneToMany(
            targetEntity = ExchangeRate.class,
            mappedBy = "currency",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<ExchangeRate> exchangeRates = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency currency)) return false;

        if (getId() != null ? !getId().equals(currency.getId()) : currency.getId() != null) return false;
        if (getCode() != null ? !getCode().equals(currency.getCode()) : currency.getCode() != null) return false;
        return getName() != null ? getName().equals(currency.getName()) : currency.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
