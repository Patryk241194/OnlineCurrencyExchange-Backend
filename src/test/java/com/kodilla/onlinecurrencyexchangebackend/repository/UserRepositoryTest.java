package com.kodilla.onlinecurrencyexchangebackend.repository;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.RoleStatus;
import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("test_username")
                .email("test@gmail.com")
                .password("test_password")
                .role(RoleStatus.USER)
                .build();
        userRepository.save(user);
    }

    @Test
    void userRepositoryCreateTest() {
        // When
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        // Then
        assertEquals(user, foundUser);
    }

    @Test
    void userRepositoryUpdateTest() {
        // Given
        String newUsername = "updated_username";

        // When
        user.setUsername(newUsername);
        userRepository.save(user);

        // Then
        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(newUsername, updatedUser.getUsername());
    }

    @Test
    void userRepositoryDeleteTest() {
        // When
        Long id = user.getId();
        userRepository.deleteById(user.getId());

        // Then
        assertFalse(userRepository.existsById(id));
    }

    @Test
    void ManyToManyRelationBetweenCurrencyAndUserTest() {
        // Given
        Currency currency = Currency.builder()
                .code("USD")
                .name("dolar amerykański")
                .build();
        currencyRepository.save(currency);

        user.getSubscribedCurrencies().add(currency);
        currency.getSubscribedUsers().add(user);
        userRepository.save(user);
        currencyRepository.save(currency);

        // When
        Set<User> foundUsers = userRepository.findUsersBySubscribedCurrenciesId(currency.getId());

        // Then
        assertFalse(foundUsers.isEmpty());
        assertTrue(foundUsers.contains(user));
    }


}