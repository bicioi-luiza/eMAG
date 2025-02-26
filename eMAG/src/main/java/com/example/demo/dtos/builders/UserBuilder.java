package com.example.demo.dtos.builders;

import com.example.demo.dtos.OrderDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.ShoppingCart;
import com.example.demo.entities.User;
import io.micrometer.common.lang.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserBuilder {

    public static UserDTO toUserDTO(User user) {
        /*List<OrderDTO> orderDTOs = user.getOrders().stream()
                .map(OrderBuilder::toOrderDTO)
                .collect(Collectors.toList());*/
       /* long idCart= Long.parseLong(null);
        if(user.getShoppingCart()!=null) idCart=user.getShoppingCart().getIdCart();*/
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .isAdmin(user.isAdmin())
                .orders(user.getOrders())
                .reviews(user.getReviews())
                .sales(user.getSales())
                .shoppingcart(user.getShoppingCart())
                .build();
    }

    public static User toEntity(UserDTO userDTO) {
        /*List<Order> ordersEn = userDTO.getOrders().stream()
                .map(OrderBuilder::toEntity)
                .collect(Collectors.toList());*/
        return User.builder()
                .id(userDTO.getId())
                .lastName(userDTO.getLastName())
                .firstName(userDTO.getFirstName())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .isAdmin(userDTO.isAdmin())
                .orders(userDTO.getOrders())
                .reviews(userDTO.getReviews())
                .sales(userDTO.getSales())
                .shoppingCart(userDTO.getShoppingcart() )
                .build();
    }
}
