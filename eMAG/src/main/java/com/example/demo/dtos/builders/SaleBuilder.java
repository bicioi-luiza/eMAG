package com.example.demo.dtos.builders;

import com.example.demo.dtos.SaleDTO;
import com.example.demo.entities.Product;

import com.example.demo.entities.Sale;
import com.example.demo.entities.User;

public class SaleBuilder {
    public static SaleDTO toSaleDTO(Sale sale) {
        return SaleDTO.builder()
                .idSale(sale.getIdSale())
                .oldPrice(sale.getOldPrice())
                .newPrice(sale.getNewPrice())
                .percent(sale.getPercent())
                .userId(sale.getUser().getId())
                .productId(sale.getProduct().getIdProd())
                .build();
    }
    public static Sale toEntity(SaleDTO saleDTO) {
        return Sale.builder()
                .idSale(saleDTO.getIdSale())
                .oldPrice(saleDTO.getOldPrice())
                .newPrice(saleDTO.getNewPrice())
                .percent(saleDTO.getPercent())
                .user(User.builder()
                        .id(saleDTO.getUserId())
                        .build())
                .product(Product.builder()
                        .idProd(saleDTO.getProductId())
                        .build())
                .build();
    }
}
