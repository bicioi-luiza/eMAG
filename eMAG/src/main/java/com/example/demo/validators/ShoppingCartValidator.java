package com.example.demo.validators;

import com.example.demo.dtos.ShoppingCartDTO;
import com.example.demo.constants.ShoppingCartConstants;
import com.example.demo.entities.ShoppingCart;
import com.example.demo.entities.User;
import com.example.demo.repositories.ShoppingCartRepository;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class ShoppingCartValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartValidator.class);
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    public ShoppingCartValidator(UserRepository userRepository,ShoppingCartRepository shoppingCartRepository) {
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public void validateShoppingCartCreation(ShoppingCartDTO shoppingCart) {
        LOGGER.info(ShoppingCartConstants.VALIDATING_SHOPPING_CART_CREATION);

        if (shoppingCart == null) {
            LOGGER.error(ShoppingCartConstants.SHOPPING_CART_CANNOT_BE_NULL);
            throw new IllegalArgumentException(ShoppingCartConstants.SHOPPING_CART_CANNOT_BE_NULL);
        }

        if (shoppingCart.getUserId() == null) {
            LOGGER.error(ShoppingCartConstants.USER_ID_CANNOT_BE_NULL);
            throw new IllegalArgumentException(ShoppingCartConstants.USER_ID_CANNOT_BE_NULL);
        }

        Optional<User> user = userRepository.findById(shoppingCart.getUserId());
        if (!user.isPresent()) {
            LOGGER.error(ShoppingCartConstants.USER_NOT_FOUND_WITH_ID, shoppingCart.getUserId());
            throw new IllegalArgumentException(ShoppingCartConstants.USER_NOT_FOUND_WITH_ID);
        }

        LOGGER.info(ShoppingCartConstants.SHOPPING_CART_VALIDATION_SUCCESSFUL);
    }
    public void validateShoppingCartExists(Long shoppingCartId) throws Exception {
        LOGGER.info(ShoppingCartConstants.VALIDATING_SHOPPING_CART_EXISTS, shoppingCartId);

        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (!optionalShoppingCart.isPresent()) {
            LOGGER.error(ShoppingCartConstants.SHOPPING_CART_NOT_FOUND_WITH_ID, shoppingCartId);
            throw new Exception("Shopping cart not found with id: " + shoppingCartId);
        }
    }
}