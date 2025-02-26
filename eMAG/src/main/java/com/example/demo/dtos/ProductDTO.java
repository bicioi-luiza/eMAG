package com.example.demo.dtos;

import com.example.demo.entities.OrderItem;
import com.example.demo.entities.Review;
import com.example.demo.entities.Sale;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductDTO {
    private Long idProd;
    private String nameProd;
    private String description;
    private double price;
    private String category;
    private int stock;

    private List<OrderItem> orderItems;
    private List<Review> reviews;
    private List<Sale> sales;
}
