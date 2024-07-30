package com.kodilla.onlinecurrencyexchangebackend.service.domain;

import com.kodilla.onlinecurrencyexchangebackend.domain.Currency;
import com.kodilla.onlinecurrencyexchangebackend.domain.RoleStatus;
import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.EditUserPasswordRequest;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.EditUserRequest;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.EditUserResponse;
import com.kodilla.onlinecurrencyexchangebackend.dto.user.UserDetailResponse;
import com.kodilla.onlinecurrencyexchangebackend.error.auth.InvalidCredentialsException;
import com.kodilla.onlinecurrencyexchangebackend.error.auth.InvalidPasswordException;
import com.kodilla.onlinecurrencyexchangebackend.error.auth.UnauthorizedActionException;
import com.kodilla.onlinecurrencyexchangebackend.error.currency.CurrencyNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.repository.CurrencyRepository;
import com.kodilla.onlinecurrencyexchangebackend.repository.UserRepository;
import com.kodilla.onlinecurrencyexchangebackend.security.authorization.UserCredentialValidator;
import com.kodilla.onlinecurrencyexchangebackend.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.kodilla.onlinecurrencyexchangebackend.security.log.LogMessages.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public EditUserResponse changeUsername(EditUserRequest request, String token) throws Exception {
        log.info(CHANGING_USERNAME, request.getUsername());
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        validateUserCredentials(request);
        user.setUsername(request.getUsername().strip());
        userRepository.save(user);
        log.info(USERNAME_CHANGED_SUCCESS, username);
        return EditUserResponse.builder().username(user.getUsername()).build();
    }

    public void changeUserPassword(EditUserPasswordRequest request, String token) {
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getOldPassword()));
            log.info(AUTHENTICATION_SUCCESS_FOR_PASSWORD_CHANGE, username);
        } catch (AuthenticationException e) {
            log.error(AUTHENTICATION_FAILED_FOR_PASSWORD_CHANGE, username);
            throw new InvalidCredentialsException();
        }
        if (!UserCredentialValidator.isPasswordValid(request.getNewPassword())) {
            log.warn(VALIDATION_FAILED_PASSWORD, username);
            throw new InvalidPasswordException();
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info(PASSWORD_CHANGED_SUCCESS, username);
    }

    public void deleteUser(String token) {
        String username = jwtService.extractUsername(token);
        if (userRepository.findByUsername(username).isEmpty()) {
            log.error(USER_NOT_FOUND, username);
            throw new UserNotFoundException();
        }
        userRepository.deleteByUsername(username);
        log.info(USER_DELETED_SUCCESS, username);
    }

    public void promoteUserToAdmin(EditUserRequest request, String token) {
        validateAdminToken(token);
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(UserNotFoundException::new);
        user.setRole(RoleStatus.ADMIN);
        userRepository.save(user);
        log.info(USER_PROMOTED_TO_ADMIN, request.getUsername());
    }

    public void demoteAdminToUser(EditUserRequest request, String token) {
        validateAdminToken(token);
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(UserNotFoundException::new);
        user.setRole(RoleStatus.USER);
        userRepository.save(user);
        log.info(ADMIN_DEMOTED_TO_USER, request.getUsername());
    }

    public UserDetailResponse findUserByUsername(String username) {
        log.info(FIND_USER_BY_USERNAME, username);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        List<String> subscribedCurrencies = user.getSubscribedCurrencies().stream()
                .map(Currency::getCode)
                .toList();
        log.info(USER_FOUND_BY_USERNAME, username);
        return UserDetailResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .subscribedCurrencies(subscribedCurrencies)
                .build();
    }

    public List<UserDetailResponse> getAllUsers() {
        log.info(FETCHING_ALL_USERS);
        List<User> users = userRepository.findAll();
        List<UserDetailResponse> userDetailResponses = users.stream()
                .map(user -> UserDetailResponse.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(String.valueOf(user.getRole()))
                        .subscribedCurrencies(user.getSubscribedCurrencies().stream()
                                .map(Currency::getCode)
                                .toList())
                        .build())
                .toList();
        log.info(USERS_FETCHED_SUCCESS);
        return userDetailResponses;
    }

    private void validateUserCredentials(EditUserRequest request) throws Exception {
        if (!UserCredentialValidator.isNameValid(request.getUsername().strip())) {
            log.warn(VALIDATION_FAILED_USERNAME, request.getUsername());
            throw new Exception("Incorrect username");
        }
    }

    private void validateAdminToken(String token) {
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != RoleStatus.ADMIN) {
            throw new UnauthorizedActionException();
        }
    }

    // TODO: 22.07.2024 methods that may need to be removed

    public void subscribeUserToCurrency(String currencyCode, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Currency currency = currencyRepository.findByCode(currencyCode).orElseThrow(CurrencyNotFoundException::new);
        if (!user.getSubscribedCurrencies().contains(currency)) {
            user.getSubscribedCurrencies().add(currency);
            currency.getSubscribedUsers().add(user);
            userRepository.save(user);
            currencyRepository.save(currency);
        }
    }

    public List<User> getUsersBySubscribedCurrenciesId(Long id) {
        return userRepository.findUsersBySubscribedCurrenciesId(id);
    }
}
