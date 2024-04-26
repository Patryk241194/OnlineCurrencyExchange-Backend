package com.kodilla.onlinecurrencyexchangebackend.mapper.validator;

import com.kodilla.onlinecurrencyexchangebackend.domain.RoleStatus;

public class RoleStatusValidator {

    public static boolean isValidRole(String role) {
        if (role == null) {
            return false;
        }

        try {
            RoleStatus.valueOf(role.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
