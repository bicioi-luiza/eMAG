package com.example.demo.dtos;

import com.example.demo.entities.OrderItem;
import com.example.demo.entities.ShoppingCart;
import com.example.demo.entities.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class OrderDTO {
    private Long idOrder;
    private LocalDateTime orderDate;
    //private double total;
    private Long users_id;

    private String status;

    private List<OrderItem> orderItems;
}
