package com.example.demo.validators;


import com.example.demo.constants.OrderItemConstants;
import com.example.demo.dtos.OrderItemDTO;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.Product;
import com.example.demo.repositories.OrderItemRepository;
import com.example.demo.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class OrderItemValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemValidator.class);
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderItemValidator(OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public void validateOrderItemCreation(OrderItemDTO orderItemDTO) {
        LOGGER.info(OrderItemConstants.VALIDATING_ORDER_ITEM_CREATION);

        if (orderItemDTO == null) {
            LOGGER.error(OrderItemConstants.ORDER_ITEM_CANNOT_BE_NULL);
            throw new IllegalArgumentException(OrderItemConstants.ORDER_ITEM_CANNOT_BE_NULL);
        }

        if (orderItemDTO.getQuantity() <= 0) {
            LOGGER.error(OrderItemConstants.QUANTITY_MUST_BE_GREATER_THAN_ZERO);
            throw new IllegalArgumentException(OrderItemConstants.QUANTITY_MUST_BE_GREATER_THAN_ZERO);
        }

        validateProductExists(orderItemDTO.getProductId());

        LOGGER.info(OrderItemConstants.ORDER_ITEM_VALIDATION_SUCCESSFUL);
    }

    public void validateOrderItemUpdate(OrderItemDTO orderItemDTO) {
        LOGGER.info(OrderItemConstants.VALIDATING_ORDER_ITEM_UPDATE);

        if (orderItemDTO.getQuantity() <= 0) {
            LOGGER.error(OrderItemConstants.QUANTITY_MUST_BE_GREATER_THAN_ZERO);
            throw new IllegalArgumentException(OrderItemConstants.QUANTITY_MUST_BE_GREATER_THAN_ZERO);
        }

        LOGGER.info(OrderItemConstants.ORDER_ITEM_VALIDATION_SUCCESSFUL);
    }

    public void validateOrderItemExists(Long orderItemId) throws Exception {
        LOGGER.info(OrderItemConstants.VALIDATING_ORDER_ITEM_EXISTS, orderItemId);

        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);
        if (!optionalOrderItem.isPresent()) {
            LOGGER.error(OrderItemConstants.ORDER_ITEM_NOT_FOUND_WITH_ID, orderItemId);
            throw new Exception("Order item not found with id: " + orderItemId);
        }
    }

    public void validateProductExists(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            LOGGER.error(OrderItemConstants.PRODUCT_NOT_FOUND_WITH_ID, productId);
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }
    }
}

