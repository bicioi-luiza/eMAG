package com.example.demo.dtos.builders;


import com.example.demo.dtos.ShoppingCartDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.ShoppingCart;
import com.example.demo.entities.User;

public class ShoppingCartBuilder {
    public static ShoppingCartDTO toShoppingCartDTO(ShoppingCart shoppingCart) {



        return ShoppingCartDTO.builder()
                .idCart(shoppingCart.getIdCart())
                .total(shoppingCart.getTotal())
                .userId(shoppingCart.getUser().getId())
                .orderItems(shoppingCart.getOrderItems())

                .build();
    }
    public static ShoppingCart toEntity(ShoppingCartDTO shoppingCartDTO) {
        return ShoppingCart.builder()
                .idCart(shoppingCartDTO.getIdCart())
                .total(shoppingCartDTO.getTotal())
                .user(User.builder()
                        .id(shoppingCartDTO.getUserId())
                        .build())
                .orderItems(shoppingCartDTO.getOrderItems())

                .build();
    }
}
