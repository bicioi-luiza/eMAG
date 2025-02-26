package com.example.demo.dtos.builders;

import com.example.demo.dtos.OrderItemDTO;
import com.example.demo.dtos.ProductDTO;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class ProductBuilder {
    public static ProductDTO toProductDTO(Product product) {
        /*List<OrderItemDTO> orderItemDTOs = product.getOrderItems().stream()
                .map(OrderItemBuilder::toOrderItemDTO)
                .collect(Collectors.toList());*/
        return ProductDTO.builder()
                .idProd(product.getIdProd())
                .nameProd(product.getNameProd())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .stock(product.getStock())
                .orderItems(product.getOrderItems())
                .reviews(product.getReviews())
                .sales(product.getSales())
                .build();
    }

    public static Product toEntity(ProductDTO productDTO) {
        /*List<OrderItem> ordersEn = productDTO.getOrderItems().stream()
                .map(OrderItemBuilder::toEntity)
                .collect(Collectors.toList());*/
        return Product.builder()
                .idProd(productDTO.getIdProd())
                .nameProd(productDTO.getNameProd())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .category(productDTO.getCategory())
                .stock(productDTO.getStock())
                .orderItems(productDTO.getOrderItems())
                .reviews(productDTO.getReviews())
                .sales(productDTO.getSales())
                .build();
    }
}
