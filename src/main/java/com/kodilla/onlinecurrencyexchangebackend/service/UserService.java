package com.kodilla.onlinecurrencyexchangebackend.service;

import com.kodilla.onlinecurrencyexchangebackend.domain.RoleStatus;
import com.kodilla.onlinecurrencyexchangebackend.domain.User;
import com.kodilla.onlinecurrencyexchangebackend.dto.UserDto;
import com.kodilla.onlinecurrencyexchangebackend.error.user.UserNotFoundException;
import com.kodilla.onlinecurrencyexchangebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(userId);
    }

    public User updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getRole() != null) {
            user.setRole(RoleStatus.valueOf(userDto.getRole()));
        }
        return userRepository.save(user);
    }
}
