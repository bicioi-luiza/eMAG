package com.example.demo.dtos.builders;

import com.example.demo.dtos.OrderItemDTO;
import com.example.demo.entities.*;

public class OrderItemBuilder {
    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .cartId(orderItem.getShoppingCart().getIdCart())
                .productId(orderItem.getProduct().getIdProd())
                .quantity(orderItem.getQuantity())
                .build();
    }
    public static OrderItem toEntity(OrderItemDTO orderItemDTO) {
        return OrderItem.builder()
                .id(orderItemDTO.getId())
                .shoppingCart(ShoppingCart.builder()
                        .idCart(orderItemDTO.getCartId())
                        .build())
                .product(Product.builder()
                        .idProd(orderItemDTO.getProductId())
                        .build())
                .quantity(orderItemDTO.getQuantity())
                .build();
    }
}
