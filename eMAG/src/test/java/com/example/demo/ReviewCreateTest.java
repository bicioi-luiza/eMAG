package com.example.demo;

import com.example.demo.dtos.ReviewDTO;
import com.example.demo.dtos.builders.ReviewBuilder;
import com.example.demo.repositories.ReviewRepository;
import com.example.demo.services.ReviewService;
import com.example.demo.validators.ReviewValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReviewCreateTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewValidator reviewValidator;

    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateReview() {
        // Given
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewDate(LocalDateTime.now())
                .rating(5)
                .comment("Great product")
                .user_id(1L)
                .product_id(1L)
                .build();

        when(reviewRepository.save(any())).thenReturn(ReviewBuilder.toEntity(reviewDTO));

        // When
        ReviewDTO createdReviewDTO = reviewService.createReview(reviewDTO);

        // Then
        verify(reviewValidator, times(1)).validateReviewCreation(reviewDTO);
        verify(reviewRepository, times(1)).save(any());

        assertEquals(reviewDTO.getReviewDate(), createdReviewDTO.getReviewDate());
        assertEquals(reviewDTO.getRating(), createdReviewDTO.getRating());
        assertEquals(reviewDTO.getComment(), createdReviewDTO.getComment());
        assertEquals(reviewDTO.getUser_id(), createdReviewDTO.getUser_id());
        assertEquals(reviewDTO.getProduct_id(), createdReviewDTO.getProduct_id());
    }
}
