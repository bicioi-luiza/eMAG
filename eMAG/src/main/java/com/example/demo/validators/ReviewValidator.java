package com.example.demo.validators;



import com.example.demo.constants.ReviewConstants;
import com.example.demo.dtos.ReviewDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.Review;
import com.example.demo.entities.User;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.ReviewRepository;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class ReviewValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewValidator.class);
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ReviewValidator(UserRepository userRepository, ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    public void validateReviewCreation(ReviewDTO reviewDTO) {
        LOGGER.info(ReviewConstants.VALIDATING_REVIEW_CREATION);

        if (reviewDTO == null) {
            LOGGER.error(ReviewConstants.REVIEW_CANNOT_BE_NULL);
            throw new IllegalArgumentException(ReviewConstants.REVIEW_CANNOT_BE_NULL);
        }

        if (reviewDTO.getUser_id() == 0) {
            LOGGER.error(ReviewConstants.USER_ID_CANNOT_BE_NULL);
            throw new IllegalArgumentException(ReviewConstants.USER_ID_CANNOT_BE_NULL);
        }

        if (reviewDTO.getProduct_id() == 0) {
            LOGGER.error(ReviewConstants.PRODUCT_ID_CANNOT_BE_NULL);
            throw new IllegalArgumentException(ReviewConstants.PRODUCT_ID_CANNOT_BE_NULL);
        }

        if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 10) {
            LOGGER.error(ReviewConstants.RATING_INVALID);
            throw new IllegalArgumentException(ReviewConstants.RATING_INVALID);
        }

        Optional<User> user = userRepository.findById(reviewDTO.getUser_id());
        if (!user.isPresent()) {
            LOGGER.error(ReviewConstants.USER_NOT_FOUND_WITH_ID, reviewDTO.getUser_id());
            throw new IllegalArgumentException(ReviewConstants.USER_NOT_FOUND_WITH_ID);
        }

        Optional<Product> product = productRepository.findById(reviewDTO.getProduct_id());
        if (!product.isPresent()) {
            LOGGER.error(ReviewConstants.PRODUCT_NOT_FOUND_WITH_ID, reviewDTO.getProduct_id());
            throw new IllegalArgumentException(ReviewConstants.PRODUCT_NOT_FOUND_WITH_ID);
        }

        LOGGER.info(ReviewConstants.REVIEW_VALIDATION_SUCCESSFUL);
    }

    public void validateReviewExists(Long reviewId) throws Exception {
        LOGGER.info(ReviewConstants.VALIDATING_REVIEW_EXISTS, reviewId);

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (!optionalReview.isPresent()) {
            LOGGER.error(ReviewConstants.REVIEW_NOT_FOUND_WITH_ID, reviewId);
            throw new Exception("Review not found with id: " + reviewId);
        }
    }
}

