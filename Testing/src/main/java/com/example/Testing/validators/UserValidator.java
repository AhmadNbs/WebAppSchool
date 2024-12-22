package com.example.Testing.validators;

import com.example.Testing.Entity.User;
import com.example.Testing.Repository.UserRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class UserValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidator.class);

    public static void validateUserId(UUID id, UserRepository userRepository) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
    }

    public static void validateUserEmail(String email, UserRepository userRepository) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isEmpty()) {
            LOGGER.error("User with email {} was not found in db", email);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with email: " + email);
        }
    }
}
