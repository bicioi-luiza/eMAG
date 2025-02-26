package com.example.demo.dtos;

import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ShoppingCartDTO {
    private Long idCart;
    private Long userId;
    private List<OrderItem> orderItems;
    private double total;

}
