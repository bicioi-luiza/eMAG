package com.example.demo.services;

import com.example.demo.dtos.ReviewDTO;
import com.example.demo.dtos.builders.ReviewBuilder;
import com.example.demo.entities.Product;
import com.example.demo.entities.Review;
import com.example.demo.entities.User;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.ReviewRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.validators.ReviewValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents a service layer for review-related operations
 */
@Service
public class ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewValidator reviewValidator;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,UserRepository userRepository,ProductRepository productRepository,ReviewValidator reviewValidator) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.reviewValidator = reviewValidator;
    }

    /**
     *
     * @return
     */
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviewList = reviewRepository.findAll();
        return reviewList.stream()
                .map(ReviewBuilder::toReviewDTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public ReviewDTO getReviewById(long id) throws Exception {
        reviewValidator.validateReviewExists(id);
        Optional<Review> review = reviewRepository.findById(id);

        return ReviewBuilder.toReviewDTO(review.get());
    }

    /**
     *
     * @param reviewDTO The DTO representing the review to be created.
     * @return
     */
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        reviewValidator.validateReviewCreation(reviewDTO);
        Review review = ReviewBuilder.toEntity(reviewDTO);
        review = reviewRepository.save(review);
        LOGGER.debug("Review with id {} was inserted in the database", review.getIdReview());
        return ReviewBuilder.toReviewDTO(review);
    }

    /**
     *
     * @param id
     * @param reviewDTO The DTO representing the review to be updated.
     * @return the updatedReviewDTO
     * @throws Exception
     */
    public ReviewDTO updateReview(long id, ReviewDTO reviewDTO) throws Exception {
        reviewValidator.validateReviewExists(id);
        reviewValidator.validateReviewCreation(reviewDTO);
        Optional<Review> optReview = reviewRepository.findById(id);

        Review existingReview = optReview.get();

        existingReview.setRating(reviewDTO.getRating());
        existingReview.setComment(reviewDTO.getComment());
        Optional<User> user=userRepository.findById(reviewDTO.getUser_id());
        if (user.isPresent()) {
            existingReview.setUser(user.get());
        } else {
            throw new Exception("User with id " + reviewDTO.getUser_id() + " not found");
        }

        Optional<Product> product=productRepository.findById(reviewDTO.getProduct_id());
        if (product.isPresent()) {
            existingReview.setProduct(product.get());
        } else {
            throw new Exception("Product with id " + reviewDTO.getProduct_id() + " not found");
        }

        Review updatedReview = reviewRepository.save(existingReview);

        return ReviewBuilder.toReviewDTO(updatedReview);
    }

    /**
     *
     * @param id
     * @throws Exception
     */
    public void deleteReview(long id) throws Exception {
        reviewValidator.validateReviewExists(id);
        Optional<Review> optReview = reviewRepository.findById(id);

        reviewRepository.deleteById(id);
    }
}
