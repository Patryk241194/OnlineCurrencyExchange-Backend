package com.kodilla.onlinecurrencyexchangebackend.security.authorization;

import com.kodilla.onlinecurrencyexchangebackend.domain.RoleStatus;
import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import com.kodilla.onlinecurrencyexchangebackend.error.auth.InvalidCredentialsException;
import com.kodilla.onlinecurrencyexchangebackend.error.auth.InvalidVerificationTokenException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserAlreadyExistsException;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.repository.UserRepository;
import com.kodilla.onlinecurrencyexchangebackend.security.dto.registration.AuthRequest;
import com.kodilla.onlinecurrencyexchangebackend.security.dto.registration.AuthResponse;
import com.kodilla.onlinecurrencyexchangebackend.security.dto.registration.RegisterRequest;
import com.kodilla.onlinecurrencyexchangebackend.security.jwt.JwtService;
import com.kodilla.onlinecurrencyexchangebackend.service.email.EmailService;
import com.kodilla.onlinecurrencyexchangebackend.service.email.Mail;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.kodilla.onlinecurrencyexchangebackend.security.log.LogMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    @Value("${app.domain.url}")
    private String domainUrl;

    public AuthResponse register(RegisterRequest request) throws Exception {
        log.info(REGISTERING_USER, request.getEmail());
        if (userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            log.warn(REGISTRATION_FAILED, request.getUsername(), request.getEmail());
            throw new UserAlreadyExistsException();
        }
        validateUserCredentials(request);
        String verificationToken = UUID.randomUUID().toString();
        var user = User.builder()
                .username(request.getUsername().strip())
                .email(request.getEmail().strip())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleStatus.USER)
                .emailVerified(false)
                .verificationToken(verificationToken)
                .build();
        userRepository.save(user);

        String verificationUrl = domainUrl + "/api/v1/auth/verify?token" + verificationToken;
        Mail mail = Mail.builder()
                .mailTo(user.getEmail())
                .subject("Please verify your email address")
                .message("To verify your email address, please click the link below:\n" + verificationUrl)
                .build();
        emailService.send(mail);

        log.info(USER_REGISTERED_SUCCESS, request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(jwtToken)
                .role(user.getRole().toString())
                .build();
    }

    public boolean verifyEmail(String token) {
        var user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> {
                    log.error(INVALID_VERIFICATION_TOKEN, token);
                    return new InvalidVerificationTokenException();
                });
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        log.info(EMAIL_VERIFIED_SUCCESS, user.getEmail());
        return true;
    }

    public AuthResponse authenticate(AuthRequest request) throws InvalidCredentialsException {
        log.info(AUTHENTICATING_USER, request.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            ));
            log.info(AUTHENTICATION_SUCCESS, request.getEmail());
        } catch (AuthenticationException e) {
            log.error(AUTHENTICATION_FAILED, request.getEmail(), e.getMessage());
            throw new InvalidCredentialsException();
        }
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error(NO_ACCOUNT_FOUND, request.getEmail());
                    return new UserNotFoundException();
                });
        var jwtToken = jwtService.generateToken(user);
        log.info(JWT_SUCCESS, request.getEmail());
        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(jwtToken)
                .role(user.getRole().toString())
                .build();
    }

    private void validateUserCredentials(RegisterRequest request) throws Exception {
        log.info(VALIDATING_CREDENTIALS, request.getEmail());

        if (!UserCredentialValidator.isNameValid(request.getUsername().strip())) {
            log.warn(VALIDATION_FAILED_USERNAME, request.getUsername());
            throw new Exception("Incorrect username");
        }
        if (!UserCredentialValidator.isEmailValid(request.getEmail().strip())) {
            log.warn(VALIDATION_FAILED_EMAIL, request.getEmail());
            throw new Exception("Incorrect email format");
        }
        if (!UserCredentialValidator.isPasswordValid(request.getPassword())) {
            log.warn(VALIDATION_FAILED_PASSWORD, request.getEmail());
            throw new Exception("Incorrect password");
        }
        log.info(CREDENTIALS_VALIDATED_SUCCESS, request.getEmail());
    }
}
