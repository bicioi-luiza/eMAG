package com.example.demo.constants;

public class SaleConstants {
    // Error messages
    public static final String SALE_CANNOT_BE_NULL = "Sale cannot be null";
    public static final String USER_ID_CANNOT_BE_NULL = "User ID cannot be null";
    public static final String PRODUCT_ID_CANNOT_BE_NULL = "Product ID cannot be null";
    public static final String USER_NOT_FOUND_WITH_ID = "User with id {} was not found in db";
    public static final String PRODUCT_NOT_FOUND_WITH_ID = "Product with id {} was not found in db";
    public static final String SALE_NOT_FOUND_WITH_ID = "Sale with id {} was not found in db";
    public static final String PERCENT_INVALID = "Percent must be between 0 and 100";

    // Logger messages
    public static final String VALIDATING_SALE_CREATION = "Validating sale creation...";
    public static final String SALE_VALIDATION_SUCCESSFUL = "Sale validation successful";
    public static final String VALIDATING_SALE_EXISTS = "Validating if sale exists with id: {}";
}
