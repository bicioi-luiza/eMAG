package com.example.demo.constants;



public class ReviewConstants {
    // Error messages
    public static final String REVIEW_CANNOT_BE_NULL = "Review cannot be null";
    public static final String USER_ID_CANNOT_BE_NULL = "User ID cannot be null";
    public static final String PRODUCT_ID_CANNOT_BE_NULL = "Product ID cannot be null";
    public static final String USER_NOT_FOUND_WITH_ID = "User with id {} was not found in db";
    public static final String PRODUCT_NOT_FOUND_WITH_ID = "Product with id {} was not found in db";
    public static final String REVIEW_NOT_FOUND_WITH_ID = "Review with id {} was not found in db";
    public static final String RATING_INVALID = "Rating must be between 1 and 10";

    // Logger messages
    public static final String VALIDATING_REVIEW_CREATION = "Validating review creation...";
    public static final String REVIEW_VALIDATION_SUCCESSFUL = "Review validation successful";
    public static final String VALIDATING_REVIEW_EXISTS = "Validating if review exists with id: {}";
}
