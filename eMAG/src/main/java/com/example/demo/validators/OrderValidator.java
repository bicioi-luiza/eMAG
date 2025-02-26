package com.example.demo.validators;


import com.example.demo.dtos.OrderDTO;
import com.example.demo.constants.OrderConstants;
import com.example.demo.entities.Order;
import com.example.demo.entities.User;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class OrderValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderValidator.class);
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderValidator(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public void validateOrderCreation(OrderDTO order) {
        LOGGER.info(OrderConstants.VALIDATING_ORDER_CREATION);

        if (order.getUsers_id() == null) {
            LOGGER.error(OrderConstants.USER_ID_CANNOT_BE_NULL);
            throw new IllegalArgumentException(OrderConstants.USER_ID_CANNOT_BE_NULL);
        }

        Optional<User> user = userRepository.findById(order.getUsers_id());
        if (!user.isPresent()) {
            LOGGER.error(OrderConstants.USER_NOT_FOUND_WITH_ID, order.getUsers_id());
            throw new IllegalArgumentException(OrderConstants.USER_NOT_FOUND_WITH_ID);
        }

        LOGGER.info(OrderConstants.ORDER_VALIDATION_SUCCESSFUL);
    }

    public void validateOrderUpdate(Long orderId, OrderDTO order) throws Exception {
        LOGGER.info(OrderConstants.VALIDATING_ORDER_UPDATE);

        if (orderId == null) {
            LOGGER.error(OrderConstants.ORDER_ID_CANNOT_BE_NULL);
            throw new IllegalArgumentException(OrderConstants.ORDER_ID_CANNOT_BE_NULL);
        }

        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (!existingOrder.isPresent()) {
            LOGGER.error(OrderConstants.ORDER_NOT_FOUND_WITH_ID, orderId);
            throw new Exception(Order.class.getSimpleName() + " with id: " + orderId);
        }

        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            LOGGER.error(OrderConstants.STATUS_CANNOT_BE_EMPTY);
            throw new IllegalArgumentException(OrderConstants.STATUS_CANNOT_BE_EMPTY);
        }

        LOGGER.info(OrderConstants.ORDER_ID_VALIDATION_SUCCESSFUL);
    }

    public void validateOrderExists(Long orderId) throws Exception {
        Optional<Order> optOrder = orderRepository.findById(orderId);
        if (!optOrder.isPresent()) {
            LOGGER.error(OrderConstants.ORDER_NOT_FOUND_WITH_ID, orderId);
            throw new Exception(Order.class.getSimpleName() + " with id: " + orderId);
        }
    }
}
