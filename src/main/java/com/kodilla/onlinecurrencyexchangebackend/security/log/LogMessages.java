package com.kodilla.onlinecurrencyexchangebackend.security.log;

public class LogMessages {

    // Registration
    public static final String REGISTERING_USER = "Registering new user with email: {}";
    public static final String REGISTRATION_FAILED = "Registration failed: Username or email is already in use. Username: {}, Email: {}";
    public static final String USER_REGISTERED_SUCCESS = "User registered successfully with email: {}";

    // Authentication
    public static final String AUTHENTICATING_USER = "Authenticating user with email: {}";
    public static final String AUTHENTICATION_SUCCESS = "Authentication successful for email: {}";
    public static final String JWT_SUCCESS = "Authentication successful. JWT token generated for email: {}";
    public static final String AUTHENTICATION_FAILED = "Authentication failed for email: {}. Exception: {}";
    public static final String NO_ACCOUNT_FOUND = "Authentication failed: No account found for email: {}";

    // Validation
    public static final String VALIDATING_CREDENTIALS = "Validating user credentials for email: {}";
    public static final String VALIDATION_FAILED_USERNAME = "Validation failed: Incorrect username. Username: {}";
    public static final String VALIDATION_FAILED_EMAIL = "Validation failed: Incorrect email format. Email: {}";
    public static final String VALIDATION_FAILED_PASSWORD = "Validation failed: Incorrect password for email: {}";
    public static final String CREDENTIALS_VALIDATED_SUCCESS = "User credentials validated successfully for email: {}";

    // User Operations
    public static final String CHANGING_USERNAME = "Attempting to change username for user: {}";
    public static final String USERNAME_CHANGED_SUCCESS = "Username changed successfully for user: {}";
    public static final String AUTHENTICATION_SUCCESS_FOR_PASSWORD_CHANGE = "Authentication successful for username: {}";
    public static final String AUTHENTICATION_FAILED_FOR_PASSWORD_CHANGE = "Authentication failed for username: {} with provided current password";
    public static final String PASSWORD_CHANGED_SUCCESS = "Password changed successfully for user with username: {}";
    public static final String USER_NOT_FOUND = "User with username {} not found";
    public static final String USER_DELETED_SUCCESS = "User with username {} has been deleted";
    public static final String USER_PROMOTED_TO_ADMIN = "User {} promoted to ADMIN";
    public static final String ADMIN_DEMOTED_TO_USER = "Admin {} demoted to USER";
    public static final String FIND_USER_BY_USERNAME = "Finding user by username: {}";
    public static final String USER_FOUND_BY_USERNAME = "User found with username: {}";
}
