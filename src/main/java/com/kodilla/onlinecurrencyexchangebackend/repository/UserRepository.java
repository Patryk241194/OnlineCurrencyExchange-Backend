package com.kodilla.onlinecurrencyexchangebackend.repository;

import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    Set<User> findUsersBySubscribedCurrenciesId(Long currencyId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    void deleteByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByVerificationToken(String token);

}
