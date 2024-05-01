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
                .firstName("Test Name")
                .lastName("Test Surname")
                .email("test@gmail.com")
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
        String newFirstName = "Updated Name";

        // When
        user.setFirstName(newFirstName);
        userRepository.save(user);

        // Then
        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assert updatedUser != null;
        assertEquals(newFirstName, updatedUser.getFirstName());
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
        List<User> foundUsers = userRepository.findUsersBySubscribedCurrenciesId(currency.getId());

        // Then
        assertFalse(foundUsers.isEmpty());
        assertTrue(foundUsers.contains(user));
    }


}