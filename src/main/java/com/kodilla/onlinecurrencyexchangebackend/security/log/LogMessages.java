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
    public static final String FETCHING_ALL_USERS = "Fetching all users";
    public static final String USERS_FETCHED_SUCCESS = "All users fetched successfully";

    // EmailService
    public static final String PREPARING_EMAIL_LOG = "Preparing to send email to user: {}";
    public static final String EMAIL_SENT_SUCCESS_LOG = "Email has been sent.";
    public static final String EMAIL_SENDING_FAILURE_LOG = "Failed to process email sending: {}";

    // Email Verification
    public static final String INVALID_VERIFICATION_TOKEN = "Invalid verification token: {}";
    public static final String EMAIL_VERIFIED_SUCCESS = "Email verified successfully for user: {}";

    // Currency Exchange Service
    public static final String ERROR_FETCHING_RATES = "Error while fetching exchange rates from the database";
    public static final String ERROR_FETCHING_RATES_BY_CODE_AND_DATE = "Error while fetching exchange rates by code and date from the database";
    public static final String ERROR_FETCHING_RATES_BY_CODE_AND_DATE_RANGE = "Error while fetching exchange rates by code and date range from the database";
    public static final String ERROR_FETCHING_RATES_BY_DATE = "Error while fetching exchange rates by date from the database";
    public static final String NO_RATES_FOUND = "No rates found for currency code: %s on date: %s";
    public static final String ERROR_FETCHING_RATES_BY_CODE = "Error while fetching exchange rates by code from the database";
    public static final String ERROR_FETCHING_RATES_BY_DATE_RANGE = "Error while fetching exchange rates by date range from the database";

    // Currency Service
    public static final String USER_SUBSCRIBED_TO_CURRENCY = "User with username: {} subscribed to currency: {}";
    public static final String USER_ALREADY_SUBSCRIBED_TO_CURRENCY = "User with username: {} is already subscribed to currency: {}";
    public static final String USER_UNSUBSCRIBED_FROM_CURRENCY = "User with username: {} unsubscribed from currency: {}";
    public static final String CURRENCY_OBSERVER_SUBSCRIBED = "Observer added for user with email: {} for currency: {}";
    public static final String CURRENCY_OBSERVER_UNSUBSCRIBED = "Observer removed for user with email: {} for currency: {}";
    public static final String CURRENCY_UPDATE_STARTED = "Starting scheduled currency update.";
    public static final String CURRENCY_UPDATE_COMPLETED = "Currency update completed.";

    // NBP API Client
    public static final String RECEIVED_DATA_FROM_API = "Received data from API: {}";
    public static final String ERROR_FETCHING_DATA = "Error fetching data from NBP API: {}";
    public static final String FETCHING_DATA_FROM_API = "Fetching data from API: {}";
    public static final String FETCHED_RATES_FROM_API = "Fetched {} rates from API. First rate: {}";
    public static final String CURRENCY_DETAILS = "Currency: {}, Code: {}, Average Rate: {}, Buying Rate: {}, Selling Rate: {}";
    public static final String INVALID_DATE_PROVIDED = "Invalid date provided for tableType {}: {}";

    // NBP API Date Validator
    public static final String HTTP_CLIENT_ERROR = "HTTP Client Error occurred: {} - {}";
    public static final String HTTP_SERVER_ERROR = "HTTP Server Error occurred: {} - {}";
    public static final String UNEXPECTED_ERROR_VALIDATING_DATE = "Unexpected error occurred while validating date {}: {}";
    public static final String DATE_IS_HOLIDAY_OR_WEEKEND = "Date {} is a holiday or weekend (no data available).";
    public static final String UNEXPECTED_RESPONSE_STATUS = "Unexpected response status {} for date {}";
}
