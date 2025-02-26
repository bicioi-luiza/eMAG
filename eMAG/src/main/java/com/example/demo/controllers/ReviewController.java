package com.example.demo.controllers;

import com.example.demo.dtos.ReviewDTO;
import com.example.demo.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ModelAndView getAllReviews() {
        ModelAndView mav=new ModelAndView("reviews");
        List<ReviewDTO> dtos = reviewService.getAllReviews();
        mav.addObject("reviews",dtos);
        return mav;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        try {
            ReviewDTO reviewDTO = reviewService.getReviewById(id);
            return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/add-review")
    public ModelAndView showAddReviewForm(@RequestParam("productId") Long productId,
                                          @RequestParam("userId") Long userId) {
        ModelAndView mav = new ModelAndView("add-review");
        mav.addObject("productId", productId);
        mav.addObject("userId", userId);
        return mav;
    }


    /**
     *
     * @param reviewDTO
     * @return
     */
    @PostMapping("/insertReview")
    public ModelAndView createReview(@Valid @ModelAttribute ReviewDTO reviewDTO) {
        ModelAndView mav=new ModelAndView("reviews");

        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        mav.addObject("review",createdReview);
        return mav;
    }

    /**
     *
     * @param id
     * @param reviewDTO
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
    }
}
