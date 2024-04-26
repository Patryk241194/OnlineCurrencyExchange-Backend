package com.kodilla.onlinecurrencyexchangebackend.repository;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.ExchangeRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {

    List<ExchangeRate> findAll();

    List<ExchangeRate> findExchangeRatesByCurrency_Id(Long id);

    Optional<ExchangeRate> findByCurrencyAndEffectiveDate(Currency currency, LocalDate effectiveDate);

}
