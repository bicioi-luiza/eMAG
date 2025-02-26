package com.example.demo.validators;
import com.example.demo.constants.UserConstants;
import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidator.class);
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public static void validateUser(UserDTO user) {
        LOGGER.info(UserConstants.VALIDATING_USER);

        if (user == null) {
            LOGGER.error(UserConstants.USER_CANNOT_BE_NULL);
            throw new IllegalArgumentException(UserConstants.USER_CANNOT_BE_NULL);
        }

        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            LOGGER.error(UserConstants.FIRST_NAME_CANNOT_BE_EMPTY);
            throw new IllegalArgumentException(UserConstants.FIRST_NAME_CANNOT_BE_EMPTY);
        }

        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            LOGGER.error(UserConstants.LAST_NAME_CANNOT_BE_EMPTY);
            throw new IllegalArgumentException(UserConstants.LAST_NAME_CANNOT_BE_EMPTY);
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            LOGGER.error(UserConstants.USERNAME_CANNOT_BE_EMPTY);
            throw new IllegalArgumentException(UserConstants.USERNAME_CANNOT_BE_EMPTY);
        }

        if (user.getPassword() == null || user.getPassword().length() < 8) {
            LOGGER.error(UserConstants.PASSWORD_LENGTH_ERROR);
            throw new IllegalArgumentException(UserConstants.PASSWORD_LENGTH_ERROR);
        }

        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            LOGGER.error(UserConstants.INVALID_EMAIL_ADDRESS);
            throw new IllegalArgumentException(UserConstants.INVALID_EMAIL_ADDRESS);
        }

        LOGGER.info(UserConstants.USER_VALIDATION_SUCCESSFUL);
    }

    public void validateUserId(Long id) throws Exception {
        LOGGER.info(UserConstants.VALIDATING_USER_ID);
        Optional<User> optUser = userRepository.findById(id);
        if (!optUser.isPresent()) {
            LOGGER.error(UserConstants.USER_NOT_FOUND_WITH_ID, id);
            throw new Exception(User.class.getSimpleName() + " with id: " + id);
        }
        LOGGER.info(UserConstants.USER_ID_VALIDATION_SUCCESSFUL);
    }

    private static boolean isValidEmail(String email) {

        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }
}



