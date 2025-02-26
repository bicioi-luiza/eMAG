package com.example.demo.dtos.builders;


import com.example.demo.dtos.OrderDTO;
import com.example.demo.dtos.OrderItemDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.ShoppingCart;
import com.example.demo.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class OrderBuilder {
    public static OrderDTO toOrderDTO(Order order) {
        /*List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(OrderItemBuilder::toOrderItemDTO)
                .collect(Collectors.toList());*/
        return OrderDTO.builder()
                .idOrder(order.getIdOrder())
                .orderDate(order.getOrderDate())
                //.total(order.getTotal())
                .users_id(order.getUser().getId())
                .orderItems(order.getOrderItems())
                .status(order.getStatus())
                .build();
    }

    public static Order toEntity(OrderDTO orderDTO) {
        /*List<OrderItem> ordersEn = orderDTO.getOrderItems().stream()
                .filter(orderItemDTO -> orderItemDTO.getId() != null) // Filter out null IDs
                .map(OrderItemBuilder::toEntity)
                .collect(Collectors.toList());*/
        return Order.builder()
                .idOrder(orderDTO.getIdOrder())
                .orderDate(orderDTO.getOrderDate())
                //.total(orderDTO.getTotal())
                .user(User.builder()
                        .id(orderDTO.getUsers_id())
                        .build())
                .orderItems(orderDTO.getOrderItems())
                .status(orderDTO.getStatus())
                .build();
    }

}
