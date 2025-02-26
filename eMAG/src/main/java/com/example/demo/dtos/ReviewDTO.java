package com.example.demo.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private Long idReview;

    private LocalDateTime reviewDate;
    private int rating;
    private String comment;

    private long user_id;


    private long product_id;
}
