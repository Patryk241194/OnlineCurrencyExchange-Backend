package com.kodilla.onlinecurrencyexchangebackend.security.authorization;

import java.util.regex.Pattern;

public class UserCredentialValidator {

    public static boolean isEmailValid(String email) {
        return email.matches("^(.+)@(.+)$");
    }

    public static boolean isNameValid(String name) {
        return !name.isBlank();
    }

    public static boolean isPasswordValid(String password) {
        final int MIN_LENGTH = 8;

        if (password.length() <= MIN_LENGTH) {
            return false;
        }

        final Pattern lowerCase = Pattern.compile("\\p{Ll}", Pattern.UNICODE_CASE);
        final Pattern upperCase = Pattern.compile("\\p{Lu}", Pattern.UNICODE_CASE);
        final Pattern numbers = Pattern.compile("[0-9]");
        final Pattern specials = Pattern.compile("[ !\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~]");

        boolean hasLower = lowerCase.matcher(password).find();
        boolean hasUpper = upperCase.matcher(password).find();
        boolean hasNumber = numbers.matcher(password).find();
        boolean hasSpecial = specials.matcher(password).find();

        return hasLower && hasUpper && hasNumber && hasSpecial;
    }

}
