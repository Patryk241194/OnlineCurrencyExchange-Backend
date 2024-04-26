package com.kodilla.onlinecurrencyexchangebackend.mapper;

import com.kodilla.onlinecurrencyexchangebackend.domain.RoleStatus;
import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import com.kodilla.onlinecurrencyexchangebackend.dto.UserDto;
import com.kodilla.onlinecurrencyexchangebackend.mapper.validator.RoleStatusValidator;

public class UserMapper implements Mapper<UserDto, User> {

    @Override
    public UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                (user.getRole() != null) ? user.getRole().toString() : null
        );
    }

    @Override
    public User mapToEntity(UserDto userDto) {
        RoleStatus roleStatus = RoleStatusValidator.isValidRole(userDto.getRole()) ?
                RoleStatus.valueOf(userDto.getRole().toUpperCase()) : null;
        return new User(
                userDto.getId(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                roleStatus
        );
    }
}
