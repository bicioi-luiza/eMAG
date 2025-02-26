package com.example.demo.validators;



import com.example.demo.dtos.ProductDTO;
import com.example.demo.constants.ProductConstants;
import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductValidator.class);
    private final ProductRepository productRepository;

    public ProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public static void validateProduct(ProductDTO product) {
        LOGGER.info(ProductConstants.VALIDATING_PRODUCT);

        if (product == null) {
            LOGGER.error(ProductConstants.PRODUCT_CANNOT_BE_NULL);
            throw new IllegalArgumentException(ProductConstants.PRODUCT_CANNOT_BE_NULL);
        }

        if (product.getNameProd() == null || product.getNameProd().isEmpty()) {
            LOGGER.error(ProductConstants.PRODUCT_NAME_CANNOT_BE_EMPTY);
            throw new IllegalArgumentException(ProductConstants.PRODUCT_NAME_CANNOT_BE_EMPTY);
        }

        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            LOGGER.error(ProductConstants.PRODUCT_DESCRIPTION_CANNOT_BE_EMPTY);
            throw new IllegalArgumentException(ProductConstants.PRODUCT_DESCRIPTION_CANNOT_BE_EMPTY);
        }

        if (product.getPrice() <= 0) {
            LOGGER.error(ProductConstants.INVALID_PRODUCT_PRICE);
            throw new IllegalArgumentException(ProductConstants.INVALID_PRODUCT_PRICE);
        }

        if (product.getCategory() == null || product.getCategory().isEmpty()) {
            LOGGER.error(ProductConstants.PRODUCT_CATEGORY_CANNOT_BE_EMPTY);
            throw new IllegalArgumentException(ProductConstants.PRODUCT_CATEGORY_CANNOT_BE_EMPTY);
        }

        if (product.getStock() < 0) {
            LOGGER.error(ProductConstants.INVALID_PRODUCT_STOCK);
            throw new IllegalArgumentException(ProductConstants.INVALID_PRODUCT_STOCK);
        }

        LOGGER.info(ProductConstants.PRODUCT_VALIDATION_SUCCESSFUL);
    }
    public void validateProductId(Long id) throws Exception {
        LOGGER.info(ProductConstants.VALIDATING_PRODUCT_ID);
        Optional<Product> optProduct = productRepository.findById(id);
        if (!optProduct.isPresent()) {
            LOGGER.error(ProductConstants.PRODUCT_NOT_FOUND_WITH_ID, id);
            throw new Exception(Product.class.getSimpleName() + " with id: " + id);
        }
        LOGGER.info(ProductConstants.PRODUCT_ID_VALIDATION_SUCCESSFUL);
    }
}
