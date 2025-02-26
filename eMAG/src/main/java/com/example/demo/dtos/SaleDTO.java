package com.example.demo.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SaleDTO {
    private Long idSale;

    private double oldPrice;
    private double newPrice;
    private double percent;

    private long userId;


    private long productId;
}
