package com.kodilla.onlinecurrencyexchangebackend.repository;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    List<Currency> findAll();

    List<Currency> findCurrenciesBySubscribedUsersId(Long userId);

    Optional<Currency> findByCode(String code);

    List<Currency> findCurrenciesByCode(String code);

    Optional<Currency> findFirstByCode(String code);

}
